package com.cweeyii.quartz.job.mq;

import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;
import com.cweeyii.baseinfo.core.service.EnterpriseBasicInfoService;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.rabbitmq.framework.sender.MQProducerService;
import com.cweeyii.rabbitmq.framework.vo.InternalMessageFactory;
import com.google.common.collect.Lists;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cweeyii on 9/6/16.
 */
@Service
public class FirstRabbitMqJob extends AbstractQuartzJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstRabbitMqJob.class);
    @Resource
    private MQProducerService mqProducerService;
    @Resource
    private EnterpriseBasicInfoService enterpriseBasicInfoService;


    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        //生产者发送消息
        List<EnterpriseBasicInfo> basicInfos = enterpriseBasicInfoService.findByIds(Lists.newArrayList(11L, 63L, 65L, 71L, 73L, 83L, 85L, 91L, 93L));
        for (EnterpriseBasicInfo basicInfo : basicInfos) {
            mqProducerService.sendDataToQueue(InternalMessageFactory.SendMessage.FIRST_POIOP_MESSAGE.getExchangeName(),
                    InternalMessageFactory.SendMessage.FIRST_POIOP_MESSAGE.getTopicName(), basicInfo);
        }
        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "第一個MQ定時任務";
    }
}
