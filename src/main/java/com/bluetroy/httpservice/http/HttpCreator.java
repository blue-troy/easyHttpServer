package com.bluetroy.httpservice.http;

import com.bluetroy.httpservice.http.request.RequestParser;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class HttpCreator {
    static Http http;

    public static Http create(SocketChannel client) {
        http = new Http();
        try {
            http.setRequest(RequestParser.parseRequest(client));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return http;
    }
}
