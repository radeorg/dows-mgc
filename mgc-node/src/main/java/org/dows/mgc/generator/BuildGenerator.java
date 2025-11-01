package org.dows.mgc.generator;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.BuildAttribute;
import org.dows.mgc.entity.MindNode;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BuildGenerator implements ObjectGenerator {
    @Override
    public void generate(String appId, MindNode mindNode) {
        log.info("build node: {}", JSONUtil.toJsonStr(mindNode));
        BuildAttribute attribute = mindNode.getNodeAttribute().toAttribute(BuildAttribute.class);

    }
}
