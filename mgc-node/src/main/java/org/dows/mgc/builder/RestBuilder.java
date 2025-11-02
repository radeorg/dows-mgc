package org.dows.mgc.builder;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.JavaAttribute;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.reader.MindReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestBuilder implements AttributeBuilder {

    private final MindReader mindReader;


    public void buildAttribute(String appId, MindNode node) {
        log.info("rest node: {}", JSONUtil.toJsonStr(node));
        List<MindNode> nodes = mindReader.readProjectMind(appId);
        JavaAttribute nodeAttribute = new JavaAttribute();
        MindNode mindNode = nodes.stream()
                .filter(n -> node.getParentId().equals(n.getNodeId())).findFirst()
                .orElse(null);
        if (mindNode != null) {
            String pkg = mindNode.getValue();
            nodeAttribute.setPkg(pkg);

            String value = node.getValue();
            // 需要暴露
            if (value.endsWith(":")) {
                value = StrUtil.upperFirst(value.substring(0, value.length() - 1));
                // todo 记录暴露的类名
            }
            nodeAttribute.setFileName(value + ".java");
            nodeAttribute.setFilePath(pkg.replaceAll("\\.", File.separator) + File.separator + nodeAttribute.getFileName());
            nodeAttribute.setClassName(value);
            node.setNodeAttribute(nodeAttribute);
        }

    }


}
