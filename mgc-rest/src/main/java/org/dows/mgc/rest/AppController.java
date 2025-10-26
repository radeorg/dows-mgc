package org.dows.mgc.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dows.mgc.GenerationSessionRegistry;
import org.dows.mgc.annotation.AuthCheck;
import org.dows.mgc.common.BaseResponse;
import org.dows.mgc.common.DeleteRequest;
import org.dows.mgc.common.ResultUtils;
import org.dows.mgc.constant.AppConstant;
import org.dows.mgc.constant.UserConstant;
import org.dows.mgc.context.UserContextHolder;
import org.dows.mgc.deploy.AppDeployHandler;
import org.dows.mgc.entity.AppEntity;
import org.dows.mgc.exception.BusinessException;
import org.dows.mgc.exception.ErrorCode;
import org.dows.mgc.exception.ThrowUtils;
import org.dows.mgc.model.dto.app.*;
import org.dows.mgc.model.vo.AppVO;
import org.dows.mgc.ratelimit.annotation.RateLimit;
import org.dows.mgc.ratelimit.enums.RateLimitType;
import org.dows.mgc.service.AppService;
import org.dows.mgc.service.GenerationStatusService;
import org.dows.mgc.service.ProjectDownloadService;
import org.dows.mgc.uim.User;
import org.dows.mgc.uim.UserAdaptor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 应用 控制层。
 *
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    @Resource
    private AppDeployHandler appDeployHandler;

    @Resource
    private UserAdaptor userService;

    @Resource
    private ProjectDownloadService projectDownloadService;

    @Resource
    private GenerationStatusService generationStatusService;

    /**
     * 应用聊天生成代码（流式 SSE）
     *
     * @param appId   应用 ID
     * @param message 用户消息
     * @param request 请求对象
     * @return 生成结果流
     */
    @GetMapping(value = "/chat/gen/code", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @RateLimit(limitType = RateLimitType.USER, rate = 100, vipRate = 200, rateInterval = 60, enableVipDifferentiation = true, message = "AI对话请求过于频繁，请稍后再试。升级VIP可享有更高频率限制", vipMessage = "VIP用户AI对话请求过于频繁，请稍后再试")
    public Flux<ServerSentEvent<String>> chatToGenCode(@RequestParam Long appId,
                                                       @RequestParam String message,
                                                       HttpServletRequest request) {
        // 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID无效");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "用户消息不能为空");
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 设置用户上下文，供后续异步回调使用
        UserContextHolder.set(loginUser);
        // 调用服务生成代码（流式）
        Flux<String> stringFlux = appDeployHandler.chatToGenCode(appId, message, loginUser)
                .doFinally(sig -> UserContextHolder.clear());
        // 对这个流式结果进行一层封装，防止空格的丢失
        Flux<ServerSentEvent<String>> dataFlux = stringFlux.map(chunk -> {
            Map<String, String> resultMap = Map.of("d", chunk);
            String jsonData = JSONUtil.toJsonStr(resultMap);
            return ServerSentEvent.<String>builder()
                    .data(jsonData)
                    .build();
        });
        // 在正常完成时发送一个 done 事件，方便前端统一处理（之前只有异常才有 done）
        ServerSentEvent<String> doneEvent = ServerSentEvent.<String>builder()
                .event("done")
                .data("{}")
                .build();
        return dataFlux.concatWith(Mono.just(doneEvent));
    }

    /**
     * 主动取消当前生成
     */
    @PostMapping("/gen/cancel")
    public BaseResponse<?> cancelGen(@RequestParam Long appId) {
        if (appId == null || appId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "appId 无效");
        }
        String sessionId = GenerationSessionRegistry.getSession(appId);
        if (sessionId == null) {
            return ResultUtils.success("no_active_generation");
        }
        GenerationSessionRegistry.cancel(appId);
        generationStatusService.markStopped(appId, sessionId, "user_stopped");
        return ResultUtils.success("cancelled");
    }

    /**
     * 获取当前代码生成 / 构建状态
     */
    @GetMapping("/gen/status")
    public BaseResponse<?> getGenStatus(@RequestParam Long appId) {
        if (appId == null || appId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "appId 无效");
        }
        var status = generationStatusService.getStatus(appId);
        if (status == null) {
            return ResultUtils.success(Map.of("status", "none"));
        }
        return ResultUtils.success(status);
    }

    /**
     * 创建应用
     *
     * @param appAddRequest 应用创建请求
     * @param request       请求对象
     * @return 新应用的 ID
     */
    @PostMapping("/add")
    @RateLimit(limitType = RateLimitType.USER, rate = 50, // 普通用户每小时5次
            vipRate = 100, // VIP用户每小时20次
            rateInterval = 3600, enableVipDifferentiation = true, message = "创建应用过于频繁，普通用户每小时最多创建5个应用", vipMessage = "创建应用过于频繁，VIP用户每小时最多创建20个应用")
    public BaseResponse<Long> addApp(@RequestBody AppAddRequest appAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        Long appId = appService.createApp(appAddRequest, loginUser);
        return ResultUtils.success(appId);
    }

    /**
     * 更新应用（用户只能更新自己的应用名称）
     *
     * @param appUpdateRequest 更新请求
     * @param request          请求
     * @return 更新结果
     */
    @PostMapping("/update")
    @RateLimit(limitType = RateLimitType.USER, rate = 20, rateInterval = 3600, message = "应用更新过于频繁，每小时最多更新20次")
    public BaseResponse<Boolean> updateApp(@RequestBody AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        if (appUpdateRequest == null || appUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long id = appUpdateRequest.getId();
        // 判断是否存在
        AppEntity oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人可更新
        if (!oldApp.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        AppEntity app = new AppEntity();
        app.setId(id);
        app.setAppName(appUpdateRequest.getAppName());
        // 设置编辑时间
        app.setEditTime(LocalDateTime.now());
        boolean result = appService.updateById(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除应用（用户只能删除自己的应用）
     *
     * @param deleteRequest 删除请求
     * @param request       请求
     * @return 删除结果
     */
    @PostMapping("/delete")
    @RateLimit(limitType = RateLimitType.USER, rate = 5, rateInterval = 3600, message = "应用删除过于频繁，每小时最多删除5个应用")
    public BaseResponse<Boolean> deleteApp(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        AppEntity oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldApp.getUserId().equals(loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = appService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取应用详情
     *
     * @param id 应用 id
     * @return 应用详情
     */
    @GetMapping("/get/vo")
    public BaseResponse<AppVO> getAppVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        AppEntity app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类（包含用户信息）
        return ResultUtils.success(appService.getAppVO(app));
    }

    /**
     * 分页获取当前用户创建的应用列表
     *
     * @param appQueryRequest 查询请求
     * @param request         请求
     * @return 应用列表
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<AppVO>> listMyAppVOByPage(@RequestBody AppQueryRequest appQueryRequest,
                                                       HttpServletRequest request) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        // 限制每页最多 20 个
        long pageSize = appQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR, "每页最多查询 20 个应用");
        long pageNum = appQueryRequest.getPageNum();
        // 只查询当前用户的应用
        appQueryRequest.setUserId(loginUser.getId());
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        Page<AppEntity> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);
        // 数据封装
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ResultUtils.success(appVOPage);
    }

    /**
     * 分页获取精选应用列表
     *
     * @param appQueryRequest 查询请求
     * @return 精选应用列表
     */
    @PostMapping("/good/list/page/vo")
    @Cacheable(value = "good_app_page", key = "T(org.dows.mgc.utils.CacheKeyUtil).generateKey(#appQueryRequest)", condition = "#appQueryRequest.pageNum <= 10")
    // 仅缓存前
    // 10
    // 页
    public BaseResponse<Page<AppVO>> listGoodAppVOByPage(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 限制每页最多 20 个
        long pageSize = appQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR, "每页最多查询 20 个应用");
        long pageNum = appQueryRequest.getPageNum();
        // 只查询精选的应用
        appQueryRequest.setPriority(AppConstant.GOOD_APP_PRIORITY);
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        // 分页查询
        Page<AppEntity> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);
        // 数据封装
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ResultUtils.success(appVOPage);
    }

    /**
     * 管理员删除应用
     *
     * @param deleteRequest 删除请求
     * @return 删除结果
     */
    @PostMapping("/admin/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteAppByAdmin(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        AppEntity oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = appService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 管理员更新应用
     *
     * @param appAdminUpdateRequest 更新���求
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateAppByAdmin(@RequestBody AppAdminUpdateRequest appAdminUpdateRequest) {
        if (appAdminUpdateRequest == null || appAdminUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = appAdminUpdateRequest.getId();
        // 判断是否存在
        AppEntity oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        AppEntity app = new AppEntity();
        BeanUtil.copyProperties(appAdminUpdateRequest, app);
        // 设置编辑时间
        app.setEditTime(LocalDateTime.now());
        boolean result = appService.updateById(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 管理员分页获取应用列表
     *
     * @param appQueryRequest 查询请��
     * @return 应用列表
     */
    @PostMapping("/admin/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<AppVO>> listAppVOByPageByAdmin(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = appQueryRequest.getPageNum();
        long pageSize = appQueryRequest.getPageSize();
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        Page<AppEntity> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);
        // 数据封装
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ResultUtils.success(appVOPage);
    }

    /**
     * 管理员根据 id 获取应用详情
     *
     * @param id 应用 id
     * @return 应用详情
     */
    @GetMapping("/admin/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<AppVO> getAppVOByIdByAdmin(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        AppEntity app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(appService.getAppVO(app));
    }

    /**
     * 下载应用代码
     *
     * @param appId    应用ID
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/download/{appId}")
    @RateLimit(limitType = RateLimitType.USER, rate = 3, vipRate = 10, rateInterval = 3600, enableVipDifferentiation = true, message = "代码下载过于频繁，每小时最多下载3次。升级VIP可享有更高限额", vipMessage = "VIP用户代码下载过于频繁，每小时最多下载10次")
    public void downloadAppCode(@PathVariable Long appId,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        // 1. 基础校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID无效");
        // 2. 查询应用信息
        AppEntity app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3. 权限校验：应用创建者可以下载自己的应用，VIP用户可以下载精选应用
        User loginUser = userService.getLoginUser(request);
        boolean isAppOwner = app.getUserId().equals(loginUser.getId());
        boolean isVipUser = userService.isVip(loginUser);
        boolean isGoodApp = AppConstant.GOOD_APP_PRIORITY.equals(app.getPriority());

        // 权限判断：应用创建者可以下载自己的应用，VIP用户可以下载精选应用
        if (!isAppOwner && !(isVipUser && isGoodApp)) {
            if (!isVipUser && isGoodApp) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "精选应用仅VIP用户可下载，请先升级VIP");
            } else {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限下载该应用代码");
            }
        }
        // 4. 构建应用代码目录路径（生成目录，非部署目录）
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;
        // 5. 检查代码目录是否存在
        File sourceDir = new File(sourceDirPath);
        ThrowUtils.throwIf(!sourceDir.exists() || !sourceDir.isDirectory(),
                ErrorCode.NOT_FOUND_ERROR, "应用代码不存在，请先生成代码");
        // 6. 生成下载文件名（不建议添加中文内容）
        String downloadFileName = String.valueOf(appId);
        // 7. 调用通用下载服务
        projectDownloadService.downloadProjectAsZip(sourceDirPath, downloadFileName, response);
    }

    /**
     * 部署应用
     *
     * @param appDeployRequest 部署请求
     * @param request          HTTP请求
     * @return 部署后的访问URL
     */
    @PostMapping("/deploy")
    @RateLimit(limitType = RateLimitType.USER, rate = 5, vipRate = 20, rateInterval = 3600, enableVipDifferentiation = true, message = "应用部署过于频繁，每小时最多部署5次。升级VIP可享有更高限额", vipMessage = "VIP用户应用部署过于频繁，每小时最多部署20次")
    public BaseResponse<String> deployApp(@RequestBody AppDeployRequest appDeployRequest, HttpServletRequest request) {
        // 1. 参数校验
        ThrowUtils.throwIf(appDeployRequest == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        Long appId = appDeployRequest.getAppId();
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID无效");

        // 2. 获取登录用户
        User loginUser = userService.getLoginUser(request);

        // 3. 调用服务层部署方法
        String deployUrl = appDeployHandler.deployApp(appId, loginUser);

        return ResultUtils.success(deployUrl);
    }

}
