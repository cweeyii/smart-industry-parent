package com.cweeyii.rabbitmq.framework.sender.impl;


import com.cweeyii.rabbitmq.framework.sender.MQProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class MQProducerServiceImpl implements MQProducerService {
    @Resource
    private AmqpTemplate amqpTemplate;

    private final static Logger LOGGER = LoggerFactory.getLogger(MQProducerServiceImpl.class);


    @Override
    @Transactional
    public void sendDataToQueue(String exchange, String queueKey, Object object) {
        try {
            amqpTemplate.convertAndSend(exchange, queueKey, object);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

    }

    @Override
    @Transactional
    public void sendDataToQueue(String queueKey, Object object) {
        try {
            amqpTemplate.convertAndSend( queueKey, object);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
}