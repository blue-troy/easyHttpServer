package com.bluetroy.servlet;

import com.bluetroy.httpservice.http.Http;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.httpservice.http.response.impl.NotFoundResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.bluetroy.servlet.utils.ReflectUtil.ParseObject;

public class Service {
    private Object serviceObject;
    private Method serviceMethod;

    public Service(Object serviceObject, Method serviceMethod) {
        this.serviceObject = serviceObject;
        this.serviceMethod = serviceMethod;
    }

    //todo 重新了解异常处理问题
    public Http serve(Http http) {

        //若没有对应的服务 返回404
        if (http.getRequest() == null || serviceObject == null) {
            http.setResponse(new NotFoundResponse());
            return http;
        }

        //TODO 从request中解析出 requestmapping方法下需要的类
        Object[] objects = new Object[serviceMethod.getParameterCount()];
        // 1。 需要的类
        //解析出需要的类
        for (int i = 0; i < serviceMethod.getParameterCount(); i++) {
            objects[i] = ParseObject(http.getRequest(), serviceMethod.getParameters()[i]);
        }

        try {
            http.setResponse((Response) serviceMethod.invoke(serviceObject, objects));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return http;
    }


}
