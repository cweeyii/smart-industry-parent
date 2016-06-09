package com.cweeyii.baseinfo.core.service;

import com.cweeyii.baseinfo.core.domain.StagePoi;

import java.util.List;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public interface StagePoiService {

    /**
     * @param id
     * @return
     */
    StagePoi findById(Long id);

    List<StagePoi> findByIds(List<Long> ids);

}

