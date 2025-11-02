package org.dows.mgc.entity;

import lombok.Getter;

public enum RepositoryType {
    github("githubClient"),
    gitlab("gitlabClient"),
    gitee("giteeClient"),
    ;

    @Getter
    private final String repositoryClient;

    RepositoryType(String github) {
        this.repositoryClient = github;
    }
}
