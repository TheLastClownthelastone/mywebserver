package com.pt.sql;
import com.pt.exception.PtException;
import com.pt.util.YmlUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * 用来管理数据库连接，以及生产session
 */
@Slf4j
public class SqlSessionFactory
{
    private static String url = YmlUtil.getString("pt.datasource.url");
    private static String username = YmlUtil.getString("pt.datasource.username");
    private  static String password = YmlUtil.getString("pt.datasource.password");
    private static String driverClass = YmlUtil.getString("pt.datasource.driverClass");


    private static Connection connection;
    /**
     * 在静态块中进行加载数据库连接
     */
    static {
        try
        {
            Class.forName(driverClass);
            setConnection();
        } catch (ClassNotFoundException e)
        {
            throw PtException.error(e.getMessage());
        }
    }
    /**
     * 获取连接
     * @return
     */
    private static void setConnection(){
        try
        {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e)
        {
            throw PtException.error(e.getMessage());
        }
    }
    /**
     * 获取session
     * @return
     */
    public Session getSession(){
        return DefaultSession.getInstance();
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection getConnection(){
        return connection;
    }

}
