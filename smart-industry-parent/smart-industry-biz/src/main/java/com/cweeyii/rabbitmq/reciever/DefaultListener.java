package com.cweeyii.rabbitmq.reciever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class DefaultListener implements MessageListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultListener.class);

    @Override
    public void onMessage(Message msg) {
        LOGGER.info("接受到消息=" + new String(msg.getBody()));
    }

}