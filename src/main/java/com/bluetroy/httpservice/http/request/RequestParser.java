package com.bluetroy.httpservice.http.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RequestParser {
    private static final Logger LOGGER = Logger.getLogger("RequestParser");

    public static Request parseRequest(SocketChannel channel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);//1KB
        channel.read(byteBuffer);//将数据从SocketChannel 读到Buffer中
        byteBuffer.flip();

        int remaining = byteBuffer.remaining();//缓冲区剩余长度 此长度为实际读取的数据长度
        byte[] bytes = new byte[remaining];
        byteBuffer.get(bytes);//把buffer中的数据提取到bytes

        LOGGER.info("request ：" + new String(bytes, 0, bytes.length));

        //把数据分成header 和 body部分 （利用header与body中有一个空行）
        //TODO :未来应该改用byte方式处理
        //TODO : 改用之前查看 string stringBuilder buffer 之间区别
        StringBuffer requestBuffer = new StringBuffer(new String(bytes, 0, bytes.length));
        int position = requestBuffer.indexOf("\r\n\r\n");
        if (position == -1) {
            //TODO: 添加异常类
            throw new IllegalArgumentException("请求不合法");
        }

        RequestHeader requestHeader = parseHeader(requestBuffer.substring(0, position));
        RequestBody requestBody = parseBody(requestHeader, requestBuffer.substring(position + 4)); // ""\r\n\r\n""四个字符

        return new Request(requestHeader, requestBody);
    }

    private static RequestBody parseBody(RequestHeader header, String bodyString) {
        //先判断Content-Type再进行对应的解析

        //x-www-form-urlencoded 的内容是这样的:one=23&two=123
        Map<String, String> formMap = Collections.EMPTY_MAP;
        Map<String, MimeData> mineMap = Collections.EMPTY_MAP;
        String contentType = header.getContentType();
        if (contentType == null || contentType.isEmpty()) return new RequestBody();
        if (header.getContentType().contains("application/x-www-form-urlencoded")) {
            parseParameters(bodyString, formMap);
        }
        //TODO 未来再添加吧
        else if (header.getContentType().contains("multipart/form-data")) { //https://www.jianshu.com/p/e810d1799384 multipart/form-data的解释说明
            int boundaryValueIndex = bodyString.indexOf("boundary=");
            String mimeString = bodyString.substring(boundaryValueIndex + 9);
            mineMap = parseMineMap(mimeString, mineMap);
        }

        RequestBody requestBody = new RequestBody();
        requestBody.setFormMap(formMap);
        requestBody.setMimeMap(mineMap);
        return requestBody;
    }

    //TODO 解析mime类型 思考：若有图片或者视频则用string不合适
    private static Map<String, MimeData> parseMineMap(String mimeString, Map<String, MimeData> mineMap) {
        return null;
    }

    private static RequestHeader parseHeader(String header) {
        RequestHeader requestHeader = new RequestHeader();

        Map<String, String> headMap = new HashMap<>();
        String[] headerLine = header.split("\r\n"); //每行分开
        //处理请求行
        String[] requestLine = headerLine[0].split("\\s");
        String method = requestLine[0];
        String path = requestLine[1];


        //url 地址 转码成utf-8
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String scheme = requestLine[2];
        //处理请求头
        for (int i = 1; i < headerLine.length; i++) {
            String[] keyValue = headerLine[i].split(":", 2);
            headMap.put(keyValue[0].trim(), keyValue[1].trim());
        }

        //todo 对象的序列化，反序列化？
        //从path中提取get 类型的queryString exam：path://s.taobao.com/search?q=神&imgfile=a
        // path://s.taobao.com/search?student= name=heyixin&age=11
        int index = path.indexOf('?');
        Map<String, String> queryMap = Collections.emptyMap();
        String queryString = null;
        if (index != -1) {
            queryString = path.substring(index + 1);
            path = path.substring(0, index);
            queryMap = parseParameters(queryString, queryMap);
        }

        requestHeader.setURI(path);
        requestHeader.setHead(headMap);
        requestHeader.setMethod(method);
        requestHeader.setQueryMap(queryMap);

        return requestHeader;
    }

    //queryString:q=神&imgfile=a
    private static Map<String, String> parseParameters(String queryString, Map<String, String> queryMap) {
        queryMap = new HashMap<>();
        String[] queryStringList = queryString.split("&");
        for (String s : queryStringList) {
            String[] keyValue = s.split("=", 2);
            queryMap.put(keyValue[0], keyValue[1]);
            System.out.println("111");
        }
        return queryMap;
    }

}
