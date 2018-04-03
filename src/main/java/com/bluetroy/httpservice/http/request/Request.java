package com.bluetroy.httpservice.http.request;

public class Request {

    private final RequestHeader header;
    private final RequestBody body;

    public Request(RequestHeader header, RequestBody body) {
        this.header = header;
        this.body = body;
    }


    public RequestHeader getHeader() {
        return header;
    }

    public RequestBody getBody() {
        return body;
    }

}
