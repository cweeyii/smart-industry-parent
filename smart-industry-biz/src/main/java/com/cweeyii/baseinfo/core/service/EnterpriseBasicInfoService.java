package com.cweeyii.baseinfo.core.service;

import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public interface EnterpriseBasicInfoService {

    /**
     * @param id
     * @return
     */
    EnterpriseBasicInfo findById(Long id);

    List<EnterpriseBasicInfo> findByIds(List<Long> ids);

    Map<Long, EnterpriseBasicInfo> findMapByIds(List<Long> unCachedIds);

    List<EnterpriseBasicInfo> findByPage(Long offset, Integer limit);
}

