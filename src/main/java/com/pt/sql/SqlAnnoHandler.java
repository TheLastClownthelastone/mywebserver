package com.pt.sql;
import com.pt.annotation.Delete;
import com.pt.annotation.Insert;
import com.pt.annotation.Select;
import com.pt.annotation.Update;
import com.pt.constant.SystemConstant;
import com.pt.util.SystemAssert;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
public class SqlAnnoHandler
{
    private static Session session = DefaultSession.getInstance();

    public  <T> T executeSqlToBean(Class<T> mapperClass){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(mapperClass);
        enhancer.setCallback(new MapperBeanMethodHandler());
        Object o = enhancer.create();
        return (T)o;
    }

    private static class  MapperBeanMethodHandler implements MethodInterceptor{
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable
        {
           // 获取执行的sql 获取参数的类型
            String sql = getSql(method);
            SystemAssert.isTrue(sql != null,"执行sql异常");
            // 获取方法的返回值
            if (method.getReturnType().isAssignableFrom(List.class))
            {
                // 查询出list的泛型
                ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
                String typeName = genericReturnType.getActualTypeArguments()[0].getTypeName();
                Class<?> aClass = Class.forName(typeName);
                if (objects.length > SystemConstant.IsInt.NO.getCode())
                {
                    Class<?> parameterType = method.getParameterTypes()[0];
                    return session.selectList(sql, objects, new Class[]{aClass,parameterType});
                }else {
                    return session.selectList(sql, aClass);
                }
            }else{

                // 获取方法的返回值
                Class<?> returnType = method.getReturnType();
                if (objects.length > SystemConstant.IsInt.NO.getCode())
                {
                    Class<?> parameterType = method.getParameterTypes()[0];
                    return session.selectOne(sql, objects, new Class[]{returnType,parameterType});
                }else {
                    return session.selectOne(sql, returnType);
                }
            }
        }

        private String getSql(Method method){
            if (method.isAnnotationPresent(Select.class))
            {
                Select annotation = method.getAnnotation(Select.class);
                String value = annotation.value();
                return value;
            }

            if (method.isAnnotationPresent(Update.class))
            {
                Update annotation = method.getAnnotation(Update.class);
                String value = annotation.value();
                return value;
            }

            if (method.isAnnotationPresent(Insert.class))
            {
                Insert annotation = method.getAnnotation(Insert.class);
                String value = annotation.value();
                return value;
            }

            if (method.isAnnotationPresent(Delete.class))
            {
                Delete annotation = method.getAnnotation(Delete.class);
                String value = annotation.value();
                return value;
            }
            return null;
        }
    }

    private SqlAnnoHandler(){}

    public static SqlAnnoHandler getInstance(){
        return new SqlAnnoHandler();
    }

}
