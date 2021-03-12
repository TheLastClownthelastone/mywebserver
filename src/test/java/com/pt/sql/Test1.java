package com.pt.sql;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.junit.Test;

import java.lang.reflect.Proxy;
public class Test1
{

    @Test
    public void test1(){
        MethodProxy proxy = new MethodProxy();
        Object o = Proxy.newProxyInstance(InterFaceInverBean.class.getClassLoader(), new Class[]{InterFaceInverBean.class}, proxy);
        InterFaceInverBean o1 = (InterFaceInverBean) o;
        System.out.println(o1.getnum());
    }

    @Test
    public void test2(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(InterFaceInverBean.class);
        enhancer.setCallback(new CglibProxy());
        InterFaceInverBean interFaceInverBean = (InterFaceInverBean) enhancer.create();
        System.out.println(interFaceInverBean.getnum());
    }
}
