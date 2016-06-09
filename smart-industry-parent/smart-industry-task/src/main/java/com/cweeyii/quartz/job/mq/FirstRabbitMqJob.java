package com.cweeyii.quartz.job.mq;

import com.cweeyii.baseinfo.core.domain.StagePoi;
import com.cweeyii.baseinfo.core.service.StagePoiService;
import com.cweeyii.gis.domain.AdminDivisionAlias;
import com.cweeyii.gis.service.AdminDivisionAliasService;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.rabbitmq.sender.MQProducerService;
import com.cweeyii.rabbitmq.vo.InternalMessageFactory;
import com.cweeyii.richness.domain.TradePoi;
import com.cweeyii.richness.service.TradePoiService;
import org.elasticsearch.common.collect.Lists;
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
    private StagePoiService stagePoiService;
    @Resource
    private TradePoiService tradePoiService;
    @Resource
    private AdminDivisionAliasService adminDivisionAliasService;


    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        //生产者发送消息
        List<StagePoi> poiList = stagePoiService.findByIds(Lists.newArrayList(11L, 63L, 65L, 71L, 73L, 83L, 85L, 91L, 93L));
        for (StagePoi poi : poiList) {
            mqProducerService.sendDataToQueue(InternalMessageFactory.SendMessage.FIRST_POIOP_MESSAGE.getExchangeName(),
                    InternalMessageFactory.SendMessage.FIRST_POIOP_MESSAGE.getTopicName(), poi);
        }

        List<TradePoi> tradePois = tradePoiService.findByPoiId(11L);
        for (TradePoi tradePoi : tradePois) {
            mqProducerService.sendDataToQueue(InternalMessageFactory.SendMessage.FIRST_MART_MESSAGE.getExchangeName(),
                    InternalMessageFactory.SendMessage.FIRST_MART_MESSAGE.getTopicName(), tradePoi);
        }

        List<AdminDivisionAlias> adminDivisionAliases = adminDivisionAliasService.getADAliasByAdId(10154838);
        for (AdminDivisionAlias adminDivisionAlias : adminDivisionAliases) {
            mqProducerService.sendDataToQueue(InternalMessageFactory.SendMessage.FIRST_GIS_MESSAGE.getExchangeName(),
                    InternalMessageFactory.SendMessage.FIRST_GIS_MESSAGE.getTopicName(), adminDivisionAlias);
        }


        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "第一個MQ定時任務";
    }
}
