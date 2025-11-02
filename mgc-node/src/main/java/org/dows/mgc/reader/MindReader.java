package org.dows.mgc.reader;

import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MindReader {
    Map<String, List<MindNode>> readMindNodes(String ...projectCode);

    List<MindNode> getMindNodeList(String projectCode);

    default Map<NodeType, List<MindNode>> getNodeTypeMap(String appId) {
        Map<String, List<MindNode>> projectMindNodes = readMindNodes();
        List<MindNode> mindNodes = projectMindNodes.get(appId);
        return mindNodes.stream().collect(Collectors.groupingBy(MindNode::getNodeType));
    }

    default Map<String, MindNode> getNodeIdMap(String appId) {
        Map<String, List<MindNode>> projectMindNodes = readMindNodes();
        List<MindNode> mindNodes = projectMindNodes.get(appId);
        return mindNodes.stream().collect(Collectors.toMap(MindNode::getNodeId, node -> node));
    }
}
