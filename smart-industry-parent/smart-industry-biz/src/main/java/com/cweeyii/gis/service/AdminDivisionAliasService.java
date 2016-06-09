package com.cweeyii.gis.service;

import com.cweeyii.gis.domain.AdminDivisionAlias;

import java.util.List;

/**
 * Created by cweeyii on 9/6/16.
 */
public interface AdminDivisionAliasService {
    void insert(AdminDivisionAlias adminDivisionAlias);
    List<AdminDivisionAlias> getADAliasByAdId(Integer adId);
}
