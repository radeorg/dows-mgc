package org.dows.mgc.git;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dows.mgc.repository")
@Data
public class RepositoryProperties {
    // 工作目录
    private String workDir;

    @NestedConfigurationProperty
    private GitlabSetting gitlab;
    @NestedConfigurationProperty
    private GithubSetting github;

    @Data
    public static class GithubSetting {
        private String host;
        private String owner;
        private String token;
    }

    @Data
    public static class GitlabSetting {
        private String host;
        private String username;
        private String password;
        private String token;
        private String owner;
    }

}
