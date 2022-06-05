package it.sevenbits.httpserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method = null;
    private String requestTarget = null;
    private String httpVersion = null;
    private Map<String, String> queryString = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();

    public HttpRequest() {
    }

    public String getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void addQuery(final String key, final String value) {
        queryString.put(key, value);
    }

    public void printQuery() {
        queryString.forEach((key, value) -> {
            System.out.println(key + "=" + value);
        });
    }

    public void parseHeaders(final String stringHeaders) {
        String[] headers = stringHeaders.split("\r\n");
        for (String header :
                headers) {
            String[] keyValue = header.split(": ");
            this.headers.put(keyValue[0], keyValue.length == 2 ? keyValue[1] : "");
        }
    }

    public void printHeaders() {
        headers.forEach((key, value) -> {
            System.out.println(key + "=" + value);
        });
    }
}

