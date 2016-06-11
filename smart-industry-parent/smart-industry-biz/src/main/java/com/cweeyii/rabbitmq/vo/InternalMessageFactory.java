package com.cweeyii.rabbitmq.vo;

/**
 * Created by cweeyii on 9/6/16.
 */
public class InternalMessageFactory {
    private InternalMessageFactory() {
    }

    public enum SendMessage {

        FIRST_MART_MESSAGE("test-mq-exchange", "rabbit.topic.mart"),
        FIRST_POIOP_MESSAGE("test-mq-exchange", "rabbit.topic.poiop"),
        FIRST_GIS_MESSAGE("test-mq-exchange", "rabbit.topic.gis");

        SendMessage(String exchangeName, String topicName) {
            this.exchangeName = exchangeName;
            this.topicName = topicName;
        }

        private String exchangeName;
        private String topicName;

        public String getExchangeName() {
            return exchangeName;
        }

        public String getTopicName() {
            return topicName;
        }

    }
}
