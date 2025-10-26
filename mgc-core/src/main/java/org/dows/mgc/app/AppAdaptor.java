package org.dows.mgc.app;

import org.dows.mgc.model.dto.app.AppAddRequest;
import org.dows.mgc.uim.User;

public interface AppAdaptor {

    Long createApp(AppAddRequest appAddRequest, User loginUser);

    // String deployApp(Long appId, UserEntity loginUser);
    //  Flux<String> chatToGenCode(Long appId, String message, UserEntity loginUser);

    void generateAppScreenshotAsync(Long appId, String appDeployUrl);
}
