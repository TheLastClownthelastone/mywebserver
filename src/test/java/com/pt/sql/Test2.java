package com.pt.sql;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
public class Test2
{

    @Test
    public void test1(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestInterFace.class);

        enhancer.setCallback(new MethodInterceptor()
        {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable
            {
                ParameterizedType genericReturnType1 = (ParameterizedType) method.getGenericReturnType();
                System.out.println(genericReturnType1.getActualTypeArguments()[0].getTypeName());
//                Type genericReturnType = method.getGenericReturnType();
//                System.out.println(genericReturnType.getTypeName());

                return null;
            }
        });
        TestInterFace testInterFace = (TestInterFace) enhancer.create();
        List<String> list = testInterFace.list();
        System.out.println(list);
    }
}
