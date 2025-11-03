package org.dows.mgc.builder;

import org.dows.mgc.entity.MindNode;

public interface AttributeBuilder {
    /**
     * 构建属性的顺序
     *
     * @return
     */
    Integer getOrder();

    /**
     * 构建属性
     *
     * @param appId
     * @param node
     */
    void buildAttribute(String appId, MindNode node);
}
