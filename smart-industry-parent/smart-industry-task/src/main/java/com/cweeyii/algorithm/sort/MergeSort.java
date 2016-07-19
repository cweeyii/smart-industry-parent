package com.cweeyii.algorithm.sort;

import java.util.Arrays;

/**
 * Created by cweeyii on 14/7/16 ${EMAIL}.
 */
public class MergeSort {
    public static void sort(int[] elements){
        mergeSort(elements,0,elements.length-1);
    }

    private static void mergeSort(int[] elements,int begin, int end){
        if(begin<end){
            int mid=(begin+end)/2;
            mergeSort(elements,begin,mid);
            mergeSort(elements,mid+1,end);
            merge(elements,begin,mid,end);
        }
    }

    private static void merge(int[] elements,int begin,int mid,int end){
        int len1=mid-begin+1;
        int[] left=new int[len1];
        System.arraycopy(elements, begin, left, 0, len1);
        int len2=end-mid;
        int[] right=new int[len2];
        System.arraycopy(elements, mid+1, right, 0, len2);
        int i=0;
        int j=0;
        int index=begin;
        while(i<len1 &&j< len2){
            if(left[i]<right[j]){
                elements[index++]=left[i++];
            }else{
                elements[index++]=right[j++];
            }
        }
        while(i< len1){
            elements[index++]=left[i++];
        }
        while(j< len2){
            elements[index++]=right[j++];
        }
    }

    public static void main(String[] args){
        int[] elements=new int[]{4,2,3,8,6,7,9};
        sort(elements);
        System.out.print(Arrays.toString(elements));
        int[] elements1=new int[]{4,2,3,3,5,4,2,1,8,6,7,9,0,3,5};
        sort(elements1);
        System.out.print(Arrays.toString(elements1));
    }
}
