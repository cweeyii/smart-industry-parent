package com.cweeyii.baseinfo.core.dao.mapper;


import com.cweeyii.baseinfo.core.domain.StagePoi;
import com.cweeyii.util.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther: xiongrui
 * @date: 15-4-23 16:22
 */
public interface StagePoiMapper {

    public StagePoi findById(Long id);
    
    public void update(StagePoi stagePoi);

    public void insert(StagePoi stagePoi);

    public int getCount(List<Long> pois);

    public List<StagePoi> findByTypeIds(List<Integer> typeIds);

    public List<Long> getUnhandleIds(List<Long> pois);

    public List<StagePoi> findBySql(@Param("sql") String sql);

    public List<Long> findIdsByIds(List<Long> ids);

    public List<Long> findIdsByLocationId(Integer locationId);

    public List<Long> findIdsByLocation(Integer location);

    public List<Long> findByMergeId(Long mergeId);

    public List<StagePoi> findEmptyBizArea();

    public List<Long> findToSync(Integer syncStatus);

    public List<Long> findIdsByMergeIds(List<Long> ids);

    public Integer countByBrands(List<String> brands);

    public List<StagePoi> findByBrands(@Param("brands") List<String> brands, @Param("page") Page page);

    public List<Long> findAccurateByPoiIds(List<Long> poiIds);

    public Integer countClosedPois();

    public List<StagePoi> getClosedPois(@Param("page") Page page);

    public Integer getBrandCount(String brandName);

    public Integer getBrandCountMerge(@Param("brandId") Integer brandId, @Param("merge") Boolean merge);

    public Integer getAccurateCountByBrand(Integer brandId);

    public Integer getCityCountByBrand(Integer brandId);

    public List<StagePoi> findMergePoisByIds(List<Long> ids);

    public Integer countByCityLocationId(Integer cityLocationId);

    public List<StagePoi> findByCityLocationId(@Param("cityLocationId") Integer cityLocationId, @Param("page") Page page);

    public Integer countByCityId(Integer cityId);

    public Integer countPoiByAccurateStatus(Integer accurateStatus);

    public List<StagePoi> getPoisByAccurateStatus(@Param("accurateStatus") Integer accurateStatus, @Param("page") Page page);

    public List<StagePoi> findByIdsByOrder(List<Long> poiIds);
    
    public List<Long> findHotelPoiIds();

    public List<StagePoi> findByIds(List<Long> ids);

    public List<Long> findExistIds(List<Long> ids);

    public List<StagePoi> findMergedPois(Long id);

    public List<StagePoi> findByPoiIdsAndPointNameOrder(List<Long> poiIds);

    public List<Long> findMergeIds();

    public List<Map> findAllAccuratePois(@Param("fields") String fields);

    public List<Long> getPoiIdByTimeRange(@Param("beginUnixTime") Integer beginUnixTime, @Param("endUnixTime") Integer endUnixTime);

    public Integer getBaseCategoryIdByPoiId(Long id);

    public List<StagePoi> getPoiByOpenDate(@Param("beginDate") Integer beginDate,
                                           @Param("endDate") Integer endDate,
                                           @Param("closeStatusList") List<Integer> closeStatusList,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    public Integer countPoiByTypeAndCity(@Param("cityLocationId") Integer cityLocationId, @Param("typeId") Integer typeId);

    public List<Long> findPoiIdByTypeAndCity(@Param("cityLocationId") Integer cityLocationId, @Param("typeId") Integer typeId, @Param("page") Page page);

    public Integer countPoiIdByType(Integer typeId);

    public List<Long> findPoiIdByType(@Param("typeId") Integer typeId, @Param("page") Page page);

    public Integer updateWifiAndParking(@Param("stagePoi") StagePoi poi);

    public List<StagePoi> getDuplicateRemoveData();

    int countAllOpenedPoi();

    List<Long> findOpenedPoiIdsByPage(@Param("minPoiId") int minPoiId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    List<Long> findPoiIdsByTypes(List<Integer> typeIds);

    List<StagePoi> findByLocationId(@Param("locationId") Integer locationId);

    int batchInsertOrUpdate(List<StagePoi> pois);

    List<StagePoi> findByTypeId(@Param("typeId") Integer typeId, @Param("modifyTime") Integer modifyTime, @Param("page") Page page);

    List<StagePoi> getUniqueStagePoiList(@Param(value = "offset") Integer offset, @Param(value = "pageSize") Integer pageSize);

    List<StagePoi> findPoisByPage(@Param("minPoiId") Long minPoiId, @Param("offset") int offset, @Param("pageSize") int pageSize);

}
