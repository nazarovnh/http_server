package it.sevenbits.http.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {
    private int port;
    private ServerSocket serverSocket;
    final static Logger logger = LoggerFactory.getLogger(ServerListenerThread.class);

    public ServerListenerThread(final int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();

                logger.info("Connection accepted" + clientSocket.getInetAddress());
                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(clientSocket);
                workerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
