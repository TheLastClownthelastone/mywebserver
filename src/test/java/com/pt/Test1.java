package com.pt;
import cn.hutool.core.lang.ClassScaner;
import com.pt.annotation.PtServer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * 测试hutool中的工具类的用法
 */
public class Test1
{
    @Test
    public void test1(){
        // 指定表扫描
        //Set<Class<?>> classes = ClassScaner.scanPackage("com.pt");

        // 扫描表的指定包的位置，并且指定类型为指定值的class集合
        //Set<Class<?>> classes = ClassScaner.scanPackageBySuper("com.pt", Server.class);

        // 扫描指定注解类型和指定包路径的class集合
        Class<BootStrap> bootStrapClass = BootStrap.class;
        String name = bootStrapClass.getName();
        name = name.substring(0,name.lastIndexOf("."));

        Set<Class<?>> classes = ClassScaner.scanPackageByAnnotation(name, PtServer.class);
        for (Class<?> aClass : classes)
        {
            System.out.println(aClass.getName());
        }
    }

    @Test
    public void test2(){
        Map<String,String> map = new HashMap<>();
        map.put("1","2");
        String put = map.put("1", "3");
        System.out.println(put);
    }
}
