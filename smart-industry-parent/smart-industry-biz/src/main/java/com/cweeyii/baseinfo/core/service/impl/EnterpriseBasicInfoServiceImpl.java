package com.cweeyii.baseinfo.core.service.impl;

import com.cweeyii.baseinfo.core.dao.mapper.EnterpriseBasicInfoMapper;
import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;
import com.cweeyii.baseinfo.core.service.EnterpriseBasicInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
@Service
public class EnterpriseBasicInfoServiceImpl implements EnterpriseBasicInfoService {
    private static final Logger log = LoggerFactory.getLogger(EnterpriseBasicInfoServiceImpl.class);

    @Resource
    private EnterpriseBasicInfoMapper enterpriseBasicInfoMapper;


    @Override
    public EnterpriseBasicInfo findById(Long id) {
        return enterpriseBasicInfoMapper.findById(id);
    }

    @Override
    public List<EnterpriseBasicInfo> findByIds(List<Long> ids) {
        return enterpriseBasicInfoMapper.findByIds(ids);
    }

    @Override
    public Map<Long, EnterpriseBasicInfo> findMapByIds(List<Long> ids) {
        Map<Long, EnterpriseBasicInfo> enterpriseBasicInfoMap = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return enterpriseBasicInfoMap;
        }
        List<EnterpriseBasicInfo> enterpriseBasicInfos = enterpriseBasicInfoMapper.findByIds(ids);
        if (enterpriseBasicInfos.isEmpty()) {
            return enterpriseBasicInfoMap;
        }
        for (EnterpriseBasicInfo enterpriseBasicInfo : enterpriseBasicInfos) {
            enterpriseBasicInfoMap.put(enterpriseBasicInfo.getId(), enterpriseBasicInfo);
        }
        return enterpriseBasicInfoMap;
    }

}

