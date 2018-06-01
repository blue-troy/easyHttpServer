package com.bluetroy.servlet;


import com.bluetroy.servlet.utils.AnnotationScanner;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


/**
 * 服务注册中心
 */
public class ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
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
        return new Service(null,null);
    }

    public static void resisterService() throws IOException {
        AnnotationScanner.registerServices();
    }
}
