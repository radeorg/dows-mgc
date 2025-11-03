package org.dows.mgc.entity;

import lombok.Data;

@Data
public class RepositoryAttribute implements NodeAttribute {
    private String folderPath;
    // 仓库地址
    private String repository;

    private String httpRepositoryUri;
    private String gitRepositoryUri;

    private String branch;

    private String token;

    private String description;

    private RepositoryType repositoryType;
}
