package com.pt.sql;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * 使用原生java连接数据库
 */
public class TestSqlCoon
{
    // jdbc连接url
    private String url = "jdbc:mysql://localhost:3306/mywebserver?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    // 数据库用户名
    private String username = "root";
    // 数据库密码
    private String password = "123456";
    // 驱动类
    private String driverClass = "com.mysql.cj.jdbc.Driver";




    @Test
    public void test1(){

        Connection connection = null;
        try
        {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values ('3','aa')");
            preparedStatement.execute();
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2(){
        Connection connection = null;
        try
        {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("select  *  from user");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println("查询出的条数为："+columnCount);
            String columnName = metaData.getColumnName(1);
            System.out.println(columnName);
            resultSet.last();
            int row = resultSet.getRow();
            System.out.println("数据行数："+row);
            resultSet.beforeFirst();
            while (resultSet.next()){
                System.out.println("---------------------");
                for (int i = 1; i <=columnCount; i++)
                {
                    System.out.println(metaData.getColumnName(i)+"："+resultSet.getObject(metaData.getColumnName(i)));
                }
                System.out.println("---------------------");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void test3() throws InstantiationException, IllegalAccessException
    {
        List<String> list = null;
        Class<? extends List> aClass = list.getClass();
        ParameterizedType genericSuperclass = (ParameterizedType) aClass.getGenericSuperclass();
        System.out.println(genericSuperclass.getActualTypeArguments()[0]);
    }


    private <T> List<T> getType(Class<T> tClass) throws IllegalAccessException, InstantiationException
    {
        System.out.println(tClass.getGenericSuperclass());
        System.out.println(Arrays.toString(tClass.getGenericInterfaces()));
        return (List<T>) tClass.newInstance();
    }



}
