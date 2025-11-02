package org.dows.mgc;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.builder.AttributeBuilder;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;
import org.dows.mgc.generator.ObjectGenerator;
import org.dows.mgc.reader.MindReader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MindService {

    private final MindReader mindReader;

    private final Map<String, AttributeBuilder> attributeBuilders;

    private final Map<String, ObjectGenerator> fileGenerators;


    public void init(String appId) {
        List<MindNode> nodes = mindReader.readProjectMind(appId);
        if (nodes == null || nodes.isEmpty()) {
            log.info("no project node found");
            return;
        }

        for (MindNode node : nodes) {
            try {
                AttributeBuilder attributeBuilder = attributeBuilders.get(node.getNodeType().getBuilder());
                if (attributeBuilder != null) {
                    attributeBuilder.buildAttribute(appId, node);
                }
            } catch (Exception e) {
                log.error("init node attribute: {}", JSONUtil.toJsonStr(node));
                throw new RuntimeException("init node attribute error", e);
            }
        }

    }

    public void generate(String appId) {
        Map<NodeType, List<MindNode>> collect = mindReader.getProjectNodeTypeMap(appId);
        collect.forEach((nodeType, mindNodes) -> {
            ObjectGenerator objectGenerator = fileGenerators.get(nodeType.getBuilderType().getGenerator());
            if (objectGenerator != null) {
                for (MindNode mindNode : mindNodes) {
                    objectGenerator.generate(appId, mindNode);
                }
            }
        });
    }


}
