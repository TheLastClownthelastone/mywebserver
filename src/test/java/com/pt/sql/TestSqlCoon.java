package com.pt.sql;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values ('1','pt')");
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



}
