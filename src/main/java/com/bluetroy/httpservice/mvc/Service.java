package com.bluetroy.httpservice.mvc;

import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.httpservice.mvc.service.ServiceInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Service {
    ServiceInterface serviceInterface;
    Method serviceMethod;

    public Service(ServiceInterface serviceInterface, Method serviceMethod) {
        this.serviceInterface = serviceInterface;
        this.serviceMethod = serviceMethod;
    }

    //todo 重新了解异常处理问题
    public Response serve(Request request) {
        Response response = null;
        try {
            response = (Response) serviceMethod.invoke(serviceInterface,request);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return response;
    }
}
