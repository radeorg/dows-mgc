package org.dows.mgc.entity;

import lombok.Data;

@Data
public class RepositoryAttribute implements NodeAttribute {
    private String folderPath;
    // 仓库地址
    private String repository;

    private String token;

    private RepositoryType repositoryType;
}
