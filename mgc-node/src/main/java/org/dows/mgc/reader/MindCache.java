package org.dows.mgc.reader;

import org.dows.mgc.entity.MindNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MindCache {
    private static final Map<String, List<MindNode>> cache = new HashMap<>();

    public static boolean containsKey(String projectUri) {
        return cache.containsKey(projectUri);
    }

    public static List<MindNode> get(String projectUri) {
        return cache.get(projectUri);
    }

    public static void put(String projectUri, List<MindNode> nodes) {
        cache.put(projectUri, nodes);
    }
}
