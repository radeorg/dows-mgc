package org.dows.mgc;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.builder.AttributeBuilder;
import org.dows.mgc.context.AppMindCache;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;
import org.dows.mgc.generator.ObjectGenerator;
import org.dows.mgc.reader.MindReader;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MindService {

    private final MindReader mindReader;

    private final Map<String, AttributeBuilder> attributeBuilders;

    private final Map<String, ObjectGenerator> fileGenerators;

    private final MD5 md5 = MD5.create();

    private final AppMindCache appMindCache;

    public String init(String file) {
        List<MindNode> nodes = mindReader.readProjectMind(file);
        if (nodes == null || nodes.isEmpty()) {
            log.info("no project node found");
            throw new RuntimeException("no project node found");
        }
        // 每次创建一个项目，根据文件对应的数据结构内容md5作为项目id
        String appId = md5.digestHex(JSONUtil.toJsonStr(nodes), StandardCharsets.UTF_8);
        log.info("init project: {}, appId: {}", file, appId);
        // todo 需要存留版本信息
        appMindCache.setAppMindMap(appId, nodes);
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
        return appId;
    }

    public void generate(String appId) {
        Map<NodeType, List<MindNode>> collect = appMindCache.getProjectNodeTypeMap(appId);
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
