package com.pt.sql;

import com.pt.constant.SystemConstant;

import java.sql.SQLException;
import java.util.List;
public class DefaultSession implements Session
{
    private SqlExecutor sqlExecutor = SqlExecutor.getInstance();

    private SqlHandler sqlHandler = SqlHandler.getInstance();

    private DefaultSession(){

    }

    @Override
    public <T> T selectOne(String sql,Class<T> ... classes) throws IllegalAccessException, SQLException, InstantiationException
    {
        String newSql = null;
        if (classes.length > SystemConstant.IsInt.YES.getCode())
        {
            newSql = sqlHandler.assembleSql(sql,classes[1]);
        }else {
            newSql = sql;
        }
        return (T) sqlExecutor.doExecuteCastBean(newSql, classes[0]);
    }
    @Override
    public <T> T selectOne(String sql, Object param,Class<T> ... classes) throws IllegalAccessException, SQLException, InstantiationException
    {
        String newSql = null;
        if (classes.length > SystemConstant.IsInt.YES.getCode())
        {
            newSql = sqlHandler.assembleSql(sql,classes[1],param);
        }else {
            newSql = sql;
        }
        return (T) sqlExecutor.doExecuteCastBean(newSql, classes[0]);
    }
    @Override
    public <T> List<T> selectList(String sql,Class<T> ... classes) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException
    {
        String newSql = null;
        if (classes.length > SystemConstant.IsInt.YES.getCode())
        {
            newSql = sqlHandler.assembleSql(sql,classes[1]);
        }else {
            newSql = sql;
        }
        return sqlExecutor.doExecuteList(newSql, classes[0]);
    }
    @Override
    public <T> List<T> selectList(String sql, Object param,Class<T> ... classes) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException
    {
        String newSql = null;
        if (classes.length > SystemConstant.IsInt.YES.getCode())
        {
            newSql = sqlHandler.assembleSql(sql,classes[1],param);
        }else {
            newSql = sql;
        }
        return sqlExecutor.doExecuteList(newSql, classes[0]);
    }


    public static Session getInstance(){
        return DefaultSessionHolder.session;
    }

    private static final class DefaultSessionHolder{
        private static Session session = new DefaultSession();
    }





}
