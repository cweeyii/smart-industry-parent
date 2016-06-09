package com.cweeyii.quartz.framework.dao.mapper;


import com.cweeyii.quartz.framework.vo.CronTriggerVo;

import java.util.List;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public interface CronTriggerMapper {
    
    List<CronTriggerVo> getAllCronTriggers();
}
