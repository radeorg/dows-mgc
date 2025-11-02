package org.dows.mgc.reader;

import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MindReader extends MindLoader {

    default Map<NodeType, List<MindNode>> getNodeTypeMap(String projectUri) {
        Map<String, List<MindNode>> projectMindNodes = loadMindProjects(projectUri);
        List<MindNode> mindNodes = projectMindNodes.get(projectUri);
        return mindNodes.stream().collect(Collectors.groupingBy(MindNode::getNodeType));
    }

    default Map<String, MindNode> getNodeIdMap(String projectUri) {
        Map<String, List<MindNode>> projectMindNodes = loadMindProjects(projectUri);
        List<MindNode> mindNodes = projectMindNodes.get(projectUri);
        return mindNodes.stream().collect(Collectors.toMap(MindNode::getNodeId, node -> node));
    }
}
