package com.bluetroy.httpservice.mvc;



import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.request.RequestParser;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.httpservice.http.response.impl.NotFoundResponse;
import com.bluetroy.httpservice.mvc.service.ServiceInterface;

import java.io.IOException;
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
        Request request = null;
        Response response = null;
        try {
            request = RequestParser.parseRequest(client);
            if (request==null) return;
            Service service = ServiceRegistry.findService(request.getHeader().getURI());
            if (service!=null) {
                response = service.serve(request);
            } else response = new NotFoundResponse();
            attachResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void attachResponse(Response response) {
        try {
            client.register(selector, SelectionKey.OP_WRITE, response);
            //todo wakeup?
            selector.wakeup();
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }
}
