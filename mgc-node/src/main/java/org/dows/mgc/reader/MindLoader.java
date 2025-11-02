package org.dows.mgc.reader;

import org.dows.mgc.entity.MindNode;

import java.util.List;
import java.util.Map;

public interface MindLoader {

    List<MindNode> loadProjectMind(String projectUri);

    Map<String, List<MindNode>> loadMindProjects(String... projectUri);

}
