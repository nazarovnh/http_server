package it.sevenbits.httpserver.core;

import it.sevenbits.httpserver.core.exception.HttpException;
import it.sevenbits.httpserver.http.HttpGenerateResponse;
import it.sevenbits.httpserver.http.HttpParser;
import it.sevenbits.httpserver.http.HttpRequest;
import it.sevenbits.httpserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;


public class HttpConnectionWorkerThread extends Thread {
    private Socket clientSocket;
    private HttpParser httpParser;
    private HttpGenerateResponse httpGenerateResponse;
    final static Logger logger = LoggerFactory.getLogger(ServerListenerThread.class);


    public HttpConnectionWorkerThread(final Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        httpParser = new HttpParser();
        httpGenerateResponse = new HttpGenerateResponse();
    }

    /**
     * Separate thread for working with one client socket
     */
    @Override
    public void run() {
        try (OutputStream out = clientSocket.getOutputStream();
             InputStream in = clientSocket.getInputStream()) {
            try {
                HttpRequest httpRequest = httpParser.parseHttpRequest(in);
                HttpResponse httpResponse = httpGenerateResponse.generateResponse(httpRequest);
                out.write(httpResponse.print().getBytes());
            } catch (HttpException e) {
                out.write(e.printResponse().getBytes());
            } catch (IOException ioException) {
                logger.error(ioException.getMessage());
            }
            clientSocket.close();
            logger.info("Connection closed");
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
