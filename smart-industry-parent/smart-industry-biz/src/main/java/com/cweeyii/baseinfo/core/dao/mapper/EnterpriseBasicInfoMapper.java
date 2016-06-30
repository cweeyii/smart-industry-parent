package com.cweeyii.baseinfo.core.dao.mapper;


import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EnterpriseBasicInfoMapper {

    EnterpriseBasicInfo findById(Long id);
    void insert(EnterpriseBasicInfo enterpriseBasicInfo);
    List<EnterpriseBasicInfo> findByIds(List<Long> ids);
    List<EnterpriseBasicInfo> findByPage(@Param("offset")Long offset,@Param("pageSize")Integer pageSize);
}
