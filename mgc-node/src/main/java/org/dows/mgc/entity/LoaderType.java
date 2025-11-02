package org.dows.mgc.entity;

import lombok.Getter;

public enum LoaderType {
    txt("txtMindLoader"),
    json("jsonMindLoader"),
    xml("xmlMindLoader"),
    gmind("gitMindLoader"),
    ;

    @Getter
    private final String loader;

    LoaderType(String loader) {
        this.loader = loader;
    }

}
