package com.pt.sql;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
public class CglibProxy implements MethodInterceptor
{
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable
    {
        Class<?> returnType = method.getReturnType();
        Object doer = doer(returnType);
        return doer;
    }


    private <T> T doer(Class<T> tClass){
        Object o = 18;
        return (T)o;
    }

}
