package org.dows.mgc.entity;

import lombok.Data;

@Data
public class ObjectAttribute implements NodeAttribute {

    private String pkg;
    private String className;

}
