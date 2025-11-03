package org.dows.mgc.context;

import lombok.Data;
import org.dows.mgc.entity.RepositoryAttribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ProjectContext {

    private static final Map<String, ProjectContext> projectContexts = new ConcurrentHashMap<>();
    // 基础包
    private String baskPkg;
    private String appId;
    private String projectName;
    private String projectDir;
//    private String workDir;

    private RepositoryAttribute repositoryAttribute;
    public static ProjectContext getProjectContext(String projectCode) {
        ProjectContext projectContext = projectContexts.get(projectCode);
        if(projectContext == null) {
            projectContext = new ProjectContext();
            projectContext.setProjectName(projectCode);
        }
        return projectContext;
    }


}
