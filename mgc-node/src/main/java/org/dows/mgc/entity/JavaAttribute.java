package org.dows.mgc.entity;

import lombok.Data;

@Data
public class JavaAttribute implements NodeAttribute {

    private String pkg;
    private String className;
    private String fileName;
    private String filePath;

}
