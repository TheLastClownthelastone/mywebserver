package com.pt.annotation;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PtServer
{
    String value() default "";
}
