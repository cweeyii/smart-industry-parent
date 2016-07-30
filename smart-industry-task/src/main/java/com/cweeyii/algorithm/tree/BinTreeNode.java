package com.cweeyii.algorithm.tree;


/**
 * Created by cweeyii on 18/7/16 ${EMAIL}.
 */
public class BinTreeNode<T> {
    public T val;
    public BinTreeNode leftChild;
    public BinTreeNode rightChild;

    public BinTreeNode(T val){
        this.val=val;
    }
}
