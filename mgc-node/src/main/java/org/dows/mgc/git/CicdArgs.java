package org.dows.mgc.git;

import lombok.Data;

/**
 * @description: cicd 环境变量</br>
 * @author: lait.zhang@gmail.com
 * @date: 5/11/2024 10:00 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class CicdArgs {

    private String moduleUrl;
    private String moduleName;
    private String moduleCode;
    private String moduleBranch;

    private String applicationRoot;
    private String applicationName;
    private String applicationCode;
    private int applicationPort;
    private String applicationBranch;

    private String sonarqubeProject;

    private String sonarqubeKey;

    private String sonarqubeToken;
    /**
     * sonar project 密钥
     */
    private String sonarProjectKey;
    /**
     * sonar project 名称
     */
    private String sonarProjectName;
    /**
     * sonar project 版本
     */
    private String sonarProjectVersion;

    /**
     * 飞书通知地址
     */
    private String webhookFeishu;
    /**
     * 微信通知地址
     */
    private String webhookWeixin;
    /**
     * 钉钉通知地址
     */
    private String webhookDingtalk;


}

