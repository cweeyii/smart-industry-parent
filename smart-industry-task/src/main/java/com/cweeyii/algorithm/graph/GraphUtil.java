package com.cweeyii.algorithm.graph;

import java.io.IOException;
import java.util.*;

/**
 * Created by cweeyii on 14/7/16 ${EMAIL}.
 */
//无向图的copy
public class GraphUtil {
    public static Node createGraph() throws IOException {
        Node A = new Node('A');
        Node B = new Node('B');
        Node C = new Node('C');
        Node D = new Node('D');
        A.addNode(B);
        A.addNode(C);
        B.addNode(A);
        B.addNode(D);
        C.addNode(A);
        C.addNode(D);
        D.addNode(B);
        D.addNode(C);
        return A;
    }

    public static void printGraph(Node A, Set<Node> usedNode) {
        System.out.print(A.getValue() + "\t");
        usedNode.add(A);
        for (Node tmp : A.getNodeList()) {
            if (!usedNode.contains(tmp)) {
                printGraph(tmp, usedNode);
            }
        }
    }

    public static Node cloneGraph(Node A) {
        Set<Node> used = new HashSet<>();
        Map<Node, Node> newMap = new HashMap<>();
        return cloneNode(A, used, newMap);
    }

    private static Node cloneNode(Node A, Set<Node> used, Map<Node, Node> newMap) {
        Node B = new Node(A.getValue());
        List<Node> nodeList = new ArrayList<>(A.getNodeList().size());
        B.setNodeList(nodeList);
        used.add(A);
        newMap.put(A, B);
        for (Node aTmp : A.getNodeList()) {
            if (!used.contains(aTmp)) {
                nodeList.add(cloneNode(aTmp, used, newMap));
            } else {
                nodeList.add(newMap.get(aTmp));
            }
        }
        return B;
    }

    public static void main(String[] args) throws IOException {
        Node A = createGraph();
        Set<Node> usedNode = new HashSet<>();
        printGraph(A, usedNode);
        System.out.println();
        usedNode.clear();
        Node B = cloneGraph(A);
        usedNode.clear();
        printGraph(B, usedNode);
        System.out.println();
    }
}
