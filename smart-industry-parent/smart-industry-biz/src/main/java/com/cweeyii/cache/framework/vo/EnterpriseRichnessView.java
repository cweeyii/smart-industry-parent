package com.cweeyii.cache.framework.vo;


import com.cweeyii.cache.framework.annotation.BaseDataProvider;
import com.cweeyii.cache.framework.annotation.CacheEntity;
import com.cweeyii.cache.framework.annotation.DataServiceUnit;
import com.cweeyii.cache.framework.annotation.MedisDataType;
import com.cweeyii.rabbitmq.framework.vo.InternalMessageFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@SuppressWarnings("unused")
@BaseDataProvider(value = "EnterpriseDataProviderUnit")
@CacheEntity(MedisDataType.Map)
@Component
public class EnterpriseRichnessView implements Serializable {

    /**
     * part 1 :POI基础信息
     */
    // POI ID
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private Long id;
    // 企业名
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private String enterpriseName;
    // 经度
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private Double longitude;
    // 纬度
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private Double latitude;
    // 所在城市
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private String cityName;
    // 行政区
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private String districtName;
    //企业地址
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private String address;
    //企业电话
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private String phone;
    // 经营品类
    @DataServiceUnit(dataProviderUnit = "EnterpriseDataProviderUnit",
            relatedMessageType = {InternalMessageFactory.MessageType.POI_CREATE,
                    InternalMessageFactory.MessageType.POI_CREATE_V2,
                    InternalMessageFactory.MessageType.POI_MODIFY_BASE,
                    InternalMessageFactory.MessageType.POI_MODIFY_CORE})
    private String businessCategory;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
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
}
