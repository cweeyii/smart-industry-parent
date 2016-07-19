package com.cweeyii.algorithm.sort;

import java.util.Arrays;

/**
 * Created by cweeyii on 14/7/16 ${EMAIL}.
 */
public class QuickSort {

    public static void sort(int[] elements){
        quickSort(elements,0,elements.length-1);
    }
    private static void quickSort(int[] elements,int begin, int end){
        if(begin<end){
            int splitIndex=partition(elements,begin,end);
            quickSort(elements,begin,splitIndex-1);
            quickSort(elements,splitIndex+1,end);
        }
    }

    private static int partition(int[] elements, int begin, int end){
        int value=elements[begin];
        while(begin<end){
            while(elements[end]>=value&&begin<end){
                end--;
            }
            if(begin<end){
                elements[begin++]=elements[end];
            }

            while (elements[begin]<=value&&begin<end){
                begin++;
            }
            if(begin<end){
                elements[end--]=elements[begin];
            }
        }
        elements[begin]=value;
        return begin;
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
