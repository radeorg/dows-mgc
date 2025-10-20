package org.dows.mgc.sql.core.enums;

import lombok.Getter;

@Getter
public enum EditType {

    TEXT("TEXT"),
    TEXTAREA("TEXTAREA"),
    RADIO("RADIO"),
    PASSWORD("PASSWORD"),
    NUMBER("NUMBER"),
    CHECKBOX("CHECKBOX"),
    SELECT("SELECT"),
    DATE("DATE"),
    DATETIME("DATETIME");

    String value;

    EditType(String value) {
        this.value = value;
    }
}
