package org.dows.mgc.generator;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.RepositoryAttribute;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RepositoryGenerator implements ObjectGenerator {
    @Override
    public void generate(String appId, MindNode mindNode) {
        log.info("repository node: {}", JSONUtil.toJsonStr(mindNode));

        RepositoryAttribute repositoryAttribute = mindNode.getNodeAttribute().toAttribute(RepositoryAttribute.class);
    }
}
