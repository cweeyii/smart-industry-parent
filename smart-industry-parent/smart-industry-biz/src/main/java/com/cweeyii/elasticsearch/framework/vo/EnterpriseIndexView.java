package com.cweeyii.elasticsearch.framework.vo;

import com.cweeyii.elasticsearch.framework.annotation.ElasticSearchParam;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@ElasticSearchParam(indexName = "elasticsearch.enterprise.index", typeName = "enterprise_basic_info")
public class EnterpriseIndexView implements Serializable {

    private Long id;
    private String enterpriseName;
    private Double longitude;
    private Double latitude;
    private String cityName;
    private String districtName;
    private String address;
    private String phone;
    private String businessCategory;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
