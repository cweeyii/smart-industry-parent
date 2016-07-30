package com.cweeyii.algorithm.graph;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cweeyii on 14/7/16 ${EMAIL}.
 */
public class Node {
    private char value;
    private List<Node> nodeList;

    public Node(char value) {
        this.value = value;
        nodeList=new ArrayList<>();
    }
    public void addNode(Node B){
        nodeList.add(B);
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

}
