package com.bluetroy.httpservice.mvc;

import com.bluetroy.httpservice.http.Http;
import com.bluetroy.httpservice.http.request.Request;

public class DispatcherServlet {
    public static Service dispatch(Http http) {
        Request request = http.getRequest();
        return ServiceRegistry.findService(request.getHeader().getURI());
    }
}
