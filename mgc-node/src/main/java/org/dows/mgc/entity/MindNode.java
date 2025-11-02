package org.dows.mgc.entity;

import lombok.Data;

import java.util.List;

@Data
public class MindNode {
    private String nodeId;
    private String parentId;
    private List<String> children;
    private String content;
    private String value;
    private String description;
    private NodeType nodeType;
    private NodeAttribute nodeAttribute;

    public void setContent(String content) {
        this.content = content;
        String[] split = content.split("/");
        try {
            nodeType = NodeType.valueOf(split[0]);
            if(split.length==1){
                value = "";
                description = "";
            } else if(split.length==2){
                value = split[1];
                description = "";
            } else if(split.length==3){
                value = split[1];
                description = split[2];
            }
        } catch (Exception e) {
            //throw new RuntimeException(String.format("节点:%s,类型:%s,不存在", nodeId, content));
        }
    }

}
