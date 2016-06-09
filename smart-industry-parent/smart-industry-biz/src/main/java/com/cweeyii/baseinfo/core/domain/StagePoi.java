package com.cweeyii.baseinfo.core.domain;


import com.cweeyii.baseinfo.core.helper.StagePoiHelper;
import com.cweeyii.dic.DicMap;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.beans.Transient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class StagePoi implements Serializable {
    private static final long serialVersionUID = 9134947943678520341L;
    /*** 倒闭状态 ***************************************************************************************/
    public static final int CLOSE_STATUS_TRUE = 1, CLOSE_STATUS_FALSE = 0;
    public static DicMap DIC_OPERATE_STATUS = new DicMap(new Object[][] { { 0, "营业中" },
            { 1, "已关门" }, {2, "筹建中"}, {3, "暂停营业"} });
    public static final String ES_POI_INDEX_KEY = "elasticsearch.poit.index";
    public static final String ES_POI_TYPE= "stagePoi";
    public enum PoiType {
        MERCHANT(0),	//门店
        UN_MERCHANT(1),		//非门店
        WULIU(2),   //物流
        MOVIE(3),   //提货点
        WEBSITE(4); //网站

        private int code;

        PoiType(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }
    }
    public enum CloseStatus {
        OPERATE(0),	//营业中
        CLOSE(1),		//已倒闭
        PREPARE(2),   //筹建中
        SUSPEND(3);   //暂停营业

        private int code;

        CloseStatus(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }

        private static Set<Integer> values;
        static {
            Set<Integer> localValues = new HashSet<>();
            CloseStatus[] closeStatuses = CloseStatus.values();
            for (CloseStatus cs : closeStatuses) {
                localValues.add(cs.code);
            }
            values = localValues;
        }
        public static boolean isCodeValid(Integer code) {
            return values.contains(code);
        }
    }
    /****************************************************************************************************/

    // POI ID
    private Long id;
    // 门店名
    private String pointName;
    // 品牌ID
    private Integer brandId;
    // 品牌
    private String brandName;
    // 别名
    private String alias;
    // 详细信息
    private String info;
    // 营业时间信息
    private String openInfo;
    // 经度，实际经度值*10的6次方取整
    private Integer longitude;
    // 纬度，实际纬度值*10的6次方取整
    private Integer latitude;
    // 加偏经度
    private Integer trimedLongitude;
    // 加偏纬度
    private Integer trimedLatitude;
    // 所在城市id
    private Integer cityId;
    // 行政区id
    private Integer districtId;
    // 商圈id
    private Integer bizAreaId;
    // 新商圈id(主)
    private Integer bareaId;
    // 新商圈ids(all)
    private String bareaIds;
    // 所在地标

    private String landmark;
    // 地铁ids
    private String subwayIds;
    // 地址
    private String address;
    // 联系电话
    private String phone;
    // 品类id
    private Integer typeId;
    // 10:大众点评导入 1:网页创建 5:MOMA客户端创建 6:旧系统导入 7:交易关联导入 8.通过交易关联新建 9:电影APP EXCEL导入
    // 100:MIS系统导入
    private Integer source;
    // 创建人id
    private Integer creatorId;
    // 创建时间
    private Integer createTime;
    // 修改时间
    private Integer modifyTime;
    // 合并到的目标id
    private Long mergeId;
    // 准确性处理结果 1通过 2放弃
    private Integer accurateResult;
    // 倒闭状态：0-营业中；1-倒闭
    private Integer closeStatus;
    // 经营状态：0-倒闭；1-营业中
    private Integer operateStatus;
    // 同步状态 1同步 2不同步 3不同步不导出 4不同步强制导出
    private Integer syncStatus;
    // poi类型
    private Integer poiType;
    // 经营面积
    private Integer area;
    // 接待量
    private Integer customerNum;
    // 停车位信息
    private String parkingInfo;
    // 是否有wifi
    private Integer wifi;
    // 门店风格
    private String styleIds;
    // location id
    private Integer locationId;
    //门类标签id
    private String catId;
    //人均单价
    private Integer pricePerson;
    //城市id
    private Integer cityLocationId;
    //预计营业时间
    private Integer openDate;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPointName() {
        return this.pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public Integer getBrandId() {
        return this.brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOpenInfo() {
        return this.openInfo;
    }

    public void setOpenInfo(String openInfo) {
        this.openInfo = openInfo;
    }

    public Integer getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDistrictId() {
        return this.districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getBizAreaId() {
        return this.bizAreaId;
    }

    public void setBizAreaId(Integer bizAreaId) {
        this.bizAreaId = bizAreaId;
    }

    public Integer getBareaId() {
        return bareaId;
    }

    public void setBareaId(Integer bareaId) {
        this.bareaId = bareaId;
    }

    public String getBareaIds() {
        return bareaIds;
    }

    public void setBareaIds(String bareaIds) {
        this.bareaIds = bareaIds;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getSource() {
        return this.source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Integer modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getMergeId() {
        return this.mergeId;
    }

    public void setMergeId(Long mergeId) {
        this.mergeId = mergeId;
    }

    public Integer getAccurateResult() {
        return this.accurateResult;
    }

    public void setAccurateResult(Integer accurateResult) {
        this.accurateResult = accurateResult;
    }

    public Integer getCloseStatus() {
        return this.closeStatus;
    }

    public void setCloseStatus(Integer closeStatus) {
        this.closeStatus = closeStatus;
    }

    public Integer getOperateStatus() {
        return this.operateStatus;
    }

    public void setOperateStatus(Integer operateStatus) {
        this.operateStatus = operateStatus;
    }

    public Integer getSyncStatus() {
        return this.syncStatus;
    }

    public void setSyncStatus(Integer syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Integer getPoiType() {
        return this.poiType;
    }

    public void setPoiType(Integer poiType) {
        this.poiType = poiType;
    }

    public Integer getTrimedLongitude() {
        return trimedLongitude;
    }

    public void setTrimedLongitude(Integer trimedLongitude) {
        this.trimedLongitude = trimedLongitude;
    }

    public Integer getTrimedLatitude() {
        return trimedLatitude;
    }

    public void setTrimedLatitude(Integer trimedLatitude) {
        this.trimedLatitude = trimedLatitude;
    }

    public Integer getArea() {
        return this.area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getCustomerNum() {
        return this.customerNum;
    }

    public void setCustomerNum(Integer customerNum) {
        this.customerNum = customerNum;
    }

    public String getParkingInfo() {
        return parkingInfo;
    }

    public void setParkingInfo(String parkingInfo) {
        this.parkingInfo = parkingInfo;
    }

    public Integer getWifi() {
        return wifi;
    }

    public void setWifi(Integer wifi) {
        this.wifi = wifi;
    }

    public String getStyleIds() {
        return styleIds;
    }

    public void setStyleIds(String styleIds) {
        this.styleIds = styleIds;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
	
	@Transient
	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}
	
	public Integer getPricePerson() {
		return pricePerson;
	}

	public void setPricePerson(Integer pricePerson) {
		this.pricePerson = pricePerson;
	}

    public Integer getCityLocationId() {
        return cityLocationId;
    }

    public void setCityLocationId(Integer cityLocationId) {
        this.cityLocationId = cityLocationId;
    }

    public Integer getOpenDate() {
        if(openDate == null){
            return StagePoiHelper.getDefaultOpenDate();
        }
        return openDate;
    }

    public void setOpenDate(Integer openDate) {
        if(openDate == null){
            this.openDate = StagePoiHelper.getDefaultOpenDate();
        }
        this.openDate = openDate;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
