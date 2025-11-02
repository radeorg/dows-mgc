package org.dows.mgc.entity;

import lombok.Getter;

public enum BuilderType {

    nullBuilder(""),
    databaseBuilder("databaseGenerator"),

    repositoryBuilder("repositoryGenerator"),

    projectBuilder("projectGenerator"),
    restBuilder("restGenerator");


    @Getter
    private final String generator;

    BuilderType(String generator) {
        this.generator = generator;
    }
}
