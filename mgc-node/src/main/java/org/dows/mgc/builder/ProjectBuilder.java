package org.dows.mgc.builder;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.ProjectAttribute;
import org.dows.mgc.reader.MindReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProjectBuilder implements AttributeBuilder {

    private final MindReader mindReader;


    /**
     * project/d:fff.f:github.radeorg.dows-eaglee/鹰眼
     *
     * @param appId
     * @param node
     */
    public void buildAttribute(String appId, MindNode node) {
        log.info("project node: {}", JSONUtil.toJsonStr(node));
        List<MindNode> nodes = mindReader.readProjectMind(appId);
        ProjectAttribute attribute = new ProjectAttribute();
        String value = node.getValue();
        int i = value.lastIndexOf(":");
        String localFolder = value.substring(0, i);
        localFolder = localFolder.replace(".", File.separator);
        String repository = value.substring(i + 1).replace(".", "/");
        String[] split = repository.split("/");
        if (split[0].equals("github")) {
            String repositoryHost = split[0] + ".com";
            repository = repository.replace(split[0], repositoryHost) + ".git";
        }

        attribute.setFolderPath(localFolder);
        attribute.setRepository(repository);
        node.setNodeAttribute(attribute);


    }


}
