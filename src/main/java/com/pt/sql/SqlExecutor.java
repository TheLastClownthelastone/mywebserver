package com.pt.sql;
import com.pt.constant.SystemConstant;
import com.pt.util.SystemAssert;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * sql执行器
 */
@Slf4j
public class SqlExecutor
{

    private Connection connection = SqlSessionFactory.getConnection();
    /**
     * sql执行
     * @param sql
     * @param tClass
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object doExecuteCastBean(String sql,Class<?> tClass) throws SQLException, IllegalAccessException, InstantiationException
    {
        log.info("【sql执行】:{}",sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //判断返回值是不是int类型
        if (tClass.isAssignableFrom(int.class))
        {
            return preparedStatement.executeUpdate();
        }

        // 执行放回对象
        ResultSet resultSet = preparedStatement.executeQuery();
        // 如果查询出来的条数大于1的话抛出错误
        resultSet.last(); // 将指针移动到最后
        int row = resultSet.getRow();
        SystemAssert.isTrue(row == SystemConstant.IsInt.YES.getCode(),"查询结果具有多条记录");
        resultSet.beforeFirst(); // 将指针移动到最前面 进行数据遍历
        ResultSetMetaData metaData = resultSet.getMetaData();
        // 获取列的个数
        int columnCount = metaData.getColumnCount();
        Object o  = tClass.newInstance();
        // 获取所有的属性
        Field[] declaredFields = tClass.getDeclaredFields();
        while (resultSet.next())
        {
            propertyAssignment(columnCount,declaredFields,metaData,resultSet,o);
        }
        return o;
    }
    /**
     * sql执行返回list
     * @param sql
     * @param tClass
     * @param <T>
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <T> List<T> doExecuteList (String sql,Class<T> tClass) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        log.info("【sql执行】:{}",sql);
        List list= new ArrayList();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        // 获取列的个数
        int columnCount = metaData.getColumnCount();

        // 获取所有的属性
        Field[] declaredFields = tClass.getDeclaredFields();
        while (resultSet.next())
        {
            Object o = tClass.newInstance();
            propertyAssignment(columnCount,declaredFields,metaData,resultSet,o);
            list.add(o);
        }
        return list;
    }
    /**
     * 属性赋值
     * @param columnCount
     * @param declaredFields
     * @param metaData
     * @param resultSet
     * @param o
     * @throws SQLException
     * @throws IllegalAccessException
     */
    private void propertyAssignment(int columnCount,Field[] declaredFields,ResultSetMetaData metaData,ResultSet resultSet,Object o) throws SQLException, IllegalAccessException
    {
        for (int i = 1; i <= columnCount; i++)
        {
            // 查询的结果 和对象的属性进行匹配
            for (Field declaredField : declaredFields)
            {
                if (metaData.getColumnName(i).equals(declaredField.getName()))
                {
                    Object object = resultSet.getObject(metaData.getColumnName(i));
                    declaredField.setAccessible(true);
                    declaredField.set(o,object);
                }
            }
        }
    }

    private SqlExecutor (){}

    public static SqlExecutor getInstance(){
        return new SqlExecutor();
    }




}
