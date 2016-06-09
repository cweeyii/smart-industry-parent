package com.cweeyii.dic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public class DicMap {
    Map<Object,Object> map = new HashMap<Object,Object>();
    Map<Object,Object> origPairMap = new HashMap<Object,Object>();

    public DicMap(Object[][] arr){
        for(Object[] a : arr){
            map.put(a[0], a[1]);
            map.put(a[1], a[0]);

            origPairMap.put(a[0], a[1]);
        }
    }

    public DicMap put(Object key,Object value){
        map.put(key, value);
        return this;
    }

    public Object get(Object key){
        return map.get(key);
    }

    public Map<Object,Object> getOrigMap(){
        return origPairMap;
    }
}

