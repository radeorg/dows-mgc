package org.dows.mgc.sql.core.enums;

import lombok.Getter;

@Getter
public enum ColumnType {
    VARCHAR("VARCHAR"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    NUMBER("NUMBER"),
    TEXT("TEXT"),
    BLOB("BLOB");

    String value;

    ColumnType(String value) {
        this.value = value;
    }
}
