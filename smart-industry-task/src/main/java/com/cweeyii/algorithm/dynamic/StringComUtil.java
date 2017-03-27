package com.cweeyii.algorithm.dynamic;

import com.cweeyii.util.StringUtil;

/**
 * Created by wenyi on 17/3/21.
 * Email:caowenyi@meituan.com
 */
public class StringComUtil {

    private static int min(int one, int two, int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min) {
            min = three;
        }
        return min;
    }
    public static int ld(String str1, String str2) {
        int d[][]; // 矩阵
        int n = str1.trim().length();
        int m = str2.trim().length();
        int i; // 遍历str1的
        int j; // 遍历str2的
        char ch1; // str1的
        char ch2; // str2的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) { // 遍历str1
            ch1 = str1.charAt(i - 1);
            // 去匹配str2
            for (j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    private static class LongestVo{
        private int firstStartId;
        private int secondStartId;
        private int length;
        private String commonStr;

        public LongestVo(int firstStartId, int secondStartId, int length, String commonStr) {
            this.firstStartId = firstStartId;
            this.secondStartId = secondStartId;
            this.length = length;
            this.commonStr = commonStr;
        }

        public int getFirstStartId() {
            return firstStartId;
        }

        public int getSecondStartId() {
            return secondStartId;
        }

        public int getLength() {
            return length;
        }

        public String getCommonStr() {
            return commonStr;
        }
    }
    public static LongestVo longestComString(String firstStr, String secondStr){

        if(StringUtil.isBlank(firstStr)||StringUtil.isBlank(secondStr)){
            return new LongestVo(-1,-1,0,"");
        }

        int firstLen=firstStr.length();
        int secondLen=secondStr.length();
        int[][] leftTable=new int[firstLen][secondLen];
        //初始化临界值
        for(int i=0;i<firstLen;i++){
            if(firstStr.charAt(i)==secondStr.charAt(0)){
                leftTable[i][0]=1;
            }
        }
        //初始化临界值
        for(int j=0;j<secondLen;j++){
            if(secondStr.charAt(j)==firstStr.charAt(0)){
                leftTable[0][j]=1;
            }
        }
        //动态规划求解最长子串
        for(int i=1;i<firstLen;i++){
            for (int j=1;j<secondLen;j++){
                if(firstStr.charAt(i)==secondStr.charAt(j)){
                    leftTable[i][j]=leftTable[i-1][j-1]+1;
                }
            }
        }
        //寻找最长子串和开始位置
        int longest=0;
        int firstStartId=-1;
        int secondStartId=-1;
        for(int i=0;i<firstLen;i++){
            for(int j=0;j<secondLen;j++){
                if(longest<leftTable[i][j]){
                    longest=leftTable[i][j];
                    firstStartId=i+1-longest;
                    secondStartId=j+1-longest;
                }
            }
        }
        String comStr="";
        if(firstStartId>-1&&secondStartId>-1){
            comStr=firstStr.substring(firstStartId,firstStartId+longest);
        }
        return new LongestVo(firstStartId,secondStartId,longest,comStr);
    }
}
