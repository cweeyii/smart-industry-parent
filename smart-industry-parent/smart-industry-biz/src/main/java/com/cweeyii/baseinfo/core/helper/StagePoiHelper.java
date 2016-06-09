package com.cweeyii.baseinfo.core.helper;


import org.apache.commons.lang3.time.DateUtils;
import com.cweeyii.baseinfo.core.domain.StagePoi;
import java.util.Date;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public class StagePoiHelper {

    public static boolean onlyModifyCloseStatus(StagePoi src, StagePoi dst){
        if (!dst.getAddress().equals(src.getAddress())) {
            return false;
        }
        if (!dst.getBareaId().equals(src.getBareaId())) {
            return false;
        }
        if (!dst.getLocationId().equals(src.getLocationId())) {
            return false;
        }
        if (!dst.getLatitude().equals(src.getLatitude())
                || !dst.getLongitude().equals(src.getLongitude())) {
            return false;
        }
        if (!dst.getPhone().equals(src.getPhone())) {
            return false;
        }
        if (!dst.getPointName().equals(src.getPointName())) {
            return false;
        }
        if (!dst.getTypeId().equals(src.getTypeId())) {
            return false;
        }
        if (dst.getCloseStatus() != src.getCloseStatus()){
            return true;
        }
        //如果只修改了预定营业时间，也认为营业状态没有修改
        if (dst.getOpenDate() !=src.getOpenDate()){
            return false;
        }
        return false;
    }



    /**
     * 默认的openDate为两个月后
     * @return
     */
    public static Integer getDefaultOpenDate(){

        Date openTime = DateUtils.addMonths(new Date(), 2);

        return (int)(openTime.getTime()/1000);
    }

}

