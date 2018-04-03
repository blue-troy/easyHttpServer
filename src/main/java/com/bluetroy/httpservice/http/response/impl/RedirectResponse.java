package com.bluetroy.httpservice.http.response.impl;


import com.bluetroy.httpservice.http.Status;
import com.bluetroy.httpservice.http.response.Response;

public class RedirectResponse  extends Response {

    public RedirectResponse(String url) {
        super(Status.TEMPORARILY_MOVED);
        if (url == null || url.isEmpty()) url = "https://github.com/blue-troy/404";
        heads.put("Location", url);
    }
}
