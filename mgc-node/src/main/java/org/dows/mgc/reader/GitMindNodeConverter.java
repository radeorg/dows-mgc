package org.dows.mgc.reader;

import org.dows.mgc.entity.GitMindNode;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.TxtNode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GitMindNodeConverter {

    /**
     * 将GitMindNode树形结构转换为MindNode扁平结构列表
     * @param rootNode 根节点
     * @return MindNode列表
     */
    public List<MindNode> convertToMindNodes(GitMindNode rootNode) {
        List<MindNode> result = new ArrayList<>();
        Map<String, MindNode> nodeMap = new HashMap<>();
        AtomicInteger nodeIdCounter = new AtomicInteger(1);

        // 递归处理根节点及其子节点
        convertNode(rootNode, null, result, nodeMap, nodeIdCounter);

        // 构建子节点关系
        buildChildrenRelationship(result, nodeMap);

        return result;
    }

    /**
     * 递归转换节点
     */
    private void convertNode(GitMindNode gitNode, String parentId, List<MindNode> result,
                             Map<String, MindNode> nodeMap, AtomicInteger nodeIdCounter) {
        if (gitNode == null) {
            return;
        }

        // 创建MindNode并设置基本属性
        MindNode mindNode = new MindNode();

        // 优先使用TxtNode的id作为MindNode的nodeId，如果不存在则使用计数器生成
        String nodeId = null;
        TxtNode data = gitNode.getData();
        if (data != null && data.getId() != null && !data.getId().isEmpty()) {
            nodeId = data.getId();
        } else {
            // 如果没有TxtNode的id，则使用计数器生成唯一id
            nodeId = String.valueOf(nodeIdCounter.getAndIncrement());
        }

        mindNode.setNodeId(nodeId);
        mindNode.setParentId(parentId);
        mindNode.setChildren(new ArrayList<>());

        // 从GitMindNode和TxtNode中提取内容
        String content = extractContent(gitNode);
        mindNode.setContent(content);

        // 添加到结果集和映射中
        result.add(mindNode);
        nodeMap.put(nodeId, mindNode);

        // 递归处理子节点
        if (gitNode.getChildren() != null) {
            for (GitMindNode child : gitNode.getChildren()) {
                convertNode(child, nodeId, result, nodeMap, nodeIdCounter);
            }
        }
    }

    /**
     * 从GitMindNode中提取content内容
     */
    private String extractContent(GitMindNode gitNode) {
        // 默认使用nodeName作为content
        String content = gitNode.getName();

        // 如果有data属性，使用其text作为content
        TxtNode data = gitNode.getData();
        if (data != null && data.getText() != null && !data.getText().isEmpty()) {
            content = data.getText();
        }

        // 确保content有正确的格式，便于MindNode解析
        // 如果content不包含斜杠，假设它只是一个类型
        if (!content.contains("/")) {
            content = content + "/";
        }

        return content;
    }

    /**
     * 构建子节点关系
     */
    private void buildChildrenRelationship(List<MindNode> mindNodes, Map<String, MindNode> nodeMap) {
        for (MindNode node : mindNodes) {
            String parentId = node.getParentId();
            if (parentId != null) {
                MindNode parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(node.getNodeId());
                }
            }
        }
    }

//    /**
//     * 将GitMindNode树形结构转换为MindNode扁平结构列表
//     * @param rootNode 根节点
//     * @return MindNode列表
//     */
//    public List<MindNode> convertToMindNodes(GitMindNode rootNode) {
//        List<MindNode> result = new ArrayList<>();
//        Map<String, MindNode> nodeMap = new HashMap<>();
//        AtomicInteger nodeIdCounter = new AtomicInteger(1);
//
//        // 递归处理根节点及其子节点
//        convertNode(rootNode, null, result, nodeMap, nodeIdCounter);
//
//        // 构建子节点关系
//        buildChildrenRelationship(result, nodeMap);
//
//        return result;
//    }
//
//    /**
//     * 递归转换节点
//     */
//    private void convertNode(GitMindNode gitNode, String parentId, List<MindNode> result,
//                           Map<String, MindNode> nodeMap, AtomicInteger nodeIdCounter) {
//        if (gitNode == null) {
//            return;
//        }
//
//        // 创建MindNode并设置基本属性
//        MindNode mindNode = new MindNode();
//        String nodeId = String.valueOf(nodeIdCounter.getAndIncrement());
//        mindNode.setNodeId(nodeId);
//        mindNode.setParentId(parentId);
//        mindNode.setChildren(new ArrayList<>());
//
//        // 从GitMindNode和TxtNode中提取内容
//        String content = extractContent(gitNode);
//        mindNode.setContent(content);
//
//        // 添加到结果集和映射中
//        result.add(mindNode);
//        nodeMap.put(nodeId, mindNode);
//
//        // 递归处理子节点
//        if (gitNode.getChildren() != null) {
//            for (GitMindNode child : gitNode.getChildren()) {
//                convertNode(child, nodeId, result, nodeMap, nodeIdCounter);
//            }
//        }
//    }
//
//    /**
//     * 从GitMindNode中提取content内容
//     */
//    private String extractContent(GitMindNode gitNode) {
//        // 默认使用nodeName作为content
//        String content = gitNode.getName();
//
//        // 如果有data属性，使用其text作为content
//        TxtNode data = gitNode.getData();
//        if (data != null && data.getText() != null && !data.getText().isEmpty()) {
//            content = data.getText();
//        }
//
//        // 确保content有正确的格式，便于MindNode解析
//        // 如果content不包含斜杠，假设它只是一个类型
//        if (!content.contains("/")) {
//            content = content + "/";
//        }
//
//        return content;
//    }
//
//    /**
//     * 构建子节点关系
//     */
//    private void buildChildrenRelationship(List<MindNode> mindNodes, Map<String, MindNode> nodeMap) {
//        for (MindNode node : mindNodes) {
//            String parentId = node.getParentId();
//            if (parentId != null) {
//                MindNode parentNode = nodeMap.get(parentId);
//                if (parentNode != null) {
//                    parentNode.getChildren().add(node.getNodeId());
//                }
//            }
//        }
//    }
}