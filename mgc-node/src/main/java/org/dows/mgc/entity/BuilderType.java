package org.dows.mgc.entity;

import lombok.Getter;

public enum BuilderType {

    nullBuilder(""),
    restBuilder("restGenerator"),

    projectBuilder("projectGenerator")


    ;

    @Getter
    private final String generator;

    BuilderType(String generator) {
        this.generator = generator;
    }
}
