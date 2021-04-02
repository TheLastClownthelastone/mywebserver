package com.pt.sql;

import com.mysql.cj.jdbc.NonRegisteringDriver;
import org.junit.Test;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.*;
import java.util.*;

/**
 * 使用原生java连接数据库
 */
public class TestSqlCoon {
    // jdbc连接url
    private String url = "jdbc:mysql://localhost:3306/mywebserver?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    // 数据库用户名
    private String username = "root";
    // 数据库密码
    private String password = "123456";
    // 驱动类
    private String driverClass = "com.mysql.cj.jdbc.Driver";


    @Test
    public void test1() {

        Connection connection = null;
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values ('3','aa')");
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2() {
        Connection connection = null;
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("select  *  from user");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println("查询出的条数为：" + columnCount);
            String columnName = metaData.getColumnName(1);
            System.out.println(columnName);
            resultSet.last();
            int row = resultSet.getRow();
            System.out.println("数据行数：" + row);
            resultSet.beforeFirst();
            while (resultSet.next()) {
                System.out.println("---------------------");
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(metaData.getColumnName(i) + "：" + resultSet.getObject(metaData.getColumnName(i)));
                }
                System.out.println("---------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void test3() throws InstantiationException, IllegalAccessException {
        List<String> list = null;
        Class<? extends List> aClass = list.getClass();
        ParameterizedType genericSuperclass = (ParameterizedType) aClass.getGenericSuperclass();
        System.out.println(genericSuperclass.getActualTypeArguments()[0]);
    }


    private <T> List<T> getType(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        System.out.println(tClass.getGenericSuperclass());
        System.out.println(Arrays.toString(tClass.getGenericInterfaces()));
        return (List<T>) tClass.newInstance();
    }

    @Test
    public void test6() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Properties info = new Properties();
        info.put("user", username);
        info.put("password", password);
        NonRegisteringDriver nonRegisteringDriver = (NonRegisteringDriver) drivers.nextElement();
        DriverPropertyInfo[] propertyInfo = nonRegisteringDriver.getPropertyInfo(url, info);
        for (DriverPropertyInfo driverPropertyInfo : propertyInfo) {
//            System.out.println(driverPropertyInfo.name);
            System.out.println(driverPropertyInfo.value);
//            System.out.println(driverPropertyInfo.description);
//            System.out.println(driverPropertyInfo.required);
        }
    }


    @Test
    @CallerSensitive
    public void test4() throws ClassNotFoundException {
        Class.forName("com.pt.sql.A");
        edo(Reflection.getCallerClass());
    }

    public void edo(Class<?> c) {
        try {
            A a = (A) c.newInstance();
            a.aaex();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}

class A {
    private String ss = "dfafdsaf";

    public void aaex() {
        System.out.println(ss);
    }

}