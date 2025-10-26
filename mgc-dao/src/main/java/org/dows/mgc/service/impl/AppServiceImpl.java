package org.dows.mgc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.ai.AiCodeGenTypeRoutingService;
import org.dows.mgc.ai.AiCodeGenTypeRoutingServiceFactory;
import org.dows.mgc.entity.AppEntity;
import org.dows.mgc.entity.UserEntity;
import org.dows.mgc.exception.BusinessException;
import org.dows.mgc.exception.ErrorCode;
import org.dows.mgc.exception.ThrowUtils;
import org.dows.mgc.mapper.AppMapper;
import org.dows.mgc.model.dto.app.AppAddRequest;
import org.dows.mgc.model.dto.app.AppQueryRequest;
import org.dows.mgc.model.enums.CodeGenTypeEnum;
import org.dows.mgc.model.vo.AppVO;
import org.dows.mgc.model.vo.UserPublicVO;
import org.dows.mgc.service.*;
import org.dows.mgc.uim.User;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 */
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, AppEntity> implements AppService {

    @Resource
    private UserService userService;
    @Resource
    private ChatHistoryService chatHistoryService;
    @Resource
    private ScreenshotService screenshotService;
    @Resource
    private AiCodeGenTypeRoutingServiceFactory aiCodeGenTypeRoutingServiceFactory;



    @Override
    public AppVO getAppVO(AppEntity app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        // 关联查询用户信息
        Long userId = app.getUserId();
        if (userId != null) {
            UserEntity user = userService.getById(userId);
            UserPublicVO userPublicVO = userService.getUserPublicVO(user);
            appVO.setUser(userPublicVO);
        }
        return appVO;
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        Boolean isVipOnly = appQueryRequest.getIsVipOnly();
        // 使用正确的 MyBatis-Flex QueryWrapper 语法
        // MyBatis-Flex 会自动忽略 null 值，无需手动判断
        return QueryWrapper.create()
                .eq("id", id)
                .like("appName", appName)
                .like("cover", cover)
                .like("initPrompt", initPrompt)
                .eq("codeGenType", codeGenType)
                .eq("deployKey", deployKey)
                .eq("priority", priority)
                .eq("userId", userId)
                .eq("isVipOnly", isVipOnly)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    @Override
    public List<AppVO> getAppVOList(List<AppEntity> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }
        // 批量获取用户信息，避免 N+1 查询问题
        Set<Long> userIds = appList.stream()
                .map(AppEntity::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserPublicVO> UserPublicVOMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(UserEntity::getId, userService::getUserPublicVO));
        return appList.stream().map(app -> {
            AppVO appVO = getAppVO(app);
            UserPublicVO userPublicVO = UserPublicVOMap.get(app.getUserId());
            appVO.setUser(userPublicVO);
            return appVO;
        }).collect(Collectors.toList());
    }



    @Override
    public Long createApp(AppAddRequest appAddRequest, User loginUser) {
        // 参数校验
        String initPrompt = appAddRequest.getInitPrompt();
        ThrowUtils.throwIf(StrUtil.isBlank(initPrompt), ErrorCode.PARAMS_ERROR, "初始化 prompt 不能为空");
        // 构造入库对象
        AppEntity app = new AppEntity();
        BeanUtil.copyProperties(appAddRequest, app);
        app.setUserId(loginUser.getId());
        // 应用名称暂时为 initPrompt 前 12 位
        app.setAppName(initPrompt.substring(0, Math.min(initPrompt.length(), 12)));
        // 使用 AI 智能选择代码生成类型
        // 多例模式来获取新的 AiCodeGenTypeRoutingService 实例 （每次创建新的aiservice）
        AiCodeGenTypeRoutingService aiCodeGenTypeRoutingService = aiCodeGenTypeRoutingServiceFactory
                .createAiCodeGenTypeRoutingService();
        CodeGenTypeEnum selectedCodeGenType = aiCodeGenTypeRoutingService.getCodeGenType(initPrompt);
        app.setCodeGenType(selectedCodeGenType.getValue());
        // 插入数据库
        boolean result = this.save(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        log.info("应用创建成功，ID: {}, 类型: {}", app.getId(), selectedCodeGenType.getValue());
        return app.getId();
    }



    /**
     * 异步生成应用封面截图
     *
     * @param appId
     * @param appDeployUrl
     */
    @Override
    public void generateAppScreenshotAsync(Long appId, String appDeployUrl) {
        // 异步生成应用封面截图
        Thread.startVirtualThread(() -> {
            try {
                // 生成应用封面截图
                String screenshotUrl = screenshotService.generateAndUploadScreenshot(appDeployUrl);
                if (StrUtil.isNotBlank(screenshotUrl)) {
                    // 更新应用的封面
                    AppEntity updateApp = new AppEntity();
                    updateApp.setId(appId);
                    updateApp.setCover(screenshotUrl);
                    this.updateById(updateApp);
                    log.info("应用 {} 的封面截图已生成并更新: {}", appId, screenshotUrl);
                } else {
                    log.warn("应用 {} 的封面截图生成失败", appId);
                }
            } catch (Exception e) {
                log.error("生成应用 {} 封面截图异常: {}", appId, e.getMessage(), e);
            }
        });
    }

    /**
     * 删除应用时，关联删除对话历史（此处不需要事务）
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        // 1.空值判断
        ThrowUtils.throwIf(id == null || !(id instanceof Long) || (Long) id <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        long appId = Long.parseLong(id.toString());
        if (appId <= 0) {
            return false;
        }
        // 2.删除关联的对话历史
        // 这里不需要事务，因为删除对话历史失败不会影响应用删除
        try {
            chatHistoryService.removeById(appId);
        } catch (Exception e) {
            log.error("删除应用关联的对话历史失败: {}", e.getMessage());
        }
        // 3.删除应用
        return super.removeById(id);
    }

}
