package org.dows.mgc.entity;

import lombok.Data;

@Data
public class ProjectAttribute implements NodeAttribute {

    private String folderPath;
    // 仓库地址
    private String repository;

}
