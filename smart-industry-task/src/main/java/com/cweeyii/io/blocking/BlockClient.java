package com.cweeyii.io.blocking;

import com.cweeyii.io.ServerUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * Created by wenyi on 17/4/14.
 * Email:caowenyi@meituan.com
 */
public class BlockClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockClient.class);
    private int remotePort;
    private String remoteIp;
    private Socket socket;
    private final static int MAX_INPUT = 1024;

    public BlockClient(int remotePort, String remoteIp) {
        this.remotePort = remotePort;
        this.remoteIp = remoteIp;
    }

    public void process() throws IOException {
        try {
            socket = new Socket(remoteIp, remotePort);
            while (true) {
                String threadName = Thread.currentThread().getName();
                Date nowDate = new Date();

                InputStream inputStream = socket.getInputStream();
                byte[] input = new byte[MAX_INPUT];
                int count = inputStream.read(input);
                String inputMessage = new String(input, 0, count);
                LOGGER.info("accept message: " + threadName + " " + nowDate + " " + inputMessage + " from service");

                OutputStream outputStream = socket.getOutputStream();
                String sendMessage = threadName + " " + nowDate + " to server";
                outputStream.write(sendMessage.getBytes());
            }
        } finally {
            IOUtils.closeQuietly(socket);
        }
    }

    public static void main(String[] args) throws IOException {
        BlockClient client = new BlockClient(ServerUtil.serverPort, ServerUtil.serviceIp);
        client.process();
    }
}

