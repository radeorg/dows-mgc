package org.dows.mgc.generator;

import org.dows.mgc.entity.MindNode;

public interface ObjectGenerator {
    void generate(String appId, MindNode mindNode);
}
