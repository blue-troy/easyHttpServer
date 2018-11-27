package com.bluetroy.servlet.utils;


import com.bluetroy.httpservice.http.request.MimeData;
import com.bluetroy.httpservice.http.request.Request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class ReflectUtil {

    private static final Map<Class, Function<Object, Object>> TYPE_FUNCTION_MAP; // 类型转换函数
    private static final Function<Object, Object> MISS_FUNCTION = s -> null;

    static {
        TYPE_FUNCTION_MAP = new HashMap<>();
        Function<Object, Object> boolFunction = s -> Boolean.parseBoolean((String) s);
        Function<Object, Object> longFunction = s -> Long.parseLong((String) s);
        Function<Object, Object> intFunction = s -> Integer.parseInt((String) s);
        Function<Object, Object> doubleFunction = s -> Double.parseDouble((String) s);
        Function<Object, Object> floatFunction = s -> Float.parseFloat((String) s);
        Function<Object, Object> charFunction = s -> ((String) s).charAt(0);
        TYPE_FUNCTION_MAP.put(Boolean.class, boolFunction);
        TYPE_FUNCTION_MAP.put(String.class, String::valueOf);
        TYPE_FUNCTION_MAP.put(Long.class, longFunction);
        TYPE_FUNCTION_MAP.put(Integer.class, intFunction);
        TYPE_FUNCTION_MAP.put(Double.class, doubleFunction);
        TYPE_FUNCTION_MAP.put(Float.class, floatFunction);
        TYPE_FUNCTION_MAP.put(Character.class, charFunction);
        TYPE_FUNCTION_MAP.put(byte[].class, o -> ((MimeData) o).getData());
        TYPE_FUNCTION_MAP.put(MimeData.class, o -> o);

    }

    /**
     * 转换对象到指定类型,可能抛出ClassCastException
     * 解析基本数据类型
     *
     * @param val  对象
     * @param type 指定类型
     * @return 转换结果
     */
    public static Object parseObj(Object val, Class<?> type) {
        if (val == null) {
            return null;
        }
        Object result;
        try {
            result = TYPE_FUNCTION_MAP.getOrDefault(type, MISS_FUNCTION).apply(val);
            valid(result, type);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("parseObj error,target type:" + type.getName(), e);
        }
        return result;
    }

    //todo 目前不能解析string 等类型
    //解析自定义数据类型
    public static Object ParseObject(Request request, Parameter parameter) {
        //两种情况，一种是基本数据类型，一种是自定义类型
        //判断是否为基本数据类型，若是则根据参数名称获取request中的对应数
        try {
            if (isBaseDataType(parameter.getType())) {
                Object val = request.getHeader().getQueryMap().get(parameter.getName());
                return parseObj(val, parameter.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseDiyObject(request, parameter);
    }

    public static Object parseDiyObject(Request request, Parameter parameter) {
        Object o = null;
        try {
            // 构造
            o = parameter.getType().getDeclaredConstructor().newInstance();
            // 注入属性值
            for (Method method : parameter.getType().getMethods()) {
                if (method.getName().startsWith("set")) {
                    String property = method.getName().substring(3).toLowerCase();
                    //todo 存在类型转换问题 request中的属性不带类型 需要找到它的类型 目前的处理方式会导致若参数中的类型为某一复杂类型则会出错(属性为某个自定义类)
                    Object propertyObject = ReflectUtil.parseObj(request.getHeader().getQueryMap().get(property), method.getParameterTypes()[0]);
                    method.invoke(o, propertyObject);
                }
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return o;
    }

    private static void valid(Object val, Class<?> type) {
        if (val != null && val.getClass() != type) {
            throw new ClassCastException(val.getClass().getName() + " cannot be cast to " + type.getName());
        }
    }

    private static boolean isBaseDataType(Class clazz) throws Exception {
        return
                (
                        clazz.equals(String.class) ||
                                clazz.equals(Integer.class) ||
                                clazz.equals(Byte.class) ||
                                clazz.equals(Long.class) ||
                                clazz.equals(Double.class) ||
                                clazz.equals(Float.class) ||
                                clazz.equals(Character.class) ||
                                clazz.equals(Short.class) ||
                                clazz.equals(BigDecimal.class) ||
                                clazz.equals(BigInteger.class) ||
                                clazz.equals(Boolean.class) ||
                                clazz.equals(Date.class) ||
                                clazz.isPrimitive()
                );
    }
}
