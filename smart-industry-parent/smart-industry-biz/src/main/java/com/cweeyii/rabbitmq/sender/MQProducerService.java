package com.cweeyii.rabbitmq.sender;

public interface MQProducerService {
    /**
     * 发送消息到指定队列
     * @param exchange
     * @param queueKey
     * @param object
     */
    void sendDataToQueue(String exchange, String queueKey, Object object);

    /**
     * 发送消息到指定队列
     * @param queueKey
     * @param object
     */
    void sendDataToQueue(String queueKey, Object object);
}