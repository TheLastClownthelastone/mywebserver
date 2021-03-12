package com.pt.annotation;
import com.pt.constant.SystemConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
// 作用在类上和作用在对应的方法上
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface RequestMapping
{
    // 路由键
    String value() default "";

    // 请求方式
    SystemConstant.HttpMethod method() default SystemConstant.HttpMethod.GET;
}
