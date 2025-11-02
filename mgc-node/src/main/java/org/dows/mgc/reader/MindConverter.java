package org.dows.mgc.reader;

import org.dows.mgc.entity.MindNode;

import java.util.List;

public interface MindConverter {

    List<MindNode> convertToMindNodes(Object object);
}
