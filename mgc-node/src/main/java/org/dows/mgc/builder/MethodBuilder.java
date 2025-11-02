package org.dows.mgc.builder;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.reader.MindReader1;
import org.dows.mgc.entity.MindNode;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MethodBuilder implements AttributeBuilder {

    private final MindReader1 mindReader;

    @Override
    public void buildAttribute(String appId, MindNode node) {
        log.info("method node: {}", JSONUtil.toJsonStr(node));
    }
}
