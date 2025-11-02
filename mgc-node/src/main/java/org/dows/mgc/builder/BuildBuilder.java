package org.dows.mgc.builder;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


        node.setNodeAttribute(attribute);
    }
}
