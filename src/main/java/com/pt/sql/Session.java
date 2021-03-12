package com.pt.sql;
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
    <T> T selectOne(String sql);
    /**
     * 查询一条记录
     * @param sql
     * @param param
     * @param <T>
     * @return
     */
    <T> T selectOne(String sql,Object param);
    /**
     * 查询多条记录
     * @param sql
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String sql);

    /**
     * 查询多条记录
     * @param sql
     * @param param
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String sql,Object param);


}
