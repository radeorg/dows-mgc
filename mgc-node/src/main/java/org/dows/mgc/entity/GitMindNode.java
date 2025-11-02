package org.dows.mgc.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GitMindNode {
    private String name;
    private TxtNode data;
    private List<GitMindNode> children = new ArrayList<>();
}