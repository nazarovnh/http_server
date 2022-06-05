package it.sevenbits.httpserver.http;

import it.sevenbits.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
            parseHeaders(reader, httpRequest);
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
        boolean isRequestTargetParred = false;
        boolean isMethodParsed = false;

        int _byte;
        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    logger.info("Request http version: {}", stringBuilder);
                    httpRequest.setHttpVersion(stringBuilder.toString());
                    return;
                }
            }
            if (_byte == SP) {
                if (!isMethodParsed) {
                    logger.info("Request method : {}", stringBuilder);
                    isMethodParsed = true;
                    httpRequest.setMethod(stringBuilder.toString());
                } else if (!isRequestTargetParred) {
                    logger.info("Request target: {}", stringBuilder);
                    isRequestTargetParred = true;
                    httpRequest.setRequestTarget(stringBuilder.toString());
                    parseRequestTarget(stringBuilder.toString(), httpRequest);
                }
                stringBuilder.delete(0, stringBuilder.length());
            } else {
                stringBuilder.append((char) _byte);
            }
        }

    }

    /**
     * Parsed http headers
     * Notice: How http request looks in start line http request
     * Header CRLF or only CRLF
     *
     * @param reader
     * @param httpRequest
     * @throws IOException
     */
    private void parseHeaders(InputStreamReader reader, HttpRequest httpRequest) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int _byte = reader.read();
        if (_byte != CR) {
            while (_byte >= 0) {
                if (_byte == CR) {
                    _byte = reader.read();
                    if (_byte == LF) {
                        _byte = reader.read();
                        if (_byte == CR) {
                            httpRequest.parseHeaders(stringBuilder.toString());
                            logger.info("Request http header: \n");
                            httpRequest.printHeaders();
                            return;
                        } else {
                            stringBuilder.append((char) CR);
                            stringBuilder.append((char) LF);
                        }
                    }
                }
                stringBuilder.append((char) _byte);
                _byte = reader.read();
            }
        }
    }

    private void parseBody(InputStreamReader reader, HttpRequest httpRequest) {
    }

    private void parseRequestTarget(String requestTarget, HttpRequest httpRequest) {
        String[] uri = requestTarget.split("\\?");
        if (Objects.equals(uri[0], "/hello") && uri.length == 2) {
            StringBuilder stringBuilders = new StringBuilder(uri[1]);
            stringBuilders.append("&");
            String[] queryString = uri[1].split("&");
            for (String query :
                    queryString) {
                String[] keyValue = query.split("=");
                httpRequest.addQuery(keyValue[0], keyValue.length == 2 ? keyValue[1] : "");
            }
        }
    }
}
