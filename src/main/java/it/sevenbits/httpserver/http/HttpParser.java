package it.sevenbits.httpserver.http;

import it.sevenbits.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {
    final static Logger logger = LoggerFactory.getLogger(HttpParser.class);
    private static final int SP = 32;// 32 in ASCII
    private static final int CR = 13;// 13 in ASCII
    private static final int LF = 10;// 10 in ASCII

    public void parseHttpRequest(InputStream inputStream) {
        try {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
            HttpRequest httpRequest = new HttpRequest();
            parseRequestLine(reader, httpRequest);
//            parseHeaders(reader, httpRequest);
//            parseBody(reader, httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse request line form http request.
     * Notice: How request line looks in start line http request is
     * Method SP Request Target(URI) SP Http Version CRLF
     *
     * @param reader
     * @param httpRequest
     * @throws IOException
     */
    private void parseRequestLine(InputStreamReader reader, HttpRequest httpRequest) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int _byte;
        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    logger.info("Request line version: {}", stringBuilder.toString());
                    return;
                }
            }
            if (_byte == SP) {
                logger.info("Request line : {}", stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.length());
            } else {
                stringBuilder.append((char) _byte);
            }
        }

    }

    private void parseHeaders(InputStreamReader reader, HttpRequest httpRequest) {
    }

    private void parseBody(InputStreamReader reader, HttpRequest httpRequest) {
    }

}
