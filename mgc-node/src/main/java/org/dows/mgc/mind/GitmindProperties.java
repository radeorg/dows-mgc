package org.dows.mgc.mind;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 4/29/2024 5:53 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
@Component
@ConfigurationProperties(prefix = "dows.mgc.gitmind")
public class GitmindProperties {

    private String loginUrl;
    private String mindFiles;
    private String mindFileUrl;
    private String mindUrl;
    private String mind;
}

