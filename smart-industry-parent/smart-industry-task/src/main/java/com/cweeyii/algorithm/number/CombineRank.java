package com.cweeyii.algorithm.number;


import com.cweeyii.util.StringUtil;


/**
 * Created by cweeyii on 15/7/16 ${EMAIL}.
 */
public class CombineRank {
    public static void combineRank(String strLine){
        if(StringUtil.isBlank(strLine)) return;
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<=strLine.length();i++){
            combine(strLine,i,sb);
        }
    }

    private static void combine(String subStrLine, int count, StringBuilder sb){
        if(count==0){
            if(sb.length()!=0) System.out.println(sb.toString());
            return;
        }
        if(StringUtil.isBlank(subStrLine)){
            return;
        }
        combine(subStrLine.substring(1),count-1,sb.append(subStrLine.charAt(0)));
        sb.deleteCharAt(sb.length() - 1);
        combine(subStrLine.substring(1),count,sb);
    }

    public static void main(String[] args){
        String strLine="abc";
        combineRank(strLine);
    }
}
