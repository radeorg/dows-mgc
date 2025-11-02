package org.dows.mgc;

import lombok.RequiredArgsConstructor;
import org.dows.mgc.builder.AttributeBuilder;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;
import org.dows.mgc.generator.ObjectGenerator;
import org.dows.mgc.reader.MindReader;
import org.dows.mgc.reader.MindReader1;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MindService {

    private final MindReader mindReader;

    private final Map<String, AttributeBuilder> attributeBuilders;

    private final Map<String, ObjectGenerator> fileGenerators;


    public void init(String appId) {
        List<MindNode> nodes = mindReader.getMindNodeList(appId);
        for (MindNode node : nodes) {
            AttributeBuilder attributeBuilder = attributeBuilders.get(node.getNodeType().getBuilder());
            if (attributeBuilder != null) {
                attributeBuilder.buildAttribute(appId,node);
            }
        }
    }

    public void generate(String appId) {
        Map<NodeType, List<MindNode>> collect = mindReader.getNodeTypeMap(appId);
        collect.forEach((nodeType, mindNodes) -> {
            ObjectGenerator objectGenerator = fileGenerators.get(nodeType.getBuilderType().getGenerator());
            if (objectGenerator != null) {
                for (MindNode mindNode : mindNodes) {
                    objectGenerator.generate(appId,mindNode);
                }
            }
        });
    }


}
