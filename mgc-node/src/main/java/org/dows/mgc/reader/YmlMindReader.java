package org.dows.mgc.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class YmlMindReader implements MindReader {

    @Override
    public Map<String, List<MindNode>> loadMindProjects(String ... projectUri) {
        return Map.of();
    }

    @Override
    public List<MindNode> loadProjectMind(String projectUri) {
        return List.of();
    }
}
