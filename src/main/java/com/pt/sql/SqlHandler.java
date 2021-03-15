package com.pt.sql;
import com.pt.util.SystemAssert;

import java.lang.reflect.Field;
/**
 * sql处理器
 */
public class SqlHandler
{
    private String jing = "#";

    private String daole = "$";
    /**
     * 进行sql拼装
     * 将sql中的特殊符号进行处理
     * @param sql
     * @param objects 表示一个二元数组  一个Object数组中 增加了一个Object数组
     * @return
     */
    public String assembleSql(String sql,Class<?> clazz,Object ...objects) throws IllegalAccessException
    {
        // 当需要参数解析的时候，将对象中的参数拼接到sql中
        if (sql.contains(jing) || sql.contains(daole))
        {
            SystemAssert.isTrue(objects.length>0,"sql执行参数异常");
            Object[] object = (Object[]) objects[0];
            // 获取改对象中所有的属性
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields)
            {
                sql = sqlCastValue(sql,object[0],field);
            }
        }
        return sql;
    }
    /**
     * 执行sql占位符替换
     * @param sql
     * @param o
     */
    private String sqlCastValue(String sql,Object o,Field field) throws IllegalAccessException
    {
        Class<?> type = field.getType();
        String str = "#{"+field.getName()+"}";
        String qtr = "${"+field.getName()+"}";
        if (sql.contains(str) || sql.contains(qtr))
        {
            field.setAccessible(true);
            Object value = field.get(o);
            // 如果是数字类型不作处理，直接替换
            if (Number.class.isAssignableFrom(type))
            {
                sql = sql.replace(str,value.toString());
                sql = sql.replace(qtr,value.toString());
            }else {
                // 其他的类型的做字符拼接处理
                sql = sql.replace(str,"'"+value.toString()+"'");
                sql = sql.replace(qtr,"'"+value.toString()+"'");
            }
        }
        return sql;
    }


    private SqlHandler (){}

    public static SqlHandler getInstance(){
        return new SqlHandler();
    }
}
