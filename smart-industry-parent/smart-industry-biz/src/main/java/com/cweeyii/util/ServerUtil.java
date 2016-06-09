package com.cweeyii.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public class ServerUtil {

    private static final Logger log = LoggerFactory.getLogger(ServerUtil.class);


    private ServerUtil (){

    }

    private static  String HOST_NAME = null;

    private static void loadHostName(){
        if (HOST_NAME == null) {
            synchronized (PoiUtil.class) {
                if (HOST_NAME == null) {
                    try {
                        HOST_NAME =  InetAddress.getLocalHost().getHostName();
                    } catch (UnknownHostException e) {
                        log.error("无法获得host name !", e);
                    }
                }
            }
        }
    }

    public static String getHostName(){
        loadHostName();
        return HOST_NAME;
    }

    /**
     * 判断是否任务机器
     * */
    public static boolean isTaskServer(){
        return true;
    }

    public static String[] getTaskServers(){
        String serverLine="WenYiCaos-MacBook-Pro.local";
        return serverLine.split(",");
    }



    private static class MqConsumerQueue {

        private String hostName;//主机

        private List<String> queueNames;//消费队列

        private List<String> exclusionQueueNames;//不消费的队列

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public List<String> getQueueNames() {
            return queueNames;
        }

        public void setQueueNames(List<String> queueNames) {
            this.queueNames = queueNames;
        }

        public List<String> getExclusionQueueNames() {
            if (this.exclusionQueueNames == null) {
                exclusionQueueNames = new ArrayList<>();
            }
            return exclusionQueueNames;
        }

        public void setExclusionQueueNames(List<String> exclusionQueueNames) {
            this.exclusionQueueNames = exclusionQueueNames;
        }

    }

    private static MqConsumerQueue buildMqConsumerQueue(String hostName, List<String> queueNames, List<String> exclusionQueueNames) {
        MqConsumerQueue dx_mdc_poi_mq01 = new MqConsumerQueue();
        dx_mdc_poi_mq01.setHostName(hostName);
        dx_mdc_poi_mq01.setQueueNames(queueNames);
        dx_mdc_poi_mq01.setExclusionQueueNames(exclusionQueueNames);
        return dx_mdc_poi_mq01;
    }




}

