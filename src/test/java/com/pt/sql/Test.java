package com.pt.sql;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
public class Test
{
    private List<String> list;
    public static void main(String[] args)
    {
        try
        {
            Field listField = Test.class.getDeclaredField("list");
            System.out.println(listField.getGenericType());
            //获取 list 字段的泛型参数
            ParameterizedType listGenericType = (ParameterizedType) listField.getGenericType();
            Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
            System.out.println(listActualTypeArguments[listActualTypeArguments.length - 1]);
            for (int i = 0; i < listActualTypeArguments.length; i++)
            {
                System.out.println(listActualTypeArguments[i]);
            }
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }
}
