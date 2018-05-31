package com.bluetroy.servlet.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
//指明该类型的注解可以注解的程序元素的范围。该元注解的取值可以为TYPE,METHOD,CONSTRUCTOR,FIELD等。如果Target元注解没有出现，那么定义的注解可以应用于程序的任何元素
@Retention(RetentionPolicy.RUNTIME) //指明了该Annotation被保留的时间长短。RetentionPolicy取值为SOURCE,CLASS,RUNTIME
@Documented
public @interface RequestMapping {
    String value() default "";
}
