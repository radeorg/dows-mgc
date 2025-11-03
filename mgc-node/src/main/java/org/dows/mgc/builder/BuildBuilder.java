package org.dows.mgc.builder;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.context.AppMindCache;
import org.dows.mgc.context.ProjectContext;
import org.dows.mgc.entity.BuildAttribute;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.reader.MindReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BuildBuilder implements AttributeBuilder {

    private final MindReader mindReader;
    private final AppMindCache appMindCache;
    @Override
    public Integer getOrder() {
        return 0;
    }

    @Override
    public void buildAttribute(String appId, MindNode node) {
        log.info("build node: {}", JSONUtil.toJsonStr(node));

        List<MindNode> nodes = mindReader.readProjectMind(appId);
        BuildAttribute attribute = new BuildAttribute();
        String value = node.getValue();
        String[] split = value.split(":");
        attribute.setArtifactId(split[0]);
        attribute.setGroupId(split[1]);
        attribute.setVersion(split[2]);

        ProjectContext projectContext = ProjectContext.getProjectContext(appId);


        node.setNodeAttribute(attribute);
    }
}
