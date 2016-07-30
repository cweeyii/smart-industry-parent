package com.cweeyii.rabbitmq.framework.vo;

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

    public enum MessageType{
        POI_MODIFY_BASE,POI_CREATE,POI_CREATE_V2,POI_SCORE,POI_MERGE_ADD,POI_MERGE_CANCEL,POI_IMG,POI_FEEDBACK,POI_MENU,POI_SUBWAY,POI_LANDMARK,POI_SERVE,
        POI_RICH_INFO,POI_MODIFY_CORE,MENU_DETAIL,POI_DEAL_SUM_COUNT,COLLECT_POINT, RAW_POI_CREATE,UPDARE_ES_INDEX, MENU_DETAIL_LIST,
        POI_TAG,POI_ATTR_VALUE,POI_ROOM_BOSS_INFO,POI_ROOM_INFO, BRAND_CREATE, BRAND_UPDATE, POI_CREATE_SOURCE, SM_POI_RELATION, POI_TYPE_AREA_SCORE,
        MTDP_RELATION, DPMT_RELATION, MTDP_MAPPING_RELATION, POI_SHOP_UPDATE, UNIFORM_FEEDBACK,POI_INTERNAL_MTDP_REPAIR,
        POI_IMG_V2, MTDP_POI_OVERWRITE,POI_RICHNESS
    }
}
