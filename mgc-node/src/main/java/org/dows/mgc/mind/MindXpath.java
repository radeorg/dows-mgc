package org.dows.mgc.mind;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MindXpath {
    private String mindFileName;
    private List<String> databaseXpath;
    private List<String> apiXpath;
}