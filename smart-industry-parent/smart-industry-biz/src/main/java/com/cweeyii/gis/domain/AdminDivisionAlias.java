package com.cweeyii.gis.domain;

import java.util.Date;

/**
 * Created by cweeyii on 9/6/16.
 */
public class AdminDivisionAlias {
    private Integer id;
    private Integer adId;
    private String alias;
    private String languageCode;
    private Date createTime;
    private Integer creator;
    private Date updateTime;
    private Integer updater;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdater() {
        return updater;
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AdminDivisionAlias{");
        sb.append("[id=").append(id);
        sb.append("\tadId=").append(adId);
        sb.append("\talias='").append(alias).append('\'');
        sb.append("\tlanguageCode='").append(languageCode).append('\'');
        sb.append("\tcreateTime=").append(createTime);
        sb.append("\tcreator=").append(creator);
        sb.append("\tupdateTime=").append(updateTime);
        sb.append("\tupdater=").append(updater);
        sb.append("]");
        return sb.toString();
    }
}
