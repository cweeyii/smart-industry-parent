package com.cweeyii.algorithm.linked;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by wenyi on 17/3/23.
 * Email:caowenyi@meituan.com
 */
public class LinkedListUtil{

    public static <T> LinkedNode createLinkedList(List<T> datas){
        if(CollectionUtils.isEmpty(datas)){
            return null;
        }
        LinkedNode<T> head=new LinkedNode<>();
        head.data=datas.get(0);
        LinkedNode<T> root=head;

        for(int i=1;i<datas.size();i++){
            LinkedNode<T> node=new LinkedNode<>();
            node.data=datas.get(i);
            node.next=null;
            head.next=node;
            head=head.next;
        }
        return root;
    }

    public static <T> String visitedLinked(LinkedNode<T> root){
        StringBuilder stringBuilder=new StringBuilder();
        if(root==null){
            return stringBuilder.toString();
        }
        while(root!=null){
            stringBuilder.append(root.data).append(" ");
            root=root.next;
        }
        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }

    public static <T> LinkedNode<T> reverse(LinkedNode<T> root){
        if(root==null||root.next==null){
            return root;
        }
        LinkedNode<T> node=reverse(root.next);
        root.next.next=root;
        root.next=null;
        return node;
    }

    public static <T> LinkedNode<T> reverse2(LinkedNode<T> root){
        if(root==null){
            return null;
        }
        LinkedNode<T> cur=root.next;
        LinkedNode<T> pre=root;
        pre.next=null;
        while(cur!=null){
            LinkedNode<T> tmp=cur.next;
            cur.next=pre;
            pre=cur;
            cur=tmp;
        }
        return pre;
    }

    public static void main(String[] args){
        LinkedNode<Integer> root=LinkedListUtil.createLinkedList(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        System.out.println(LinkedListUtil.visitedLinked(root));
        LinkedNode<Integer> reverseRoot=LinkedListUtil.reverse(root);
        System.out.println(LinkedListUtil.visitedLinked(reverseRoot));
        LinkedNode<Integer> reverseRoot2=LinkedListUtil.reverse2(reverseRoot);
        System.out.println(LinkedListUtil.visitedLinked(reverseRoot2));

    }
}
