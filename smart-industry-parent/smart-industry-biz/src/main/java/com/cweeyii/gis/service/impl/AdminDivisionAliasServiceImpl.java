package com.cweeyii.gis.service.impl;

import com.cweeyii.gis.dao.gismapper.AdminDivisionAliasMapper;
import com.cweeyii.gis.domain.AdminDivisionAlias;
import com.cweeyii.gis.service.AdminDivisionAliasService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cweeyii on 9/6/16.
 */
@Service
public class AdminDivisionAliasServiceImpl implements AdminDivisionAliasService {
    @Resource
    private AdminDivisionAliasMapper adminDivisionAliasMapper;

    @Override
    public void insert(AdminDivisionAlias adminDivisionAlias) {
        adminDivisionAliasMapper.insert(adminDivisionAlias);

    }

    @Override
    public List<AdminDivisionAlias> getADAliasByAdId(Integer adId) {
        return adminDivisionAliasMapper.getADAliasByAdId(adId);
    }
}
