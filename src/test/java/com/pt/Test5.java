package com.pt;
import com.pt.beans.TestMethodAnno;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
public class Test5
{
    /**
     * 获取方法方法形参的注解
     */
    @Test
    public void test1(){
        Class<TestMethodAnno> testMethodAnnoClass = TestMethodAnno.class;
        Method[] declaredMethods = testMethodAnnoClass.getDeclaredMethods();
        for (Method method : declaredMethods)
        {
            // 获取方法中所有的形参
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 获取所有形参的注解
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterTypes.length; i++)
            {
                // 获取 每一个参数对应的注解
                for (Annotation annotation : parameterAnnotations[i])
                {
                    System.out.println(annotation.toString());
                }
            }
        }
    }
}
