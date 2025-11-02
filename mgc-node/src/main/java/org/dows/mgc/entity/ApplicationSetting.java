package org.dows.mgc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 5/6/2024 11:01 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Schema(title = "应用设置[端口|环境|...等]")
@Data
public class ApplicationSetting {

    @Schema(title = "端口")
    private String port = "8080";
    @Schema(title = "context path")
    private String contextPath;
    // 环境 需用bulidRequest 设置进来
    @JsonIgnore
    private String env = "dev";
    //分支 需用bulidRequest 设置进来
    @JsonIgnore
    private String branch;

    @Schema(title = "项目接口版本")
    private String version;
    @Schema(title = "是否开启mock")
    private boolean mock = true;

    private boolean creating = true;

}

