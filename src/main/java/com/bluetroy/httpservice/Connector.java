package com.bluetroy.httpservice;


import com.bluetroy.httpservice.http.Http;
import com.bluetroy.httpservice.http.HttpCreator;
import com.bluetroy.servlet.Service;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 控制器
 */
public class Connector implements Runnable {
    private final SocketChannel client;
    private final Selector selector;

    public Connector(SocketChannel client, Selector selector) {
        this.client = client;
        this.selector = selector;
    }

    @Override
    public void run() {
        Http http = HttpCreator.create(client);
        Service service = DispatcherServlet.dispatch(http);
        http = service.serve(http);
        attachResponse(http);
    }

    private void attachResponse(Http http) {
        try {
            client.register(selector, SelectionKey.OP_WRITE, http);
            //todo wakeup?
            selector.wakeup();
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }
}
