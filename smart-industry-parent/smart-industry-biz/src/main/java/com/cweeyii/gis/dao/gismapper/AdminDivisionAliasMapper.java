package com.cweeyii.gis.dao.gismapper;

import com.cweeyii.gis.domain.AdminDivisionAlias;

import java.util.List;

/**
 * Created by cweeyii on 9/6/16.
 */
public interface AdminDivisionAliasMapper {
    void insert(AdminDivisionAlias adminDivisionAlias);
    List<AdminDivisionAlias> getADAliasByAdId(Integer adId);
}
