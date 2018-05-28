package com.bluetroy.httpservice.http;

import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.response.Response;

public class Http {
    Request request;
    Response response;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
