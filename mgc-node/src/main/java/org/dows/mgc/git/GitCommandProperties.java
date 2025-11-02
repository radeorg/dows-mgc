package org.dows.mgc.git;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 5/6/2024 6:56 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Component
@ConfigurationProperties(prefix = "dows.mgc.git")
@Data
public class GitCommandProperties {
    private List<String> init;
    private List<String> merge;
    private List<String> pull;
    private List<String> clone;
}

