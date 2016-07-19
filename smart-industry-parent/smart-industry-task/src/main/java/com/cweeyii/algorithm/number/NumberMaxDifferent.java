package com.cweeyii.algorithm.number;

/**
 * Created by cweeyii on 15/7/16 ${EMAIL}.
 */
public class NumberMaxDifferent {
    /*
    *求数对之差的最大值:右方-左方
     */
    public static int differentOfNumber(int[] elements){
        return getMaxDifferentOfNumber(elements,0,elements.length-1);
    }

    private static int getMaxValue(int[] elements, int begin,int end){
        int max=Integer.MIN_VALUE;
        for(int i=begin;i<=end;i++){
            if(elements[i]>max){
                max=elements[i];
            }
        }
        return max;
    }
    private static int getMinValue(int[] elements, int begin,int end){
        int min=Integer.MAX_VALUE;
        for(int i=begin;i<=end;i++){
            if(elements[i]<min){
                min=elements[i];
            }
        }
        return min;
    }
    private static int getMaxDifferentOfNumber(int[] elements, int begin,int end){
        int max=Integer.MIN_VALUE;
        for(int k=begin;k<end;k++){
            int maxBetween=getMaxValue(elements,k+1,end)-getMinValue(elements,begin,k);
            int maxLeft=getMaxDifferentOfNumber(elements,begin,k);
            int maxRight=getMaxDifferentOfNumber(elements,k+1,end);
            int kMax=Math.max(maxBetween,Math.max(maxLeft,maxRight));
            if(max<kMax) max=kMax;
        }
        return max;
    }

    public static void main(String[] args){
        int[] elements=new int[]{2, 4, 1, 16, 7, 5, 11, 9};
        System.out.println(differentOfNumber(elements));
        int[] elements1=new int[]{0, 4, 1, 16, 7, 5, 11, 29};
        System.out.println(differentOfNumber(elements1));
    }
}
