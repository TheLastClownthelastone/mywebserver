package com.pt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nate-pt
 * @date 2022/12/26 9:33
 * @Since 1.8
 * @Description 忽略sql注入
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface IgnoreSql {
}
