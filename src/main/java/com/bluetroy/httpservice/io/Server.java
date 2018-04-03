package com.bluetroy.httpservice.io;

import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.httpservice.mvc.Connector;
import com.bluetroy.httpservice.mvc.ServiceRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger("server");
    private Selector selector;

    private boolean init() {
        long start = System.currentTimeMillis();
        ServerSocketChannel serverSocketChannel = null;
        try {
            ServiceRegistry.registerServices();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);//设置非阻塞
            serverSocketChannel.bind(new InetSocketAddress(8080));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//注册ACCEPT事件
        } catch (IOException e) {
            LOGGER.warning("初始化错误" + e);
            if (serverSocketChannel != null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e1) {
                    LOGGER.warning("服务器关闭错误" + e1);
                }
            }
            return false;
        }
        LOGGER.info("服务器启动耗时" + (System.currentTimeMillis() - start) + "ms");
        return true;
    }

    public void start() {

        if (!init()) {//当init成功才继续
            return;
        }

        while (true) {
            try {
                //selector.select() 会阻塞直到有注册的事件来临
                selector.select();
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        //Accept事件 就是可以来了一个Socket可以建立连接了
                        ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
                        SocketChannel client = serverSocket.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        //Read事件 接收到HTTP请求
                        //读取到数据之后提交给Controller进行异步的HTTP请求解析,根据FilePath转发给服务处理类。处理完后会给通道注册WRITE的监听。client.register(selector, SelectionKey.OP_WRITE)。
                        //并让key携带Response对象(将在后续章节写出)
                        SocketChannel client = (SocketChannel) key.channel();
                        ThreadPool.execute(new Connector(client, selector));
                        //TODO 什么意思
                        key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);

                    } else if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        Response response = (Response) key.attachment(); //再connector 中的attachResponse中被添加

                        ByteBuffer byteBuffer = response.getByteBuffer();
                        if (byteBuffer.hasRemaining()) {
                            client.write(byteBuffer);
                        }
                        if (!byteBuffer.hasRemaining()) {
                            key.cancel();
                            client.close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        try {
            selector.close();
        } catch (IOException e) {
            LOGGER.warning("选择器关闭失败" + e);
        }
    }
}
