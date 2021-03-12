package com.pt.sql;

import java.util.List;
public class DefaultSession implements Session
{
    private DefaultSession(){

    }

    @Override
    public <T> T selectOne(String sql)
    {
        return null;
    }
    @Override
    public <T> T selectOne(String sql, Object param)
    {
        return null;
    }
    @Override
    public <T> List<T> selectList(String sql)
    {
        return null;
    }
    @Override
    public <T> List<T> selectList(String sql, Object param)
    {
        return null;
    }


    public static Session getInstance(){
        return DefaultSessionHolder.session;
    }

    private static final class DefaultSessionHolder{
        private static Session session = new DefaultSession();
    }





}
