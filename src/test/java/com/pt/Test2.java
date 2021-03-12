package com.pt;
import cn.hutool.core.io.IoUtil;
import com.pt.beans.TestBean1;
import com.pt.beans.TestBean2;
import com.pt.context.BeanContext;
import com.pt.context.DefaultBeanContext;
import com.pt.excaplem.controller.MainController;
import com.pt.init.InitExecutor;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * 测试读取yml文件
 */
public class Test2
{

    @Test
    public void test1(){
        InputStream in = null;
        try
        {
            Yaml yaml = new Yaml();
            // 通过类加载器获取resource中的静态资源
            in = this.getClass().getClassLoader().getResourceAsStream("application.yml");
            //文件路径是相对类目录(src/main/java)的相对路径
            Map<String, Object> map = yaml.loadAs(in, Map.class);
            String appid = map.getOrDefault("appid", "123").toString();
            System.out.println(appid);
            String port = ((Map<String, Object>) map.get("server")).get("port").toString();
            System.out.println(port);
            Object test = map.get("test");
            System.out.println("获取yml中只有一个层级的属性："+test);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            IoUtil.close(in);
        }
    }

    @Test
    public void test2(){
        BeanContext instance = DefaultBeanContext.getInstance();
        System.out.println(instance);
    }

    @Test
    public void test3(){
        Annotation[] annotations = TestBean1.class.getAnnotations();
        System.out.println(Arrays.toString(annotations));
        String simpleName = TestBean1.class.getSimpleName();
        System.out.println(simpleName);
    }


    @Test
    public void test4() throws IntrospectionException
    {
        // 通过Introspector 获取一个bean的所有内容 然后获取改类的属性方法信息
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(TestBean2.class).getPropertyDescriptors();
        List<PropertyDescriptor> propertyDescriptors1 = Arrays.asList(propertyDescriptors);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors1)
        {
            System.out.println(propertyDescriptor.getName());
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null)
            {
                System.out.println("setter方法为："+writeMethod.getName());
                Class<?>[] parameterTypes = writeMethod.getParameterTypes();
                Arrays.asList(parameterTypes).stream().map(Class::getName).forEach(System.out::println);
            }
            System.out.println("getter方法为："+propertyDescriptor.getReadMethod().getName());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        // 只能获取对应的public方法如果是private 修饰的话，访问不到
        MethodDescriptor[] methodDescriptors = Introspector.getBeanInfo(TestBean2.class).getMethodDescriptors();
        for (MethodDescriptor methodDescriptor : methodDescriptors)
        {
            Method method = methodDescriptor.getMethod();
            method.setAccessible(true);
            System.out.println(method.getName());
        }
    }
    /**
     * 测试获取自定义的容器
     */
    @Test
    public void test5() throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        // 先执行完初始化bean类加载的
        InitExecutor.init(BootStrap.class);
        // 通过上下文对象获取容器里面的内容
        BeanContext instance = DefaultBeanContext.getInstance();
        MainController mainController = instance.getBean("mainController", MainController.class);
        mainController.setOne();
    }

}
