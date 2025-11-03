package org.dows.mgc.builder;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.context.AppMindCache;
import org.dows.mgc.context.ProjectContext;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.ProjectAttribute;
import org.dows.mgc.reader.MindReader;
import org.dows.mgc.util.MindUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProjectBuilder implements AttributeBuilder {

    private final MindReader mindReader;
    private final AppMindCache appMindCache;

    @Override
    public Integer getOrder() {
        return 0;
    }
    /**
     * project/d:fff.f:github.radeorg.dows-eaglee/鹰眼
     * project/d:fff.f:dows-eaglee
     * @param appId
     * @param node
     */
    public void buildAttribute(String appId, MindNode node) {
        log.info("project node: {}", JSONUtil.toJsonStr(node));
        List<MindNode> nodes = mindReader.readProjectMind(appId);
        ProjectAttribute attribute = new ProjectAttribute();
        String value = node.getValue();





        //String projectPath = parsePath(projectNode.getValue());
        //attribute.setFolderPath(projectPath);





        int i = value.lastIndexOf(":");
        String localFolder = value.substring(0, i);
        localFolder = localFolder.replace(".", File.separator);
        String repository = value.substring(i + 1).replace(".", "/");
        String[] split = repository.split("/");
        if (split[0].equals("github")) {
            String repositoryHost = split[0] + ".com";
            repository = repository.replace(split[0], repositoryHost) + ".git";
        }
        String projectName = MindUtil.extractProjectName(value);
        ProjectContext projectContext = ProjectContext.getProjectContext(appId);
        projectContext.setAppId(appId);
        projectContext.setProjectDir(localFolder);
        projectContext.setProjectName(projectName);

        attribute.setFolderPath(localFolder);
        attribute.setRepository(repository);
        node.setNodeAttribute(attribute);


    }


}
