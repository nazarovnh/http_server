package it.sevenbits.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {
    private int port;
    final static Logger logger = LoggerFactory.getLogger(ServerListenerThread.class);

    public ServerListenerThread(final int port) throws IOException {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Connection accepted" + clientSocket.getInetAddress());
                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(clientSocket);
                workerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Server down");
    }
}
