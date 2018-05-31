package com.bluetroy;

import com.bluetroy.httpservice.io.Server;

public class StartUp {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

}
