package org.dows.mgc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 5/9/2024 9:32 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Schema(title = "部署设置")
@Data
public class DeploySetting {

    @Schema(title = "是否部署")
    private boolean deploy;
    @Schema(title = "部署通道[gitlab-runner,jenkins....]")
    private String channel;
    //需用bulidRequest 设置进来
    @JsonIgnore
//    @Schema(title = "环境[dev|sit|uat|prd...]")
    //环境[dev|sit|uat|prd...]
    private String env = "dev";
    //需用bulidRequest 设置进来
    @Schema(title = "部署分支")
    @JsonIgnore
    private String branch;
    @Schema(title = "部署脚本(可选)")
    private String script;
    @JsonIgnore
    private String workDir;
/*    @JsonIgnore
    private String projectFolder;*/


    @Schema(title = "脚本参数(可选)")
    private CicdScriptParam cicdScriptParam;

}

