package com.cweeyii.algorithm.recursion;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenyi on 17/3/24.
 * Email:caowenyi@meituan.com
 */
public class RecursionUtil {
    public static  void computeScore(List<Integer> scoreList, Integer score, int times, List<Integer> resultList){
        if(times==0&&score==0){
            System.out.println(resultList);
        }
        if(times>0&&score>0){
            for(Integer s:scoreList){
                resultList.add(s);
                computeScore(scoreList,score-s,times-1,resultList);
                resultList.remove(resultList.size()-1);
            }
        }
    }

    public static void main(String[] args){
        /*List<Integer> scoreList= Lists.newArrayList(3,1,0);
        Integer score=20;
        int times=8;
        List<Integer> resultList=new ArrayList<>();
        computeScore(scoreList, score, times, resultList);*/

        List<Integer> stepKinds=Lists.newArrayList(1,2);
        List<Integer> steps=new ArrayList<>();
        stepStairs(stepKinds,10,steps);
    }

    public static void stepStairs(List<Integer> stepKinds,int n, List<Integer> steps){
        if(n==0){
            System.out.println(steps);
        }
        if(n>0){
            for(Integer step:stepKinds){
                steps.add(step);
                stepStairs(stepKinds, n - step, steps);
                steps.remove(steps.size()-1);
            }
        }

    }
}
