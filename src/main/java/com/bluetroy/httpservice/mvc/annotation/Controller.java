package com.bluetroy.httpservice.mvc.annotation;


import java.lang.annotation.*;

/**
 * 服务注解
 */
@Target({ElementType.TYPE})
//指明该类型的注解可以注解的程序元素的范围。该元注解的取值可以为TYPE,METHOD,CONSTRUCTOR,FIELD等。如果Target元注解没有出现，那么定义的注解可以应用于程序的任何元素
@Retention(RetentionPolicy.RUNTIME) //指明了该Annotation被保留的时间长短。RetentionPolicy取值为SOURCE,CLASS,RUNTIME
@Documented
//指明拥有这个注解的元素可以被javadoc此类的工具文档化。这种类型应该用于注解那些影响客户使用带注释的元素声明的类型。如果一种声明使用Documented进行注解，这种类型的注解被作为被标注的程序成员的公共API
public @interface Controller {

}
