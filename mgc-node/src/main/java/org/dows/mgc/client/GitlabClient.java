package org.dows.mgc.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.git.RepositoryProperties;
import org.gitlab4j.api.GitLabApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GitlabClient implements RepositoryClient{

    /*public GitLabApi gitlabClient() {
        RepositoryProperties.GitlabSetting gitlabSetting = repositoryProperties.getGitlab();
        *//**
         * 账号密码方式
         * GitLabApi gitLabApi = GitLabApi.oauth2Login(gitlabSetting.getHost(),
         *                 gitlabSetting.getUsername(),
         *                 gitlabSetting.getPassword());
         * access-token方式
         * GitLabApi gitLabApi = new GitLabApi(gitlabSetting.getHost(), gitlabSetting.getToken());
         *//*
        return new GitLabApi(gitlabSetting.getHost(), gitlabSetting.getToken());
    }*/

}
