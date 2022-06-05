package it.sevenbits.httpserver.core;

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

    @Override
    public void run() {
        try {
            try (OutputStream out = clientSocket.getOutputStream();
                 InputStream in = clientSocket.getInputStream()) {
                HttpRequest httpRequest = httpParser.parseHttpRequest(in);
                HttpResponse httpResponse = httpGenerateResponse.generateResponse(httpRequest);
                //String html = "<html><head><title>WOW</title></head><body><h1>BUM</h1></body></html>";
                //String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length: " + html.getBytes().length + CRLF + CRLF + html + CRLF + CRLF;
                out.write(httpResponse.print().getBytes());
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
