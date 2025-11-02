package org.dows.mgc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CicdScriptParam {
    private String moduleUrl = "http://192.168.23.19/shdy/shdy-demo";
    private String moduleName = "模块名称";
    private String moduleCode = "code";
    private String moduleBranch = "main";

    private String applicationRoot = "/shdy/saas/wes";
    private String applicationName = "wes";
    private String applicationCode = "wes-application";
    private int applicationPort = 8080;
    private String applicationBranch = "main";

    private String sonarqubeProject = "shdy-project";

    private String sonarqubeKey = "shdy_shdy-rbac_AY5ljhOca69qkk3-ccIM";

    private String sonarqubeToken = "sqp_733d7feda5ce9a6ea0b9bdfb400723ca817778eb";
    /**
     * sonar project 密钥
     */
    private String sonarProjectKey = "shdy_shdy-rbac_AY5ljhOca69qkk3-ccIM";
    /**
     * sonar project 名称
     */
    private String sonarProjectName = "shdy-project";
    /**
     * sonar project 版本
     */
    private String sonarProjectVersion = "1.0.240218.001";

    /**
     * 飞书通知地址
     */
    private String webhookFeishu = "https://open.feishu.cn/open-apis/bot/v2/hook/1c9f1a4d-4334-4693-9a3f-4f6d340f3324";
    /**
     * 微信通知地址
     */
    private String webhookWeixin = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=69655512-999d-417d-9453-4f6d340f3324";
    /**
     * 钉钉通知地址
     */
    private String webhookDingtalk = "https://oapi.dingtalk.com/robot/send?access_token=69655512-999d-417d-9453-4f6d340f3324";

    @JsonIgnore
    private Boolean deploy = true;
}
