package com.cweeyii.quartz.framework.dao.mapper;


import com.cweeyii.quartz.framework.domain.QRTZJobResult;

import java.util.List;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public interface QRTZJobResultMapper {

    int insert(QRTZJobResult result);

    int update(QRTZJobResult result);

    List<QRTZJobResult> findLatestResultsByJobName(String jobName,int limit);
}

