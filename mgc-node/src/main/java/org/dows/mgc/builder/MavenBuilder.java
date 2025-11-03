package org.dows.mgc.builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.context.AppMindCache;
import org.dows.mgc.entity.MindNode;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class MavenBuilder implements AttributeBuilder {
    private final AppMindCache appMindCache;

    @Override
    public Integer getOrder() {
        return 0;
    }

    @Override
    public void buildAttribute(String appId, MindNode node) {

    }
}
