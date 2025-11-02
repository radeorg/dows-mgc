package org.dows.mgc.git;

import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

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


    public  String getWorkDir() {
        return repositoryProperties.getWorkDir();
    }

/*    public String getRepository() {
        return repositoryProperties.getName();
    }

    public String getBranch() {
        return repositoryProperties.getBranch();
    }*/

    @Bean
    public GitLabApi gitlabClient() {
        RepositoryProperties.GitlabSetting gitlabSetting = repositoryProperties.getGitlab();
        /**
         * 账号密码方式
         * GitLabApi gitLabApi = GitLabApi.oauth2Login(gitlabSetting.getHost(),
         *                 gitlabSetting.getUsername(),
         *                 gitlabSetting.getPassword());
         * access-token方式
         * GitLabApi gitLabApi = new GitLabApi(gitlabSetting.getHost(), gitlabSetting.getToken());
         */
        return new GitLabApi(gitlabSetting.getHost(), gitlabSetting.getToken());
    }


    @Bean
    public GitHub githubClient() {
        try {
            /**
             * github = new GitHubBuilder()
             * .withOAuthToken("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")
             * .build();
             */
            RepositoryProperties.GithubSetting github1 = repositoryProperties.getGithub();
            GitHub github = new GitHubBuilder().withOAuthToken(github1.getToken()).build();
            return github;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
