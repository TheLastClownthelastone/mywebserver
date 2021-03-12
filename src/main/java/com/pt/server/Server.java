package com.pt.server;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
/**
 * 服务启动的接口
 */
public interface Server
{
    /**
     * 启动方法
     */
    void start();
    /**
     * 预启动方法
     */
    void preStart() throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException;

}
