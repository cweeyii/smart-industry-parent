package com.cweeyii.cache.framework.provider;

import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;
import com.cweeyii.baseinfo.core.service.EnterpriseBasicInfoService;
import com.cweeyii.cache.framework.vo.EnterpriseRichnessView;
import com.cweeyii.util.EnterpriseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnterpriseDataRepertoryUnit extends AbstractDataRepertoryUnit<Long, EnterpriseRichnessView> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseDataRepertoryUnit.class);

    @Resource
    private EnterpriseBasicInfoService enterpriseBasicInfoService;

    @Override
    public Map<Long, EnterpriseRichnessView> getObjectFromDB(List<Long> keys) {
        Map<Long, EnterpriseRichnessView> enterpriseRichnessViewMap = new HashMap<>();
        Map<Long, EnterpriseBasicInfo> enterpriseBasicInfoMap = enterpriseBasicInfoService.findMapByIds(keys);
        for (Long key : keys) {
            EnterpriseRichnessView enterpriseRichnessView = new EnterpriseRichnessView();
            if (enterpriseBasicInfoMap.containsKey(key)) {
                EnterpriseBasicInfo enterpriseBasicInfo = enterpriseBasicInfoMap.get(key);
                EnterpriseUtil.copyProperties(enterpriseBasicInfo, enterpriseRichnessView);
                enterpriseRichnessViewMap.put(key, enterpriseRichnessView);
            } else {
                LOGGER.warn("数据库不存在这样的记录key=" + key);
            }
        }
        return enterpriseRichnessViewMap;
    }
}
