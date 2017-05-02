package com.cweeyii.algorithm.tree;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by cweeyii on 18/7/16 ${EMAIL}.
 */
public class TreeUtil {
    /*
    *                    e
    *                 /     \
    *                a       g
    *                 \      /
    *                  c    f
    *                 / \
    *                b   d
     */

    /*
    *               g
    *            /     \
    *           d      i
    *         /   \   / \
    *        b    f   h  j
    *       /\   /
    *     a   c e
     */

    /*
    *               h
    *            /     \
    *           d       l
    *         /   \   /   \
    *        b    f   j    n
    *       /\   /\  /\   /\
    *     a   c e g  i k m  o
    *     hdbacfegljiknmo
     */
    public static <T> BinTreeNode<T> getTreePreOrderInOrder(T[] preOrder,int big1,int end1,T[] midOrder,int big2,int end2){
        if(big1>end1||big2>end2) return null;
        BinTreeNode<T> root = new BinTreeNode<>(preOrder[big1]);
        int index = 0;//头节点（头结点指的是big1到end1范围内的节点的头节点）在中序big2到end2之间所处的位置
        for(int i=big2; i<end2 ; i++){
            T in = midOrder[i];
            if(in.equals(preOrder[big1])){
                break;
            }else{
                index ++;
            }
        }
        //计算建立左节点的节点范围
        int leftBig1 = big1+1;//先序的起始位置
        int leftEnd1 = big1+index;//先序的最终位置
        int leftBig2 = big2;//中序的起始位置
        int leftEnd2 = big2+index-1;//中序的最终位置
        //计算建立右节点的节点范围
        int rightBig1 = big1+index+1;//先序的起始位置
        int rightEnd1 = end1;//先序的最终位置
        int rightBig2 = big2+index+1;//中序的起始位置
        int rightEnd2 = end2;//中序的最终位置
        //左节点
        BinTreeNode leftNode = getTreePreOrderInOrder(preOrder, leftBig1,leftEnd1,midOrder,leftBig2,leftEnd2);
        //右节点
        BinTreeNode rightNode = getTreePreOrderInOrder(preOrder, rightBig1,rightEnd1,midOrder, rightBig2,rightEnd2);
        root.leftChild = leftNode;
        root.rightChild = rightNode;
        return root;
    }

    public static <T> void preOrderVisited(BinTreeNode<T> root){
        if(root!=null){
            System.out.print(root.val + " ");
            preOrderVisited(root.leftChild);
            preOrderVisited(root.rightChild);
        }
    }

    public static <T> void midOrderVisited(BinTreeNode<T> root){
        if(root!=null){
            midOrderVisited(root.leftChild);
            System.out.print(root.val + " ");
            midOrderVisited(root.rightChild);
        }
    }

    public static <T> int getTreeNodeCount(BinTreeNode<T> root){
        if(root==null) return 0;
        return getTreeNodeCount(root.leftChild)+getTreeNodeCount(root.rightChild)+1;
    }

    public static <T> int getTreeDepth(BinTreeNode<T> root){
        if(root==null) return 0;
        return Math.max(getTreeDepth(root.leftChild),getTreeDepth(root.rightChild))+1;
    }

    /*
    * 二叉查找树变双向有序链表 不能创建新节点，只调整指针
     */
    public static <T> BinTreeNode<T> convertToDoubleLinkedList(BinTreeNode<T> root){
        root=convertToLinkedList(root);
        if(root==null) return null;
        while(root.leftChild!=null){
            root=root.leftChild;
        }
        visitedLinkedList(root);
        return root;
    }
    private static <T> void visitedLinkedList(BinTreeNode<T> root){
        while(root!=null) {
            System.out.print(root.val+" ");
            root=root.rightChild;
        }
    }
    private static <T> BinTreeNode<T> convertToLinkedList(BinTreeNode<T> root){
        if(root==null) return null;
        BinTreeNode leftLinked=convertToLinkedList(root.leftChild);
        if(leftLinked!=null){
            while(leftLinked.rightChild!=null) leftLinked=leftLinked.rightChild;
            leftLinked.rightChild=root;
            root.leftChild=leftLinked;
        }
        BinTreeNode rightLinked=convertToLinkedList(root.rightChild);
        if(rightLinked!=null){
            while(rightLinked.leftChild!=null) rightLinked=rightLinked.leftChild;
            rightLinked.leftChild=root;
            root.rightChild=rightLinked;
        }
        return root;
    }

    public static <T> int getNodeCountByLevel(BinTreeNode<T> root, int level){
        if(root==null|| level<1) return 0;
        if(level==1) return 1;
        return getNodeCountByLevel(root.leftChild, level - 1)+getNodeCountByLevel(root.rightChild, level - 1);
    }

    public static <T> int getLeafNodeCount(BinTreeNode<T> root){
        if(root==null) return 0;
        if(root.leftChild==null&&root.rightChild==null) return 1;
        return getLeafNodeCount(root.leftChild)+getLeafNodeCount(root.rightChild);
    }

    public static <T> boolean isSameTree(BinTreeNode<T> root1, BinTreeNode<T> root2) {
        return root1 == null && root2 == null || root1 != null && root2 != null && root1.val == root2.val && isSameTree(root1.leftChild, root2.leftChild) && isSameTree(root1.rightChild, root2.rightChild);
    }

    public static <T> boolean isAvlTree(BinTreeNode<T> root) {
        return root == null || isAvlTree(root.leftChild) && isAvlTree(root.rightChild) && Math.abs(getTreeDepth(root.leftChild) - getTreeDepth(root.rightChild)) <= 1;
    }

    public static <T> BinTreeNode<T> mirrorTree(BinTreeNode<T> root){
        if(root==null) return null;
        BinTreeNode<T> leftChild=mirrorTree(root.leftChild);
        BinTreeNode<T> rightChild=mirrorTree(root.rightChild);
        root.leftChild=rightChild;
        root.rightChild=leftChild;
        return root;
    }

    public static <T> BinTreeNode<T> mirrorCopyTree(BinTreeNode<T> root){
        if(root==null) return null;
        BinTreeNode<T> newRoot=new BinTreeNode<>(root.val);
        BinTreeNode<T> leftChild=mirrorCopyTree(root.leftChild);
        BinTreeNode<T> rightChild=mirrorCopyTree(root.rightChild);
        newRoot.leftChild=rightChild;
        newRoot.rightChild=leftChild;
        return newRoot;
    }

    public static <T> boolean isMirrorTree(BinTreeNode<T> root1, BinTreeNode<T> root2) {
        return root1 == null && root2 == null || root1 != null && root2 != null && root1.val == root2.val && isMirrorTree(root1.leftChild, root2.rightChild) && isMirrorTree(root1.rightChild, root2.leftChild);
    }

    public static <T> BinTreeNode<T> getFirstParentNode(BinTreeNode<T> root, T val1, T val2){
        if(root==null) return null;
        boolean isLeft=findNode(root.leftChild,val1);
        boolean isRight=findNode(root.rightChild,val2);
        if(isLeft){
            if(isRight) return root;
            return getFirstParentNode(root.leftChild,val1,val2);
        }else {
            if(findNode(root.leftChild,val2)) return root;
            return getFirstParentNode(root.rightChild,val1,val2);
        }
    }

    private static <T> boolean findNode(BinTreeNode<T> root, T value) {
        return root != null && (root.val.equals(value) || findNode(root.leftChild, value) || findNode(root.rightChild, value));
    }

    public static <T> int getMaxDistance(BinTreeNode<T> root){
        if(root==null) return 0;
        int leftMaxDistance=getTreeDepth(root.leftChild);
        int rightMaxDistance=getTreeDepth(root.rightChild);
        return leftMaxDistance+rightMaxDistance+1;
    }

    public static <T> boolean isCompleteBinTree(BinTreeNode<T> root){
        if(root==null) return true;
        Queue<BinTreeNode<T>> queue=new LinkedList<>();
        queue.add(root);
        boolean mustNoChild=false;
        while(!queue.isEmpty()){
            BinTreeNode<T> node=queue.remove();
            if(mustNoChild){
                if(node.leftChild!=null||node.rightChild!=null){
                    return false;
                }
            }else {
                if(node.leftChild!=null&&node.rightChild!=null){
                    queue.add(node.leftChild);
                    queue.add(node.rightChild);
                }else if(node.leftChild!=null&&node.rightChild==null){
                    queue.add(node.leftChild);
                    mustNoChild=true;
                }else if(node.leftChild==null&&node.rightChild!=null){
                    return false;
                }else {
                    mustNoChild=true;
                }
            }
        }
        return true;
    }

    private static <T> Pair<Integer,Boolean> getCompleteTree(BinTreeNode<T> root){
        if(root==null) return new Pair(0,true);
        Pair<Integer,Boolean> leftPair=getCompleteTree(root.leftChild);
        Pair<Integer,Boolean> rightPair=getCompleteTree(root.rightChild);
        if(leftPair.getValue()){
            if(leftPair.getKey().equals(rightPair.getKey())){
                return new Pair<>(rightPair.getKey()+1,rightPair.getValue());
            }else if(leftPair.getKey()==rightPair.getKey()+1){
                return new Pair<>(leftPair.getKey()+1,false);
            }else
                return new Pair<>(-1,false);

        }else {
            if(rightPair.getValue()&&leftPair.getKey()==rightPair.getKey()+1){
                return new Pair<>(leftPair.getKey()+1,false);
            }else {
                return new Pair<>(-1,false);
            }
        }
    }

    public static <T> boolean isCompleteTree(BinTreeNode<T> root){
        return getCompleteTree(root).getKey()!=-1;
    }

    public static <T> boolean isFullTree(BinTreeNode<T> root){
        return getCompleteTree(root).getValue();
    }


    public static void main(String[] args){
        String preOrder[] = {"e","a","c","b","d","g","f"};
        String inOrder[] = {"a","b","c","d","e","f","g"};
        BinTreeNode<String> root = getTreePreOrderInOrder(preOrder, 0, preOrder.length - 1, inOrder, 0, inOrder.length - 1);
        midOrderVisited(root);
        System.out.println();
        System.out.println("二叉树节点数量=" + getTreeNodeCount(root));
        System.out.println("二叉树深度=" + getTreeDepth(root));
        //BinTreeNode<String> head=convertToDoubleLinkedList(root);
        int k=3;
        System.out.println("第"+k+"层节点数="+getNodeCountByLevel(root,k));
        System.out.println("叶子节点数量="+getLeafNodeCount(root));

        String preOrder2[] = {"e","a","c","b","d","g","f","h"};
        String inOrder2[] = {"a","b","c","d","e","f","g","h"};
        BinTreeNode<String> root2 = getTreePreOrderInOrder(preOrder2, 0, preOrder2.length - 1, inOrder2, 0, inOrder2.length - 1);
        System.out.println("是同一颗树=" + isSameTree(root, root2));

        String preOrderAvl[] = {"g","d","b","a","c","f","e","i","h","j"};
        String inOrderAvl[] = {"a","b","c","d","e","f","g","h","i","j"};
        BinTreeNode<String> rootAvl = getTreePreOrderInOrder(preOrderAvl, 0, preOrderAvl.length - 1, inOrderAvl, 0, inOrderAvl.length - 1);
        System.out.println("是否是平衡二叉树="+isAvlTree(rootAvl));
        System.out.println("是否是平衡二叉树="+isAvlTree(root));
        System.out.println("是否是平衡二叉树="+isAvlTree(root2));

        //BinTreeNode<String> mirrorAvl=mirrorTree(rootAvl);
        BinTreeNode<String> mirrorCopyAvl=mirrorCopyTree(rootAvl);
        midOrderVisited(mirrorCopyAvl);
        System.out.println();
        midOrderVisited(rootAvl);
        System.out.println();
        System.out.println("是否是镜像树=" + isMirrorTree(rootAvl, mirrorCopyAvl));

        System.out.println("找到最深公共祖先="+getFirstParentNode(rootAvl,"a","i").val);
        System.out.println("找到最深公共祖先="+getFirstParentNode(rootAvl,"a","f").val);

        System.out.println("树中节点最长路径="+getMaxDistance(root));
        System.out.println("树中节点最长路径="+getMaxDistance(rootAvl));

        System.out.println("是否是完全二叉树="+isCompleteBinTree(root));
        System.out.println("是否是完全二叉树="+isCompleteBinTree(rootAvl));

        System.out.println("是否是完全二叉树="+isCompleteTree(root));
        System.out.println("是否是完全二叉树="+isCompleteTree(rootAvl));

        System.out.println("是否是满二叉树="+isFullTree(root));
        System.out.println("是否是满二叉树=" + isFullTree(rootAvl));

        String preOrderFull[] = {"h","d","b","a","c","f","e","g","l","j","i","k","n","m","o"};
        String inOrderFull[] = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
        BinTreeNode<String> rootFull = getTreePreOrderInOrder(preOrderFull, 0, preOrderFull.length - 1, inOrderFull, 0, inOrderFull.length - 1);
        System.out.println("是否是满二叉树=" + isFullTree(rootFull));

    }

}
