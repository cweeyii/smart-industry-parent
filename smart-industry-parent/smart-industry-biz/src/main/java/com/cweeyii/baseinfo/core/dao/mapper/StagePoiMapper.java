package com.cweeyii.baseinfo.core.dao.mapper;


import com.cweeyii.baseinfo.core.domain.StagePoi;

import java.util.List;


public interface StagePoiMapper {

    StagePoi findById(Long id);

    void insert(StagePoi stagePoi);

    List<StagePoi> findByIds(List<Long> ids);
}
