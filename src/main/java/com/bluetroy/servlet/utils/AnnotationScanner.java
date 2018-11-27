package com.bluetroy.servlet.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author heyixin
 */
public class AnnotationScanner {

    public static <A extends Annotation> List<Class> getClassList(Class<A> annotationClass) {
        return ClassScanner.getClassList().stream().filter(clazz -> clazz.isAnnotationPresent(annotationClass)).collect(Collectors.toList());
    }

    public static <A extends Annotation> List<Method> getMethodList(Class<A> annotationClass) {
        return getMethodList(null, annotationClass);
    }

    public static <A extends Annotation> List<Method> getMethodList(Class clazz, Class<A> annotationClass) {
        Stream<Method> methodStream;
        if (clazz == null) {
            methodStream = MethodScanner.getMethodList().stream();
        } else {
            methodStream = Arrays.stream(clazz.getMethods());
        }
        return methodStream.filter(method -> method.isAnnotationPresent(annotationClass)).collect(Collectors.toList());
    }
}
