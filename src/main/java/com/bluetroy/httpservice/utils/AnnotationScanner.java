package com.bluetroy.httpservice.utils;

import com.bluetroy.httpservice.StartUp;
import com.bluetroy.httpservice.mvc.Service;
import com.bluetroy.httpservice.mvc.annotation.Controller;
import com.bluetroy.httpservice.mvc.annotation.RequestMapping;
import com.bluetroy.httpservice.mvc.service.ServiceInterface;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static com.bluetroy.httpservice.mvc.ServiceRegistry.register;

public class AnnotationScanner {

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
                    //TODO 并不一定是按照顺序的，而且未来有可能不只是2个注解

                    // 扫描注册 controller requestMapping
                    if (annotations.length > 1 && annotations[0].annotationType().equals(Controller.class) && annotations[1].annotationType().equals(RequestMapping.class)) {
                        String preUrl = aClass.getAnnotation(RequestMapping.class).value();
                        Method[] methods = aClass.getMethods();
                        for (Method method : methods) {
                            Annotation requestMapping = method.getAnnotation(RequestMapping.class);
                            if (requestMapping != null) {
                                String pixUrl = ((RequestMapping) requestMapping).value();
                                register(preUrl + pixUrl, new Service(aClass.asSubclass(ServiceInterface.class).getConstructor().newInstance(), method));
                                System.out.println(((RequestMapping) requestMapping).value());
                            }
                        }

                    }

                    //放弃以下早期的扫描服务代码
                    // 实现了注解，并且实现了接口
//                    if (annotation != null && ServiceInterface.class.isAssignableFrom(aClass)) {
//
////                        register(annotation.urlPattern(),aClass.asSubclass(ServiceInterface.class).getDeclaredConstructor().newInstance());
////                        System.out.println("成功注册服务: " + annotation.urlPattern() + "  " + className);
//                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
    }
}
