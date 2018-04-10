package com.bluetroy.httpservice.mvc;

import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.httpservice.mvc.service.ServiceInterface;
import com.bluetroy.httpservice.utils.ReflectUtil;

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
        //TODO 从request中解析出 requestmapping方法下需要的类
        Object[] objects = new Object[serviceMethod.getParameterCount()];
        // 1。 需要的类
        //解析出需要的类
        for (int i = 0; i < serviceMethod.getParameterCount(); i++) {
            objects[i] = ParseObject(request, serviceMethod.getParameters()[i].getType());
        }


        try {
            response = (Response) serviceMethod.invoke(serviceInterface, objects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //todo 区分是否解析出
//        try {
//            response = (Response) serviceMethod.invoke(serviceInterface,request);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
        return response;
    }

    private Object ParseObject(Request request, Class<?> type) {
        Object o = null;
        try {
            // 构造
            o = type.getDeclaredConstructor().newInstance();
            // 注入属性值
            for (Method method : type.getMethods()) {
                if (method.getName().startsWith("set")) {
                    String property = method.getName().substring(3).toLowerCase();
                    //todo 存在类型转换问题 request中的属性不带类型 需要找到它的类型 目前的处理方式会导致若参数中的类型为某一复杂类型则会出错(属性为某个自定义类)
                    Object propertyObject = ReflectUtil.parseObj(request.getHeader().getQueryMap().get(property),method.getParameterTypes()[0]);
                    method.invoke(o, propertyObject);
                }
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return o;
    }
}
