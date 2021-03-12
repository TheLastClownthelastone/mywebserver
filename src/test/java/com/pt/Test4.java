package com.pt;
import com.pt.beans.SampleClass;
import io.netty.handler.codec.http.HttpMethod;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Method;
/**
 * 测试cglb进行 动态代理
 */
public class Test4
{

    @Test
    public void test1(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                System.out.println("before method run...");
                Object result = proxy.invokeSuper(obj, args);
                System.out.println("after method run...");
                return result;
            }
        });
        SampleClass sample = (SampleClass) enhancer.create();
        sample.test(1,"1");
    }

    @Test
    public void test2(){
        System.out.println(HttpMethod.POST.name());
    }

    @Test
    public void test3(){
        String uri = "http://www.baidu.com?name=qz&kf=pg";
        String substring = uri.substring(uri.indexOf("?")+1);
        System.out.println(substring);
    }
}
