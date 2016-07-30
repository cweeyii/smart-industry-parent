package com.cweeyii.elasticsearch.framework.client;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by wenyi on 16/6/19.
 * Email:caowenyi@meituan.com
 */
public class ESClientConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(ESClientConfig.class);
    private static Client client;

    private ESClientConfig() {
    }

    public synchronized static Client getClient() {
        if (client == null) {

            /*client = new TransportClient(ImmutableSettings.settingsBuilder().put("client.transport.sniff", "true")
                    .put("cluster.name", "wenyi-elasticsearch-cluster").build())
                    .addTransportAddress((new InetSocketTransportAddress("localhost", 9300)));*/
            //client=new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));

            Settings settings = Settings.settingsBuilder()
                    .put("client.transport.sniff", "true")
                    .put("cluster.name", "wenyi-elasticsearch-cluster").build();
            try {
                client = TransportClient.builder().settings(settings).build()
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    public synchronized static void close() {
        if (client != null) {

            client.close();
            client = null;
            LOGGER.info("ElasticSearch Client is closed");
        }
    }
}
