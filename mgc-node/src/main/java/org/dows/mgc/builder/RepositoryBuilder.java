package org.dows.mgc.builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RepositoryBuilder implements AttributeBuilder{
    @Override
    public void buildAttribute(String appId, MindNode node) {
        log.info("RepositoryBuilder buildAttribute appId: {}, node: {}", appId, node);
    }
}
