package com.cweeyii.baseinfo.core.service.impl;

import com.cweeyii.baseinfo.core.dao.mapper.StagePoiMapper;
import com.cweeyii.baseinfo.core.domain.StagePoi;
import com.cweeyii.baseinfo.core.service.StagePoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;


/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
@Service
public class StagePoiServiceImpl implements StagePoiService {
    private static final Logger log = LoggerFactory.getLogger(StagePoiServiceImpl.class);

    @Resource
    private StagePoiMapper stagePoiMapper;


    @Override
    public StagePoi findById(Long id) {
        return stagePoiMapper.findById(id);
    }

    @Override
    public List<StagePoi> findByIds(List<Long> ids) {
        return stagePoiMapper.findByIds(ids);
    }

}

