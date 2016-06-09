/*
 * Copyright (c) 2010-2011 meituan.com
 * All rights reserved.
 * 
 */
package com.cweeyii.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cweeyii.quartz.framework.domain.QRTZJobResult;
import com.cweeyii.quartz.framework.domain.QuartzJobOperator;
import com.cweeyii.quartz.framework.vo.JobInfo;
import com.cweeyii.quartz.framework.vo.JobNameInfo;
import com.cweeyii.quartz.framework.vo.TriggerInfo;
import com.cweeyii.quartz.framework.vo.TriggerNameInfo;
import com.cweeyii.util.DateUtil;
import com.cweeyii.util.PoiUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/quartzTask")
public class QuartzTaskController {
    @Resource
    private QuartzJobOperator quartzJobOperator;

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzTaskController.class);


    /*
    ====================================================================================
    jobName=addressClearJobDetail
    triggerName=associateMenuFrontImgJobCronTrigger
    ====================================================================================
    表：QRTZ_CRON_TRIGGERS
    SCHED_NAME: quartzScheduler
    TRIGGER_NAME: advertisersPoiAutoProcessJobCronTrigger
    TRIGGER_GROUP: DEFAULT
    CRON_EXPRESSION: 0 0 4 * * ?
    TIME_ZONE_ID: GMT+08:00

    表：QRTZ_JOB_DETAILS
    SCHED_NAME: quartzScheduler
    JOB_NAME: addressClearJobDetail
    JOB_GROUP: DEFAULT
    DESCRIPTION: 地址规范化--地址清洗任务
    JOB_CLASS_NAME: com.sankuai.meituan.poiop.quartz.job.timing.accurate.AddressClearJob
    IS_DURABLE: 1
    IS_NONCONCURRENT: 1
    IS_UPDATE_DATA: 0
    REQUESTS_RECOVERY: 1
    JOB_DATA: ¬í sr org.quartz.JobDataMap°迩°Ë  xr &org.quartz.utils.StringKeyDirtyFlagMapèÃûÅ]( Z allowsTransientDataxr org.quartz.utils.DirtyFlagMapæ.­(v
    Î Z dirtyL mapt Ljava/util/Map;xp sr java.util.HashMapÚÁÃ`Ñ F
    loadFactorI 	thresholdxp?@            x

    表：QRTZ_JOB_RESULT
    id: 794161
    job_name: timeoutClearForBJobDetail
    trigger_name: timeoutClearForBJobCronTrigger
    start_time: 1456578720013
    result: 1
    end_time: 1456578720018
    host_name: dx-mdc-poi-task03.dx.sankuai.com
    */


    /**
     * 得到任务信息
     * @param search 过滤参数
     * @return 返回任务信息JsonMap
     */
    @RequestMapping(value = "/getAllTasks", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getAllTasks(String search) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        resultMap.put("msg", "");
        List<JobInfo> jobInfoList = quartzJobOperator.search(search);
        if (CollectionUtils.isEmpty(jobInfoList)) {
            resultMap.put("status", -1);
            resultMap.put("msg", "不存在jobs");
            return resultMap;
        }
        JSONArray resultData = new JSONArray();
        for (JobInfo li : jobInfoList) {
            //额，这里不规范，但是与前端已经定义好了，下次注意，类定义规范
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("JobName", li.getJobName());
            jsonObject.put("Description", li.getDescription());
            jsonObject.put("TriggerName", li.getTriggerName());
            jsonObject.put("beanName", li.getBeanName());
            jsonObject.put("StartTime", "");
            jsonObject.put("EndTime", "");
            jsonObject.put("cronEx", li.getCronEx());
            jsonObject.put("state", li.getTriggerState());
            resultData.add(jsonObject);
        }
        resultMap.put("msg", "成功");
        resultMap.put("data", resultData);
        return resultMap;
    }

    /**
     *  得到一个job的最近七条执行日志
     * @param jobName 实际是jobName
     * @return 任务执行情况列表
     */
    @RequestMapping(value = "/getTasksLog", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getTasksLog(String jobName) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        resultMap.put("msg", "");
        List<QRTZJobResult> listQRTZJobResult;

        listQRTZJobResult = quartzJobOperator.getTaskRunningLogsByJobName(jobName);
        if ( CollectionUtils.isEmpty(listQRTZJobResult)) {
            resultMap.put("status", -1);
            resultMap.put("msg", " 不存在任务纪录");
            return resultMap;
        }
        JSONArray resultJsArr = new JSONArray();

        for (QRTZJobResult jobResult : listQRTZJobResult) {
            JSONObject resultJSONObject = parseReObj2JSonObject(jobResult);
            resultJsArr.add(resultJSONObject);
        }
        resultMap.put("msg", "成功");
        resultMap.put("data", resultJsArr);
        return resultMap;
    }

    /**
     *  将JavaResult对象转化成JsonObject
     * @param result
     * @return
     */
    private JSONObject parseReObj2JSonObject(QRTZJobResult result) {
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("id", result.getId());
        resultJSONObject.put("jobName", result.getJobName());
        resultJSONObject.put("triggerName", result.getTriggerName());
        if (result.getStartTime() == null || result.getStartTime() == 0L)
            resultJSONObject.put("StartTime", "");
        else {
            String dateTime= DateUtil.Date2String(DateUtil.fromUnixTime((int) (result.getStartTime() / 1000L)), DateUtil.DefaultLongFormat);
            resultJSONObject.put("StartTime", dateTime);
            //resultJSONObject.put("StartTime", parseLongDate2String(result.getStartTime()));
        }
        if (result.getEndTime() == null || result.getEndTime() == 0L)
            resultJSONObject.put("endTime", "");
        else {
           String dateTime=DateUtil.Date2String(DateUtil.fromUnixTime((int) (result.getEndTime() / 1000L)), DateUtil.DefaultLongFormat);
           resultJSONObject.put("endTime", dateTime);
            //resultJSONObject.put("endTime", parseLongDate2String(result.getEndTime()));
        }

        if (result.getStartTime() != 0 && result.getEndTime() != 0) {
            resultJSONObject.put("runTime",Long.toString((result.getEndTime() - result.getStartTime())/1000));
        } else {
            resultJSONObject.put("runTime", "");
        }
        resultJSONObject.put("hostName", result.getHostName());
        resultJSONObject.put("result", parserResult2String(result.getResult()));

        return resultJSONObject;
    }

    /**
     * 转化result状态
     * @param resultStates
     * @return 转化后状态
     */
    private String parserResult2String(int resultStates) {
        String triggerState;

        if (resultStates == 1) {
            triggerState = "成功";
        } else if (resultStates == 2) {
            triggerState = "失败";
        } else if (resultStates == 3) {
            triggerState = "被中断";
        } else if (resultStates == 0) {
            triggerState = "运行中";
        } else {
            triggerState = "";
        }

        return triggerState;
    }


    /**
     * 立即执行一个job
     * @return 执行状态JSon map返回
     */
    @RequestMapping(value = "/startJob", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> startJob(HttpServletRequest request) throws IOException {
        String content = PoiUtil.read(request.getReader(), request.getContentLength());
        JSONObject jsonObject = JSON.parseObject(content);
        String jobName = jsonObject.getString("jobName");
        if (StringUtils.isEmpty(jobName)) {
            throw new IllegalArgumentException("jobName should not be empty");
        }
        Object params = jsonObject.get("params");
        String paramsJson = params == null ? null : String.valueOf(params);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        resultMap.put("msg", "");
        try {
            switch (quartzJobOperator.startJob(jobName, paramsJson)) {
                case -3:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "这个Job已经在运行");
                    break;
                case -2:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "不存在这个Job");
                    break;
                case -1:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "开始Job创建失败");
                    break;
                case 1:
                    resultMap.put("status", 1);
                    resultMap.put("msg", "Job开始执行");
                    break;
                default:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "异常");
            }
        } catch (Exception e) {
            resultMap.put("status", -1);
            resultMap.put("msg", "异常");
            LOGGER.warn("立即执行job异常[jobName=" + jobName + "]", e);
        }
        return resultMap;
    }

    /**
     * 删除任务触发器
     * @param triggerNameInfo
     * @return 删除状态
     */
    @RequestMapping(value = "/deleteTrigger", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> deleteTrigger(@RequestBody TriggerNameInfo triggerNameInfo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        resultMap.put("msg", "");
        try {
            switch (quartzJobOperator.deleteTrigger(triggerNameInfo.getTriggerName())) {
                case 1:
                    resultMap.put("msg", "删除成功");
                    break;
                case -1:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "触发器不存在");
                    break;
                case -2:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "删除异常");
                    break;
                default:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "ERROR");
            }
        } catch (Exception e) {
            resultMap.put("status", -1);
            resultMap.put("msg", "ERROR");
            LOGGER.error("删除触发器异常[tirrgerName" + triggerNameInfo.getTriggerName() + "]", e);
        }
        return resultMap;
    }

    /**
     * 中止一个正在运行的job
     * @param jobNameInfo
     * @return 删除状态
     */
    @RequestMapping(value = "/quitJob", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> quitJob(@RequestBody JobNameInfo jobNameInfo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        if(jobNameInfo.getJobName()==null){
            resultMap.put("status",-1);
            resultMap.put("msg", "参数错误");
            return resultMap;
        }
        resultMap.put("msg", "终止任务成功");
        try {
            if(quartzJobOperator.quitJob(jobNameInfo.getJobName())){
                resultMap.put("msg","任务已经被停止");
            }else {
                resultMap.put("msg","任务不能被停止");
            }
        } catch (UnableToInterruptJobException e) {
            resultMap.put("status", -1);
            resultMap.put("msg", "ERROR");
            LOGGER.error("中断任务出错[jobName" + jobNameInfo.getJobName() + "]", e);
        }
        return resultMap;
    }

    /**
     *
     * @param triggerInfo
     * @return 返回修改状态
     */
    @RequestMapping(value = "/addTrigger", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addTrigger(@RequestBody TriggerInfo triggerInfo) {
    	//改成发个消息
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        resultMap.put("msg", "");
        if(triggerInfo.getJobName()==null)
        {
            resultMap.put("status", -1);
            resultMap.put("msg", "不存在这个Job");
            return  resultMap;
        }
        try {
            switch (quartzJobOperator.saveOrupdateTrigger(triggerInfo.getJobName(), triggerInfo.getCronEx())) {
                case 1:
                    resultMap.put("msg", "添加触发器成功");
                    break;
                case -1:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "不存在这个Job");
                    break;
                case -2:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "触发器名称为kong");
                    break;
                case -3:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "没有这个Job");
                    break;
                case -4:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "CronEx为空");
                    break;
                case -5:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "cronEx不合法");
                    break;
                case -6:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "创建异常");
                    break;
                default:
                    resultMap.put("status", -1);
                    resultMap.put("msg", "ERROR");
            }
        } catch (Exception e) {
            resultMap.put("status", -1);
            resultMap.put("msg", "ERROR");
            LOGGER.error("修改触发器异常[jobName"+triggerInfo.getJobName()+"]", e);
        }

        return resultMap;
    }

}
