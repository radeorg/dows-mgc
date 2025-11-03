package org.dows.mgc.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.LoaderType;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MindReader {

    private final Map<String, MindLoader> mindLoaders;

    public List<MindNode> readProjectMind(String projectFile) {
        MindLoader mindLoader = getMindLoader(projectFile);
        return mindLoader.loadProjectMind(projectFile);
    }

    /*public Map<NodeType, List<MindNode>> getProjectNodeTypeMap(String projectUri) {
        MindLoader mindLoader = getMindLoader(projectUri);
        Map<String, List<MindNode>> projectMindNodes = mindLoader.loadMindProjects(projectUri);
        List<MindNode> mindNodes = projectMindNodes.get(projectUri);
        if (mindNodes == null || mindNodes.isEmpty()) {
            log.info("no project node found");
            return Map.of();
        }
        return mindNodes.stream().collect(Collectors.groupingBy(MindNode::getNodeType));
    }

    public Map<String, MindNode> readProjectNodeIdMap(String projectUri) {
        MindLoader mindLoader = getMindLoader(projectUri);
        List<MindNode> mindNodes = mindLoader.loadProjectMind(projectUri);
        return mindNodes.stream().collect(Collectors.toMap(MindNode::getNodeId, node -> node));
    }*/



    /**
     * http://ddd.txt
     * http://ddd.json
     * http://ddd.xml
     * file://ddd.txt
     *
     * @param projectUri
     * @return
     */
    private MindLoader getMindLoader(String projectUri) {
        String loaderType = projectUri.substring(projectUri.lastIndexOf(".") + 1);
        MindLoader mindLoader = mindLoaders.get(LoaderType.valueOf(loaderType).getLoader());
        if (mindLoader == null) {
            throw new IllegalArgumentException("loaderType not found");
        }
        return mindLoader;
    }
}
