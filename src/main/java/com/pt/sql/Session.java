package com.pt.sql;
import java.sql.SQLException;
import java.util.List;
/**
 * sql会话对象
 */
public interface Session
{
    /**
     * 查询一条记录
     * @param sql
     * @param <T>
     * @return
     */
    <T> T selectOne(String sql,Class<T> ... classes) throws IllegalAccessException, SQLException, InstantiationException;
    /**
     * 查询一条记录
     * @param sql
     * @param param
     * @param <T>
     * @return
     */
    <T> T selectOne(String sql,Object param,Class<T> ... classes) throws IllegalAccessException, SQLException, InstantiationException;
    /**
     * 查询多条记录
     * @param sql
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String sql,Class<T> ... classes) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException;

    /**
     * 查询多条记录
     * @param sql
     * @param param
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String sql,Object param,Class<T> ... classes) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException;


}
