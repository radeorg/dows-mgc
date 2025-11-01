package org.dows.mgc.entity;

import lombok.Data;

@Data
public class BuildAttribute implements NodeAttribute {

    private String artifactId;
    private String groupId;
    private String version;
}
