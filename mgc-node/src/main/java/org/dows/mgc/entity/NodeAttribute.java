package org.dows.mgc.entity;

public interface NodeAttribute {
    default <T extends NodeAttribute> T toAttribute(Class<T> tClass) {
        return tClass.cast(this);
    }
}
