package com.cweeyii.zookeeper.sample;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenyi on 16/8/26.
 * Email:caowenyi@meituan.com
 */
public class EquivalenceRelation {
    private static final Logger LOGGER= LoggerFactory.getLogger(EquivalenceRelation.class);
    private Map<Long,Long> dupWmPoiMap=new HashMap<>();

    @Test
    public void testFunction(){
        List<Pair<Long,Long>> pairs1= Lists.newArrayList(new Pair<>(32659L,27712L),new Pair<>(34937L,32659L),
                new Pair<>(32659L,34937L));
        doWork(pairs1);
        List<Pair<Long,Long>> pairs2= Lists.newArrayList(new Pair<>(34937L,32659L),
                new Pair<>(32659L,34937L),new Pair<>(32659L,27712L));
        doWork(pairs2);
        List<Pair<Long,Long>> pairs3= Lists.newArrayList(new Pair<>(32659L,34937L),
                new Pair<>(32659L,27712L),new Pair<>(34937L,32659L));
        doWork(pairs3);
        List<Pair<Long,Long>> pairs4= Lists.newArrayList(new Pair<>(32659L,27712L),
                new Pair<>(32659L,34937L),new Pair<>(34937L,32659L));
        doWork(pairs4);

    }

    private void doWork( List<Pair<Long,Long>> pairs){
        for(Pair<Long,Long> pair:pairs){
            computeEquivalence(pair.getKey(),pair.getValue());
        }
        printEqMap();
    }
    private void computeEquivalence(Long firstId,Long secondId){
        Long smaller = firstId;
        Long larger = secondId;
        if (larger < smaller) {
            larger = firstId;
            smaller = secondId;
        }
        boolean containS = dupWmPoiMap.containsKey(smaller);
        boolean containL = dupWmPoiMap.containsKey(larger);
        Long valueS = dupWmPoiMap.get(smaller);
        Long valueL = dupWmPoiMap.get(larger);
        if (containS && containL) {
            String info = String.format("已经包含传递关系:%d->%d %d->%d", smaller, valueS, larger, valueL);
            LOGGER.info(info);
        } else if (containS) {
            if (valueS.equals(0L)) {
                dupWmPoiMap.put(larger, smaller);
            } else {
                dupWmPoiMap.put(larger, valueS);
            }
        } else if (containL) {
            if (valueL.equals(0L)){
                dupWmPoiMap.put(smaller, larger);
            }else{
                dupWmPoiMap.put(smaller,valueL);
            }
        } else {
            dupWmPoiMap.put(larger, smaller);
            dupWmPoiMap.put(smaller, 0L);
        }
    }

    private void printEqMap(){
        for(Map.Entry<Long,Long> entry:dupWmPoiMap.entrySet()){
            LOGGER.info(entry.getKey()+"->"+entry.getValue());
        }
        LOGGER.info("\n");
        dupWmPoiMap.clear();
    }

    @Test
    public void testPow(){
        System.out.println(Math.pow(10,4));
    }
}
