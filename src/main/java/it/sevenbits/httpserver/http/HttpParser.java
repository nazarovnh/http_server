package it.sevenbits.httpserver.http;

import it.sevenbits.httpserver.core.ServerListenerThread;
import it.sevenbits.httpserver.core.exception.HttpException;
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

    /**
     * Parse http request.
     * First of all parse a request line, after headers of http request.
     *
     * @param inputStream - inputStream
     * @return HttpRequest
     */
    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpException {
        HttpRequest httpRequest = new HttpRequest();
        try {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
            parseRequestLine(reader, httpRequest);
            parseHeaders(reader, httpRequest);
            validate(httpRequest);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return httpRequest;
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
    private void parseRequestLine(InputStreamReader reader, HttpRequest httpRequest) throws IOException, HttpException {
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
                            logger.info("Request http header: \n{}", httpRequest.printHeaders());
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

    /**
     * Parse request target for get a query string.
     *
     * @param requestTarget - requestTarget
     * @param httpRequest   - httpRequest
     */
    private void parseRequestTarget(String requestTarget, HttpRequest httpRequest) {
        String[] uri = requestTarget.split("\\?");
        httpRequest.setURI(uri[0]);
        if (uri.length == 2) {
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

    private void validate(HttpRequest request) throws HttpException {
        if (!Objects.equals(request.getMethod(), "GET")) {
            HttpException e = new HttpException("Method Not Allowed", "405");
            e.setHttpVersion(request.getHttpVersion());
            throw e;
        } else if (!Objects.equals(request.getURI(), "/hello")) {
            HttpException e = new HttpException("Not Found", "404");
            e.setHttpVersion(request.getHttpVersion());
            throw e;
        } else if (!request.isContainsQueryName("name")) {
            HttpException e = new HttpException("Bad Request", "400");
            e.setHttpVersion(request.getHttpVersion());
            throw e;
        }

    }
}
