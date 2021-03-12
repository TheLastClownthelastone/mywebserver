package com.pt.sql;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 * 使用java中动态代理模式
 */
public class MethodProxy implements InvocationHandler
{
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        return 189;
    }
}
