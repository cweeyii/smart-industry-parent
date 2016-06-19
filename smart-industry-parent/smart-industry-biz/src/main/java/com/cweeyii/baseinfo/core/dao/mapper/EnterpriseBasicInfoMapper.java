package com.cweeyii.baseinfo.core.dao.mapper;


import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;

import java.util.List;


public interface EnterpriseBasicInfoMapper {

    EnterpriseBasicInfo findById(Long id);
    void insert(EnterpriseBasicInfo enterpriseBasicInfo);
    List<EnterpriseBasicInfo> findByIds(List<Long> ids);
}
