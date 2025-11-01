package org.dows.mgc.entity;

import lombok.Data;

@Data
public class FolderAttribute implements NodeAttribute {

    private String folderName;
    private String folderPath;


}
