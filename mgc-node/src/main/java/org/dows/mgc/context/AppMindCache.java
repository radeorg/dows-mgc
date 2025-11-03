package org.dows.mgc.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Component
public class AppMindCache {

    private Map<String, List<MindNode>> appMindMap = new ConcurrentHashMap<>();

    public void setAppMindMap(String appId, List<MindNode> mindNodes) {
        appMindMap.put(appId, mindNodes);
    }

    public Map<NodeType, List<MindNode>> getProjectNodeTypeMap(String appId) {
        List<MindNode> mindNodes = appMindMap.get(appId);
        if (mindNodes == null || mindNodes.isEmpty()) {
            log.info("no project node found");
            return Map.of();
        }
        return mindNodes.stream().collect(Collectors.groupingBy(MindNode::getNodeType));
    }

    public Map<String, MindNode> getProjectNodeIdMap(String appId) {
        List<MindNode> mindNodes = appMindMap.get(appId);
        return mindNodes.stream().collect(Collectors.toMap(MindNode::getNodeId, node -> node));
    }
}
