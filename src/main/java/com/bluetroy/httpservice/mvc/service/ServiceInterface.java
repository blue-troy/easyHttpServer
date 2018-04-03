package com.bluetroy.httpservice.mvc.service;

import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.response.Response;

public interface ServiceInterface {
    Response service(Request request);
}
