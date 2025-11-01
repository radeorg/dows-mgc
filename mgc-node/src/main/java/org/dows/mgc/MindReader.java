package org.dows.mgc;

import lombok.RequiredArgsConstructor;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MindReader {
    private final ResourceLoader resourceLoader;

    private static final Map<String, List<MindNode>> cache = new HashMap<>();


    public Map<NodeType, List<MindNode>> getNodeTypeMap(String appId) {
        return getMindNodeList(appId).stream().collect(Collectors.groupingBy(MindNode::getNodeType));
    }

    public Map<String, MindNode> getNodeIdMap(String appId) {
        return getMindNodeList(appId).stream().collect(Collectors.toMap(MindNode::getNodeId, node -> node));
    }

    public List<MindNode> getMindNodeList(String appId) {
        if (cache.containsKey(appId)) {
            return cache.get(appId);
        }
        List<MindNode> nodes = new ArrayList<>();
        try {

            Resource resource = resourceLoader.getResource(String.format("classpath:%s.txt", appId));
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            // 使用ArrayDeque代替Stack，性能更优
            Deque<MindNode> nodeStack = new LinkedList<>();
            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;

                // 快速计算缩进级别
                int level = getIndentLevel(line);
                String content = line.trim();

                // 跳过空行
                if (content.isEmpty()) {
                    continue;
                }

                // 调整栈大小以匹配当前层级
                while (nodeStack.size() > level) {
                    nodeStack.pollLast();
                }

                // 创建新节点并初始化children列表
                MindNode node = new MindNode();
                node.setNodeId(String.valueOf(lineNum));
                node.setContent(content);
                node.setChildren(new ArrayList<>()); // 初始化children列表

                // 设置父节点ID
                if (!nodeStack.isEmpty()) {
                    node.setParentId(nodeStack.peekLast().getNodeId());
                } else {
                    node.setParentId(null);
                }

                // 添加到结果集并入栈
                nodes.add(node);
                nodeStack.addLast(node);
            }

            reader.close();

            // 构建父子关系：为每个节点添加子节点到children列表
            // 创建节点ID到节点的映射，用于快速查找
            Map<String, MindNode> nodeMap = nodes.stream()
                    .collect(Collectors.toMap(MindNode::getNodeId, node -> node));

            // 遍历所有节点，将子节点ID添加到父节点的children列表中
            for (MindNode node : nodes) {
                if (node.getParentId() != null) {
                    MindNode parent = nodeMap.get(node.getParentId());
                    if (parent != null) {
                        parent.getChildren().add(node.getNodeId());
                    }
                }
            }

            cache.put(appId, nodes);
            return nodes;
        } catch (Exception e) {
            throw new RuntimeException("读取并解析文件失败", e);
        }
        /*if (cache.containsKey(appId)) {
            return cache.get(appId);
        }
        List<MindNode> nodes = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource(String.format("classpath:%s.txt", appId));
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            // 使用ArrayDeque代替Stack，性能更优
            Deque<MindNode> nodeStack = new LinkedList<>();
            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;

                // 快速计算缩进级别
                int level = getIndentLevel(line);
                String content = line.trim();

                // 跳过空行
                if (content.isEmpty()) {
                    continue;
                }

                // 调整栈大小以匹配当前层级 - 核心优化点
                while (nodeStack.size() > level) {
                    nodeStack.pollLast();
                }

                // 创建新节点
                MindNode node = new MindNode();
                node.setNodeId(String.valueOf(lineNum));
                node.setContent(content);

                // 设置父节点ID
                if (!nodeStack.isEmpty()) {
                    node.setParentId(nodeStack.peekLast().getNodeId());
                } else {
                    node.setParentId(null);
                }

                // 添加到结果集并入栈
                nodes.add(node);
                nodeStack.addLast(node);
            }

            reader.close();
            cache.put(appId, nodes);
            return nodes;
        } catch (Exception e) {
            throw new RuntimeException("读取并解析文件失败", e);
        }*/
    }

    /**
     * 计算行的缩进级别
     * 优化的计算方法，支持tab和空格缩进
     */
    private int getIndentLevel(String line) {
        int level = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\t') {
                level++;
            } else if (c == ' ') {
                // 支持4个空格等于1个tab的缩进方式
                if ((i + 1) % 4 == 0) {
                    level++;
                }
            } else {
                break;
            }
        }
        return level;
    }


//    public List<MindNode> mindNodes(String appId) {
//        List<MindNode> nodes = new ArrayList<>();
//        try {
//            Resource resource = resourceLoader.getResource("classpath:100000_rest.txt");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//
//            // 使用栈来维护当前路径上的节点
//            Stack<MindNode> nodeStack = new Stack<>();
//            String line;
//            int lineNum = 0;
//
//            while ((line = reader.readLine()) != null) {
//                lineNum++;
//                String trimmedLine = line.trim();
//                if (trimmedLine.isEmpty()) {
//                    continue;
//                }
//
//                // 计算缩进级别（制表符数量）
//                int level = 0;
//                for (int i = 0; i < line.length(); i++) {
//                    if (line.charAt(i) == '\t') {
//                        level++;
//                    } else {
//                        break;
//                    }
//                }
//
//                // 调整栈大小以匹配当前层级
//                while (nodeStack.size() > level) {
//                    nodeStack.pop();
//                }
//
//                // 创建新节点
//                MindNode node = new MindNode();
//                node.setNodeId(String.valueOf(lineNum));
//                node.setContent(trimmedLine);
//
//                // 设置父节点
//                if (!nodeStack.isEmpty()) {
//                    node.setParentId(nodeStack.peek().getNodeId());
//                } else {
//                    node.setParentId(null);
//                }
//
//                // 添加到结果集和栈中
//                nodes.add(node);
//                nodeStack.push(node);
//            }
//
//            reader.close();
//            return nodes;
//        } catch (Exception e) {
//            throw new RuntimeException("读取并解析文件失败", e);
//        }


//        List<MindNode> nodes = new ArrayList<>();
//        try {
//            Resource resource = resourceLoader.getResource("classpath:100000_rest.txt");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//
//            // 使用栈来维护当前路径上的节点
//            Stack<MindNode> nodeStack = new Stack<>();
//            String line;
//            int lineNum = 0;
//
//            while ((line = reader.readLine()) != null) {
//                lineNum++;
//                String trimmedLine = line.trim();
//                if (trimmedLine.isEmpty()) {
//                    continue;
//                }
//
//                // 计算缩进级别（制表符数量）
//                int level = 0;
//                for (int i = 0; i < line.length(); i++) {
//                    if (line.charAt(i) == '\t') {
//                        level++;
//                    } else {
//                        break;
//                    }
//                }
//
//                // 调整栈大小以匹配当前层级
//                while (nodeStack.size() > level) {
//                    nodeStack.pop();
//                }
//
//                // 创建新节点
//                MindNode node = new MindNode();
//                node.setNodeId(String.valueOf(lineNum));
//                node.setContent(trimmedLine);
//
//                // 设置父节点
//                if (!nodeStack.isEmpty()) {
//                    node.setParentId(nodeStack.peek().getNodeId());
//                } else {
//                    node.setParentId(null);
//                }
//
//                // 添加到结果集和栈中
//                nodes.add(node);
//                nodeStack.push(node);
//            }
//
//            reader.close();
//            return nodes;
//        } catch (Exception e) {
//            throw new RuntimeException("读取并解析文件失败", e);
//        }

//        List<MindNode> nodes = new ArrayList<>();
//        try {
//            Resource resource = resourceLoader.getResource("classpath:100000_rest.txt");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//
//            // 使用栈来维护当前路径上的节点
//            Stack<MindNode> nodeStack = new Stack<>();
//            String line;
//            int lineNum = 0;
//
//            while ((line = reader.readLine()) != null) {
//                lineNum++;
//                String trimmedLine = line.trim();
//                if (trimmedLine.isEmpty()) {
//                    continue;
//                }
//
//                // 计算缩进级别
//                int level = 0;
//                for (int i = 0; i < line.length(); i++) {
//                    if (line.charAt(i) == '\t') {
//                        level++;
//                    } else {
//                        break;
//                    }
//                }
//
//                // 调整栈大小以匹配当前层级
//                while (nodeStack.size() > level) {
//                    nodeStack.pop();
//                }
//
//                // 创建新节点
//                MindNode node = new MindNode();
//                node.setNodeId(String.valueOf(lineNum));
//                node.setContent(trimmedLine);
//
//                // 设置父节点
//                if (!nodeStack.isEmpty()) {
//                    node.setParentId(nodeStack.peek().getNodeId());
//                } else {
//                    node.setParentId(null);
//                }
//
//                // 添加到结果集和栈中
//                nodes.add(node);
//                nodeStack.push(node);
//            }
//
//            reader.close();
//            return nodes;
//        } catch (Exception e) {
//            throw new RuntimeException("读取并解析文件失败", e);
//        }


//        List<MindNode> nodes = new ArrayList<>();
//        try {
//            // 读取resource下的rest.txt文件
//            Resource resource = resourceLoader.getResource("classpath:100000_rest.txt");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//
//            // 用于记录每一级的最后一个节点ID，用于确定父节点
//            Map<Integer, String> levelLastNodeIdMap = new HashMap<>();
//            String line;
//            int lineNum = 0;
//
//            // 逐行读取并解析
//            while ((line = reader.readLine()) != null) {
//                lineNum++;
//                // 去除前后空格，保留缩进信息
//                String trimmedLine = line.trim();
//                if (trimmedLine.isEmpty()) {
//                    continue;
//                }
//
//                // 计算缩进级别（假设使用tab缩进）
//                int level = 0;
//                for (int i = 0; i < line.length(); i++) {
//                    if (line.charAt(i) == '\t') {
//                        level++;
//                    } else {
//                        break;
//                    }
//                }
//
//                // 创建节点
//                MindNode node = new MindNode();
//                node.setNodeId(String.valueOf(lineNum)); // 按行号生成nodeId
//
//                // 设置父节点ID
//                if (level > 0) {
//                    node.setParentId(levelLastNodeIdMap.get(level - 1));
//                } else {
//                    node.setParentId(null); // 根节点没有父节点
//                }
//
//                // 设置内容
//                node.setContent(trimmedLine);
//
//                // 添加到结果集
//                nodes.add(node);
//
//                // 更新当前级别最后一个节点ID
//                levelLastNodeIdMap.put(level, node.getNodeId());
//
//                // 清理高于当前级别的节点ID记录
//                for (int i = level + 1; i <= 10; i++) { // 假设最多10级嵌套
//                    levelLastNodeIdMap.remove(i);
//                }
//            }
//
//            reader.close();
//        } catch (Exception e) {
//            throw new RuntimeException("读取并解析文件失败", e);
//        }
//        return nodes;
//    }
}
