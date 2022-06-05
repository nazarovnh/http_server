package it.sevenbits.httpserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String statusLine;
    private Map<String, String> headers = new HashMap<>();
    private String messageBody = null;
    private final String CRLF = "\n\r";

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public HttpResponse() {
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(String statusLine) {
        this.statusLine = statusLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }


    public String print() {
        StringBuilder headerString = new StringBuilder();
        headers.forEach((key, value) -> {
            headerString.append(key + ": " + value + "\n");
        });
        return statusLine + headerString + CRLF + CRLF + messageBody;
    }
}
