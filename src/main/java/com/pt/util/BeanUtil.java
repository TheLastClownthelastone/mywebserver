package com.pt.util;
import cn.hutool.core.convert.Convert;
import com.pt.exception.PtException;

import java.lang.reflect.Field;
import java.util.Map;
/**
 * 用来处理实体的工具类
 */
public class BeanUtil
{
    /**
     * 将map转成javaBean
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T MapCastToBean(Map<String,Object> map,Class<T> clazz){
        T t;
        try
        {
            t = clazz.newInstance();
            // 获取到所有的属性集合
            Field[] declaredFields = clazz.getDeclaredFields();
            // 进行遍历 将map中key对应属性名称对应上的进行赋值
            for (Field field : declaredFields)
            {
                for (Map.Entry<String, Object> entry : map.entrySet())
                {
                    if (field.getName().equals(entry.getKey()))
                    {
                        // 设置私有属性也可以进行访问
                        field.setAccessible(true);
                        Class<?> type = field.getType();
                        // 进行强制类型转换之后进行赋值
                        Object convert = Convert.convert(type, entry.getValue());
                        field.set(t,convert);
                    }
                }
            }
        } catch (InstantiationException e)
        {
            throw PtException.error(e.getMessage());
        } catch (IllegalAccessException e)
        {
            throw PtException.error(e.getMessage());
        }
        return t;
    }
}
