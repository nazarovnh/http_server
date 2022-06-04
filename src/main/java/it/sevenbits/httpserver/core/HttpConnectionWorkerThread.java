package it.sevenbits.httpserver.core;

import it.sevenbits.httpserver.http.HttpParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;


public class HttpConnectionWorkerThread extends Thread {
    private Socket clientSocket;
    private HttpParser httpParser;
    final static Logger logger = LoggerFactory.getLogger(ServerListenerThread.class);
    private final String CRLF = "\n\r";


    public HttpConnectionWorkerThread(final Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        httpParser = new HttpParser();
    }

    @Override
    public void run() {
        try {
            try (OutputStream out = clientSocket.getOutputStream();
                 InputStream in = clientSocket.getInputStream()) {
                httpParser.parseHttpRequest(in);
                String html = "<html><head><title>WOW</title></head><body><h1>BUM</h1></body></html>";
                String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length: " + html.getBytes().length + CRLF + CRLF + html + CRLF + CRLF;
                out.write(response.getBytes());
                clientSocket.close();
                logger.info("Connection closed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}