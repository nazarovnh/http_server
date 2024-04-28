package it.sevenbits.httpserver.core.exception;

public class HttpException extends Exception {
    private String message;
    private String status;
    private String httpVersion;
    private static final char SP = ' ';// 32 in ASCII
    private static final String CRLF = "\r\n";


    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HttpException(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public HttpException(String s, String message, String status) {
        super(s);
        this.message = message;
        this.status = status;
    }

    public HttpException(String s, Throwable throwable, String message, String status) {
        super(s, throwable);
        this.message = message;
        this.status = status;
    }

    public HttpException(Throwable throwable, String message, String status) {
        super(throwable);
        this.message = message;
        this.status = status;
    }

    public HttpException(String s, Throwable throwable, boolean b, boolean b1, String message, String status) {
        super(s, throwable, b, b1);
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String printResponse() {
        return httpVersion + SP + status + SP + message + CRLF + CRLF;
    }
}
