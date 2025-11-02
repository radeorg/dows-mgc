package org.dows.mgc.entity;

import lombok.Getter;

@Getter
public enum NodeType {

    repository(BuilderType.repositoryBuilder),
    project(BuilderType.projectBuilder),
    build(BuilderType.nullBuilder),
    pkg(BuilderType.nullBuilder),
    rest(BuilderType.restBuilder),
    post(BuilderType.nullBuilder),
    get(BuilderType.nullBuilder),
    put(BuilderType.nullBuilder),
    delete(BuilderType.nullBuilder),
    in(BuilderType.nullBuilder),
    out(BuilderType.nullBuilder),

    database(BuilderType.nullBuilder),
    table(BuilderType.nullBuilder),
    varchar(BuilderType.nullBuilder),
    integer(BuilderType.nullBuilder),
    bool(BuilderType.nullBuilder),
    bigint(BuilderType.nullBuilder),
    decimal(BuilderType.nullBuilder),
    datetime(BuilderType.nullBuilder),
    object(BuilderType.nullBuilder),

    api(BuilderType.nullBuilder),
    feign(BuilderType.nullBuilder),
    app(BuilderType.nullBuilder),
    boot(BuilderType.nullBuilder),
    core(BuilderType.nullBuilder),
    controller(BuilderType.nullBuilder),
    service(BuilderType.nullBuilder),
    handler(BuilderType.nullBuilder),
    dao(BuilderType.nullBuilder),
    ;
    private final String builder;

    private final BuilderType builderType;

    NodeType(BuilderType builderType) {
        this.builderType = builderType;
        this.builder = builderType.name();
    }
}
