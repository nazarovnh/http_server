package it.sevenbits.httpserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpGenerateResponse {
    final static Logger logger = LoggerFactory.getLogger(HttpGenerateResponse.class);
    private static final char SP = ' ';// 32 in ASCII
    private static final char CR = '\r';// 13 in ASCII
    private static final char LF = '\n';// 10 in ASCII
    private final Map<String, String> status = new HashMap<String, String>() {{
        put("200", "OK");
    }};


    /**
     * Generate http response with basic structure
     * Status-Line
     * (( general-header
     *  | response-header
     *  | entity-header ) CRLF)
     * CRLF
     * [ message-body ]
     * @param httpRequest - httpRequest
     * @return HttpResponse
     */
    public HttpResponse generateResponse(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();
        generateStatusLine(httpResponse, httpRequest);
        generateMessageBody(httpResponse, httpRequest);
        generateHeaders(httpResponse, httpRequest);
        return httpResponse;
    }


    private void generateStatusLine(HttpResponse httpResponse, HttpRequest httpRequest) {
        httpResponse.setStatusLine(httpRequest.getHttpVersion() + SP + "200" + SP + status.get("200") + CR + LF);
    }

    private void generateHeaders(HttpResponse httpResponse, HttpRequest httpRequest) {
        httpResponse.addHeader("Content-Type", "text/plain");
        httpResponse.addHeader("Content-Length", String.valueOf(httpResponse.getMessageBody().getBytes().length + 3));
    }


    private void generateMessageBody(HttpResponse httpResponse, HttpRequest httpRequest) {
        httpResponse.setMessageBody("Hello, " + httpRequest.getQuery("name"));
    }


}
