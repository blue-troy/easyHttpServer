package com.bluetroy.servlet.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: heyixin
 * Date: 2018-11-28
 * Time: 04:09
 */
public class MethodScanner {
    private static final List<Method> METHOD_LIST = new ArrayList<>();

    static {
        List<Class> classList = ClassScanner.getClassList();
        for (Class aClass : classList) {
            METHOD_LIST.addAll(Arrays.asList(aClass.getMethods()));
        }
    }

    public static List<Method> getMethodList() {
        return List.copyOf(METHOD_LIST);
    }
}
