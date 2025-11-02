package org.dows.mgc.generator;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseGenerator implements ObjectGenerator {
    @Override
    public void generate(String appId, MindNode mindNode) {
        log.info("generate database: {}", JSONUtil.toJsonStr(mindNode));
    }
}
