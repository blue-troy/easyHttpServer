package com.bluetroy.httpservice.http.response.impl;

import com.bluetroy.httpservice.http.Status;
import com.bluetroy.httpservice.http.response.Response;
import com.google.gson.Gson;


public class JsonResponse extends Response {

    public JsonResponse(Status status, Object object) {
        super(status);
        heads.put("Content-Type", "application/json; charset=" + DEFAULT_CHARSET);
        content = new Gson().toJson(object).getBytes();
    }
}
