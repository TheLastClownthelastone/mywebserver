package com.pt.init;
import cn.hutool.core.lang.ClassScaner;
import com.pt.annotation.Component;
import com.pt.annotation.Controller;
import com.pt.annotation.EnableRedisService;
import com.pt.annotation.Mapper;
import com.pt.annotation.Service;
import com.pt.context.DefaultBeanContext;
import lombok.extern.slf4j.Slf4j;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * 初始化执行器
 */
@Slf4j
public class InitExecutor
{

    private InitExecutor(){

    }
    // 原子锁
    private static AtomicBoolean initialized = new AtomicBoolean(false);


    // 限制改方法只能执行一次
    public static void init(Class clazz) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException
    {
        // 如果是原子性的锁里面的值被修改了的话，那么返回值为true
        if (!initialized.compareAndSet(false,true))
        {
            log.error("【非法操作】：初始方法只能被执行一次");
            return;
        }else {
            String name = clazz.getName();
            name = name.substring(0,name.lastIndexOf("."));
            // 已启动类作为基准
            // 扫描含有@PtServer 注解下面所有含有自定义注解的类
            Set<Class<?>> result = new HashSet<>();
            Set<Class<?>> controllers = ClassScaner.scanPackageByAnnotation(name, Controller.class);
            Set<Class<?>> services = ClassScaner.scanPackageByAnnotation(name, Service.class);
            Set<Class<?>> components = ClassScaner.scanPackageByAnnotation(name, Component.class);
            Set<Class<?>> mappers = ClassScaner.scanPackageByAnnotation(name, Mapper.class);
            for (Class<?> controller : controllers)
            {
                result.add(controller);
            }
            for (Class<?> service : services)
            {
                result.add(service);
            }
            for (Class<?> component : components)
            {
                result.add(component);
            }
            for (Class<?> mapper : mappers)
            {
                result.add(mapper);
            }
            // 是否开启redis服务
            boolean isOpenRedis = clazz.isAnnotationPresent(EnableRedisService.class);
            // 初始化上下文
            DefaultBeanContext.doInt(result,isOpenRedis);
        }
    }



}
