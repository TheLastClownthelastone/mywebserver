package com.pt.annotation;
import com.pt.task.TaskDoType;
import com.pt.task.timer.MyTimeUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PTJob
{
    // 任务名称
    String name() default "";

    // 任务执行方式
    TaskDoType  type();

    //  执行的时间
    long lValue() default Long.MAX_VALUE;
    // 执行的时间片段
    String time() default "";

    // 执行周期
    MyTimeUnit cycle() default MyTimeUnit.SECONDS;

}
