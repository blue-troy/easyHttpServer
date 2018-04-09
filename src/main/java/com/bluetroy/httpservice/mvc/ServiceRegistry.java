package com.bluetroy.httpservice.mvc;


import com.bluetroy.httpservice.StartUp;
import com.bluetroy.httpservice.mvc.annotation.Controller;
import com.bluetroy.httpservice.mvc.annotation.RequestMapping;
import com.bluetroy.httpservice.mvc.service.ServiceInterface;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;


/**
 * 服务注册中心
 */
public class ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    private static Map<String, ServiceInterface> services = new TreeMap<>(); //我们得到的是已排好序的结果 ?for what

    private static void register(String urlPattern, ServiceInterface service) {
        services.put(urlPattern, service);
    }

    public static ServiceInterface findService(String url) {
        for (String pattern : services.keySet()) {
            System.out.println("url = " + url + " pattern = " + pattern + url.matches(pattern));
            if (url.matches(pattern)) return services.get(pattern);
        }
        // 弃用 无法利用正则表达 return services.get(urlPattern);
        return null;
    }

    public static void registerServices() {

        String packageRootURL = StartUp.class.getResource("/").getFile(); //项目根路径的绝对地址
        String packageName = StartUp.class.getPackageName(); //startUp 类地址

        //过滤出目录和类文件
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory() || pathname.getName().endsWith("class")) return true;
                return false;
            }
        };
        registerFromPackage(packageName, packageRootURL + packageName.replace(".", "/"), fileFilter);
    }

    /*
     * auto registry service from package
     * */
    //TODO
    private static void registerFromPackage(String packageName, String packageURL, FileFilter fileFilter) {
        File dir = new File(packageURL);
        if (!dir.exists() || !dir.isDirectory()) return;
        File[] dirFiles = dir.listFiles(fileFilter);
        for (File file : dirFiles)
            if (file.isDirectory()) {
                registerFromPackage(packageName + "." + file.getName(), file.getAbsolutePath(), fileFilter);
            } else {
                String className = file.getName().substring(0, file.getName().indexOf("."));
                try {
                    Class<?> aClass = Class.forName(packageName + "." + className);
                    Annotation[] annotations = aClass.getAnnotations();
                    if (annotations.length > 1 && annotations[0].annotationType().equals(Controller.class) && annotations[1].annotationType().equals(RequestMapping.class)) {
                        RequestMapping requestMapping = aClass.getAnnotation(RequestMapping.class);
                        for (int i = 0; i < requestMapping.value().length; i++) {
                            register(requestMapping.value()[i],aClass.asSubclass(ServiceInterface.class).getConstructor().newInstance());
                            System.out.println("成功注册服务: " + requestMapping.value()[i] +"  " + aClass.getName());
                        }
                    }

                    //放弃以下早期的扫描服务代码
                    // 实现了注解，并且实现了接口
//                    if (annotation != null && ServiceInterface.class.isAssignableFrom(aClass)) {
//
////                        register(annotation.urlPattern(),aClass.asSubclass(ServiceInterface.class).getDeclaredConstructor().newInstance());
////                        System.out.println("成功注册服务: " + annotation.urlPattern() + "  " + className);
//                    }
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
    }

}
