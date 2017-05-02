package com.cweeyii.io.blocking;

import com.cweeyii.io.ServerUtil;
import com.cweeyii.threadpool.BasicThreadPoolFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;

public class BlockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockService.class);

    private int port;
    private ExecutorService executorService = BasicThreadPoolFactory.newThreadPoolExecutor("blockingService", 4, 10);

    public BlockService(int port) {
        this.port = port;
    }

    public void accept() throws IOException {
        ServerSocket ss = new ServerSocket(port);
        while (!Thread.interrupted()) {
            Socket clientSocket = ss.accept();
            executorService.submit(new MessageHandler(clientSocket));
        }
    }


    private class MessageHandler implements Runnable {
        private final Socket socket;
        private final static int MAX_INPUT = 1024;

        MessageHandler(Socket s) {
            socket = s;
        }

        public void run() {
            try {
                while (true) {
                    String threadName = Thread.currentThread().getName();
                    Date nowDate = new Date();
                    String output = threadName + " " + nowDate + " to client";
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(output.getBytes());
                    byte[] input = new byte[MAX_INPUT];
                    InputStream inputStream = socket.getInputStream();
                    int count = inputStream.read(input);
                    String received = new String(input, 0, count);
                    LOGGER.info("accept message: " + threadName + " " + nowDate + " " + received + " from client");
                }
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(),ex);
                throw new IllegalArgumentException(ex.getMessage());
            } finally {
                IOUtils.closeQuietly(socket);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BlockService blockService = new BlockService(ServerUtil.serverPort);
        blockService.accept();
    }
}  