package com.bluetroy.httpservice.http.response;

import com.bluetroy.httpservice.http.Status;
import com.bluetroy.servlet.utils.TimeUtil;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class Response {
    protected static final String HTTP_VERSION = "HTTP/1.1";
    protected static final String DEFAULT_CHARSET = "utf-8";
    protected Map<String, String> heads;
    protected byte[] content;
    private Status status;
    //由于最后写入SocketChannel需要ByteBuffer,那么我们需要将响应变成ByteBuffer
    private ByteBuffer finalData = null;
    private static final Logger LOGGER = Logger.getLogger("response");

    public Response(Status status) {
        this.status = status;
        heads = new HashMap<>();
        content = new byte[0];
        this.heads.put("Date", TimeUtil.toRFC822(ZonedDateTime.now()));
        //TODO SERVER
        this.heads.put("Connection", "Close"); // TODO keep-alive
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, String> getHeads() {
        return heads;
    }

    public void setHeads(Map<String, String> heads) {
        this.heads = heads;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public ByteBuffer getByteBuffer() {

        if (finalData == null) { //没有这样的话 ，多次读取就会造成 byteBuffer 多次flip ，导致丢失position，limit
            //构造response
            StringBuilder headStringBuilder = new StringBuilder();
            //状态行
            headStringBuilder.append(HTTP_VERSION).append("  ").append(status.getCode()).append("  ").append(status.getMessage()).append("\r\n");
            //消息报头
            heads.put("Content-Length", String.valueOf(content.length));
            for (Map.Entry<String, String> headEntry : heads.entrySet()) {
                headStringBuilder.append(headEntry.getKey()).append(": ").append(headEntry.getValue()).append("\r\n");
            }
            //空行
            headStringBuilder.append("\r\n");

            //二进制化
            byte[] head = new byte[0];
            try {
                head = headStringBuilder.toString().getBytes(DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //合成response
            finalData = ByteBuffer.allocate(head.length + content.length + 2);// +2 因为最后还有一个回车换行
            finalData.put(head);
            finalData.put(content);
            finalData.put("\r\n".getBytes());
            finalData.flip(); //切换成读取模式
        }
        return finalData;
    }
}
