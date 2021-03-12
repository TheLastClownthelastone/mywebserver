package com.pt.cglib;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Method;
/**
 * 测试cglib中的工程
 */
public class TestCglib
{
    @Test
    public void test(){
        TestCglibBean o = (TestCglibBean)Enhancer.create(TestCglibBean.class, new MethodInterceptor()
        {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable
            {
                System.out.println("在执行方法之前执行");
                methodProxy.invokeSuper(o,objects);
                System.out.println("在执行方法之后执行");
                return o;
            }
        });
        o.say("ssss");

    }

}
