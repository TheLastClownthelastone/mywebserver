package com.pt.aspct;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
public interface Advice
{

    /**
     * 前置方法
     * @param obj
     * @param method
     * @param args
     * @param proxy
     */
    void before(Object obj, Method method, Object[] args, MethodProxy proxy);
    /**
     * 后置方法
     * @param obj
     * @param method
     * @param args
     * @param proxy
     */
    void after(Object obj, Method method, Object[] args, MethodProxy proxy);

    /**
     * 发生异常的时候执行该方法
     * @param obj
     * @param method
     * @param args
     * @param proxy
     * @param throwable
     */
     void exception(Object obj, Method method, Object[] args, MethodProxy proxy,Throwable throwable);
}
