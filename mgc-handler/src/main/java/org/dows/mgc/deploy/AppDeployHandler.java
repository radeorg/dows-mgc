package org.dows.mgc.deploy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.GenerationSessionRegistry;
import org.dows.mgc.builder.VueProjectBuilder;
import org.dows.mgc.constant.AppConstant;
import org.dows.mgc.entity.AppEntity;
import org.dows.mgc.exception.BusinessException;
import org.dows.mgc.exception.ErrorCode;
import org.dows.mgc.exception.ThrowUtils;
import org.dows.mgc.generator.AiCodeGenerator;
import org.dows.mgc.handler.StreamHandlerExecutor;
import org.dows.mgc.model.enums.ChatHistoryMessageTypeEnum;
import org.dows.mgc.model.enums.CodeGenTypeEnum;
import org.dows.mgc.monitor.MonitorContext;
import org.dows.mgc.monitor.MonitorContextHolder;
import org.dows.mgc.service.AppService;
import org.dows.mgc.service.ChatHistoryService;
import org.dows.mgc.service.GenerationStatusService;
import org.dows.mgc.service.UserService;
import org.dows.mgc.uim.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.File;
import java.time.LocalDateTime;

import static org.dows.mgc.constant.AppConstant.CODE_OUTPUT_ROOT_DIR;

@Component
@Slf4j
public class AppDeployHandler {

    @Value("${code.deploy-host:http://localhost}")
    private String deployHost;

    private GenerationStatusService generationStatusService;

    @Resource
    private StreamHandlerExecutor streamHandlerExecutor;

    @Resource
    private VueProjectBuilder vueProjectBuilder;

    @Resource
    private AiCodeGenerator aiCodeGenerator;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;


    public Flux<String> chatToGenCode(Long appId, String message, User loginUser) {
        // 1. 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "用户消息不能为空");
        // 2. 查询应用信息
        AppEntity app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3. 验证用户是否有权限访问该应用，仅本人可以生成代码
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限访问该应用");
        }
        // 4.检验应用是否为vip专属，并且检验用户是否为vip
        if (app.getIsVipOnly() && !userService.isVip(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "该应用为 VIP 专属应用，请先开通 VIP");
        }
        // 5. 获取应用的代码生成类型
        String codeGenTypeStr = app.getCodeGenType();
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenTypeStr);
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型");
        }
        // 6. 调用 AI 前，先将用户消息保存到数据库中
        chatHistoryService.addChatMessage(appId, message, ChatHistoryMessageTypeEnum.USER.getValue(), loginUser.getId());
        // 7.设置线程监控上下文，便于通过threadlocal来获取userid和appid
        MonitorContextHolder.setContext(
                MonitorContext.builder()
                        .userId(loginUser.getId().toString())
                        .appId(appId.toString())
                        .build());
        // 8. 标记生成状态 running（sessionId 暂用时间戳）
        try {
            String sessionId = String.valueOf(System.currentTimeMillis());
            generationStatusService.markRunning(appId, sessionId);
            GenerationSessionRegistry.setSession(appId, sessionId);
        } catch (Exception e) {
            log.warn("标记运行状态失败 appId={} error={}", appId, e.getMessage());
        }
        // 9. 调用 AI 生成代码，并返回结果流
        Flux<String> stringFlux = aiCodeGenerator.generateCodeAndSaveStream(message, codeGenTypeEnum, appId);
        // 10.收集响应内容并在完成过后记录到对话历史中 异步
        return streamHandlerExecutor.doExecutor(stringFlux, codeGenTypeEnum, chatHistoryService, appId, loginUser)
                .doFinally(signalType -> {
                    MonitorContextHolder.clearContext(); // 异步清除上下文
                });
    }


    /**
     * 应用部署
     *
     * @param appId
     * @param loginUser
     * @return
     */
    public String deployApp(Long appId, User loginUser) {
        // 1.参数校验
        if (appId == null || appId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        }
        if (loginUser == null || loginUser.getId() == null || loginUser.getId() <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录或登录信息不完整");
        }
        // 2.查询应用信息
        AppEntity app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");

        // 3.验证应用部署权限
        boolean isAppOwner = app.getUserId().equals(loginUser.getId());
        boolean isVipUser = userService.isVip(loginUser);
        boolean isGoodApp = AppConstant.GOOD_APP_PRIORITY.equals(app.getPriority());

        // 权限判断：应用创建者可以部署自己的应用，VIP用户可以部署精选应用
        if (!isAppOwner && !(isVipUser && isGoodApp)) {
            if (!isVipUser && isGoodApp) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "精选应用仅VIP用户可部署，请先升级VIP");
            } else {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限部署该应用");
            }
        }

        // 4.验证应用是否为vip专属，并且检验用户是否为vip
        if (app.getIsVipOnly() && !userService.isVip(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "该应用为 VIP 专属应用，请先开通 VIP");
        }
        // 5.检查是否有deploykey(6位大小写字母+数字),没有则生成
        String deployKey = app.getDeployKey();
        if (StrUtil.isBlank(deployKey)) {
            deployKey = RandomUtil.randomString(6);
        }
        // 6.获取代码生成类型，构建源目录路径
        String codeGenTypeStr = app.getCodeGenType();
        String sourceDirName = codeGenTypeStr + "_" + appId;
        String sourceDirPath = CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName; // 注意分割
        // 7.检查源目录是否存在
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用代码生成目录不存在，请先生成代码");
        }
        // vue处理
        CodeGenTypeEnum codeGenType = CodeGenTypeEnum.getEnumByValue(codeGenTypeStr);
        if (codeGenType == CodeGenTypeEnum.VUE_PROJECT) {
            boolean result = vueProjectBuilder.buildVueProject(sourceDirPath);
            ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR, "Vue项目构建失败，请检查代码生成目录是否正确");
            // 检查生成的目录是否存在
            File distDir = new File(sourceDirPath, "dist");
            if (!distDir.exists() || !distDir.isDirectory()) {
                ThrowUtils.throwIf(!distDir.exists(), ErrorCode.SYSTEM_ERROR, "Vue 项目构建完成但未生成 dist 目录");
            }
            sourceDir = distDir; // 将源目录指向 dist 目录
        }
        // 8.复制文件到部署目录
        String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
        try {
            FileUtil.copyContent(sourceDir, new File(deployDirPath), true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用部署失败：" + e.getMessage());
        }
        // 9.更新应用的deploykey和部��时间
        AppEntity updateApp = new AppEntity();
        updateApp.setId(appId);
        updateApp.setDeployKey(deployKey);
        updateApp.setUpdateTime(LocalDateTime.now());
        // 调用保存
        boolean updateResult = appService.updateById(updateApp);
        ThrowUtils.throwIf(!updateResult, ErrorCode.SYSTEM_ERROR, "应用部署信息更新失败，请稍后重试");
        // 10.返回访问的url
        // 注意这里一定要带上/ 不然无法触发重定向
        String appDeployUrl = String.format("%s/%s/", deployHost, deployKey);
        // 11.异步生成封面
        appService.generateAppScreenshotAsync(appId, appDeployUrl);
        return appDeployUrl;
    }
}
