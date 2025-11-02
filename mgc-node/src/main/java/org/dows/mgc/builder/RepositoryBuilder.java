package org.dows.mgc.builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.RepositoryAttribute;
import org.dows.mgc.entity.RepositoryType;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RepositoryBuilder implements AttributeBuilder{
    @Override
    public void buildAttribute(String appId, MindNode node) {
        log.info("RepositoryBuilder buildAttribute appId: {}, node: {}", appId, node);

        RepositoryAttribute repositoryAttribute = new RepositoryAttribute();
        repositoryAttribute.setFolderPath("D:/git/");
        repositoryAttribute.setRepository("https://github.com/dows/mgc-node.git");
        repositoryAttribute.setRepositoryType(RepositoryType.github);
        repositoryAttribute.setToken("");
        node.setNodeAttribute(repositoryAttribute);
    }
}
