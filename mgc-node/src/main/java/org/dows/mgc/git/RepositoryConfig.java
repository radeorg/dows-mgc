package org.dows.mgc.git;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {

    private final RepositoryProperties repositoryProperties;

    public String getOwner(String channel) {
        if ("github".equals(channel)) {
            return repositoryProperties.getGithub().getOwner();
        } else if ("gitlab".equals(channel)) {
            return repositoryProperties.getGitlab().getOwner();
        }
        throw new RuntimeException("不支持的仓库类型");
    }


    public String getWorkDir() {
        return repositoryProperties.getWorkDir();
    }

/*    public String getRepository() {
        return repositoryProperties.getName();
    }

    public String getBranch() {
        return repositoryProperties.getBranch();
    }*/


}
