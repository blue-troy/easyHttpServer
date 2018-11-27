package com.bluetroy.servlet.utils;

import com.bluetroy.httpservice.http.request.Request;

public class RequestUtil {
    public static boolean isStaticFileRequest(Request request) {
        String uri = request.getHeader().getURI();
        if (uri.endsWith(".png") || uri.endsWith(".txt")) return true;
        return false;
    }
}
