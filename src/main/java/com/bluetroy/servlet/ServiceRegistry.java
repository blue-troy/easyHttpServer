package com.bluetroy.servlet;


import com.bluetroy.servlet.annotation.Controller;
import com.bluetroy.servlet.annotation.RequestMapping;
import com.bluetroy.servlet.utils.AnnotationScanner;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 服务注册中心
 */
@Log4j2
public class ServiceRegistry {
    private static Map<String, Service> services = new TreeMap<>(); //我们得到的是已排好序的结果 ?for what

    public static void register(String urlPattern, Service service) {
        services.put(urlPattern, service);
    }

    public static Service findService(String url) {
        for (String pattern : services.keySet()) {
            System.out.println("url = " + url + " pattern = " + pattern + url.matches(pattern));
            if (url.matches(pattern)) return services.get(pattern);
        }
        // 弃用 无法利用正则表达 return services.get(urlPattern);
        return new Service(null, null);
    }

    public static void resisterService() throws IOException {
        registerController();
    }

    private static void registerController() {
        List<Class> classList = AnnotationScanner.getClassList(Controller.class);
        for (Class controller : classList) {
            String preUrl = ((RequestMapping) controller.getAnnotation(RequestMapping.class)).value();
            for (Method method : AnnotationScanner.getMethodList(controller, RequestMapping.class)) {
                String pixUrl = method.getAnnotation(RequestMapping.class).value();
                try {
                    ServiceRegistry.register(preUrl + pixUrl, new Service(controller.getConstructor().newInstance(), method));
                    log.info("controller :" +preUrl+pixUrl +" " + method.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
