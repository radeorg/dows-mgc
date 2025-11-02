package org.dows.mgc.generator;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.JavaAttribute;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.reader.MindReader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestGenerator implements ObjectGenerator {

    private final MindReader mindReader;

    @Override
    public void generate(String appId, MindNode mindNode) {
        log.info("rest node: {}", JSONUtil.toJsonStr(mindNode));
        Map<String, MindNode> nodeIdMap = mindReader.readProjectNodeIdMap(appId);

        JavaAttribute javaAttribute = mindNode.getNodeAttribute().toAttribute(JavaAttribute.class);
        String filePath = javaAttribute.getFilePath();

        List<String> children = mindNode.getChildren();
        for (String child : children) {
            MindNode methodNode = nodeIdMap.get(child);
            if (methodNode != null) {
                log.info("method node: {}", JSONUtil.toJsonStr(methodNode));
                List<String> params = methodNode.getChildren();

            }
        }


    }
}
