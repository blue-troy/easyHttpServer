package com.bluetroy.servlet.utils;

import com.bluetroy.StartUp;
import com.bluetroy.servlet.Service;
import com.bluetroy.servlet.ServiceRegistry;
import com.bluetroy.servlet.annotation.Controller;
import com.bluetroy.servlet.annotation.RequestMapping;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class AnnotationScanner {
    static final String packageRootURL = StartUp.class.getResource("/").getFile();
    static final String packageName = StartUp.class.getPackageName(); //startUp 类地址
    static final String classRootPathString = packageRootURL + packageName.replace(".", "/");
    static final Path classRootPath = Paths.get(classRootPathString);


    public static void registerServices() throws IOException {
        Files.walkFileTree(classRootPath, new controllerVisitor());
/*        //过滤出目录和类文件
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory() || pathname.getName().endsWith("class")) return true;
                return false;
            }
        };
        registerFromPackage(packageName, packageRootURL + packageName.replace(".", "/"), fileFilter);*/
    }


/*    public static void main(String[] args) throws IOException {
        new AnnotationScanner().register();

    }

    *//*
     * auto registry service from package
     * *//*
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
                                ServiceRegistry.register(preUrl + pixUrl, new Service(aClass.asSubclass(ServiceInterface.class).getConstructor().newInstance(), method));
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

    private void register() throws IOException {
        //扫描出所有带有controller注解的类 他们是控制器
        Files.walkFileTree(classRootPath, new controllerVisitor());
    }*/

    private static class controllerVisitor extends SimpleFileVisitor<Path> {


        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!file.toString().endsWith(".class")) return FileVisitResult.CONTINUE; //过滤出class文件
            String packageName = pathToPackageName(file);
            try {
                Class<?> aClass = Class.forName(packageName);
                if (aClass.isAnnotationPresent(Controller.class)) {
                    handleController(aClass);
                }

            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }


            return FileVisitResult.CONTINUE;

        }

        private void handleController(Class<?> aClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            String preUrl;
            Annotation annotation = aClass.getAnnotation(RequestMapping.class);
            if (annotation != null) {
                preUrl = ((RequestMapping) annotation).value();
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    Annotation requestMapping = method.getAnnotation(RequestMapping.class);
                    if (requestMapping != null) {
                        String pixUrl = ((RequestMapping) requestMapping).value();
                        ServiceRegistry.register(preUrl + pixUrl, new Service(aClass.getConstructor().newInstance(), method));
                        System.out.println(((RequestMapping) requestMapping).value());
                    }
                }
            }
        }

        private String pathToPackageName(Path file) {
            return file.toString().substring(packageRootURL.length(), file.toString().indexOf(".")).replace("/", ".");
        }
    }

}
