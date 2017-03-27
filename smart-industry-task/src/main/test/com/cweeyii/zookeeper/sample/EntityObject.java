package com.cweeyii.zookeeper.sample;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenyi on 16/9/24.
 * Email:caowenyi@meituan.com
 */
public class EntityObject {
    private int f11;
    private Integer f12;
    private boolean f21;
    private Boolean f22;
    private String f31;
    private Double f41;
    private Float f51;
    private String f61;

    public EntityObject(int f11, Integer f12, boolean f21, Boolean f22, String f31, Double f41, Float f51) {
        this.f11 = f11;
        this.f12 = f12;
        this.f21 = f21;
        this.f22 = f22;
        this.f31 = f31;
        this.f41 = f41;
        this.f51 = f51;
    }

    public int getF11() {
        return f11;
    }

    public void setF11(int f11) {
        this.f11 = f11;
    }

    public Integer getF12() {
        return f12;
    }

    public void setF12(Integer f12) {
        this.f12 = f12;
    }

    public boolean isF21() {
        return f21;
    }

    public void setF21(boolean f21) {
        this.f21 = f21;
    }

    public Boolean getF22() {
        return f22;
    }

    public void setF22(Boolean f22) {
        this.f22 = f22;
    }

    public String getF31() {
        return f31;
    }

    public void setF31(String f31) {
        this.f31 = f31;
    }

    public Double getF41() {
        return f41;
    }

    public void setF41(Double f41) {
        this.f41 = f41;
    }

    public Float getF51() {
        return f51;
    }

    public void setF51(Float f51) {
        this.f51 = f51;
    }

    public static void  main(String[] args){
        EntityObject object=new EntityObject(1,2,false,true,"zhongguo",4.0,5.0f);
        JSONObject jsonObject= (JSONObject) JSON.toJSON(object);
        Float value=(Float)jsonObject.get("f51");
        System.out.println(jsonObject);

        List<Long> list= Lists.newArrayList(new ArrayList<Long>());
        list.addAll(new ArrayList<Long>());
    }
}
