package com.cweeyii.algorithm.search;

/**
 * Created by cweeyii on 14/7/16 ${EMAIL}.
 */
public class BinSearch {
    public static boolean search(int[] elements, int value){
        int len=elements.length;
        int begin=0;
        int end=len-1;
        int mid=(begin+end)/2;
        while(mid<end){
            if(elements[mid]==value){
                return true;
            }else if(elements[mid]>value){
                end=mid-1;
            }else{
                begin=mid+1;
            }
            mid=(begin+end)/2;
        }
        return false;
    }

    public static void main(String[] args){
        int[] elements1=new int[]{1,3,5,7,9};
        System.out.print(search(elements1,5));
        System.out.print(search(elements1,6));
        int[] elements2=new int[]{1,5,5,7,8,10};
        System.out.print(search(elements2,5));
        int[] elements3=new int[]{1,5,5,7,8,10,11};
        System.out.print(search(elements3,5));
    }
}
