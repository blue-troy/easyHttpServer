package com.bluetroy.httpservice.http.request;


import com.bluetroy.httpservice.http.HttpHeader;

import java.util.Map;

public class RequestHeader {
    private String method;
    private String URI; //请求行
    private Map<String, String> head;
    private Map<String, String> queryMap;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public Map<String, String> getHead() {
        return head;
    }

    public void setHead(Map<String, String> head) {
        this.head = head;
    }

    public Map<String, String> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public String getContentType() {
        return head.get(HttpHeader.CONTENT_TYPE.toLowerName());
    }
}
