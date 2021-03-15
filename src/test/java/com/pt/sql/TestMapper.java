package com.pt.sql;
import org.junit.Test;

import java.util.List;
public class TestMapper
{
    @Test
    public void test1(){
        UserMapper userMapper = SqlAnnoHandler.getInstance().executeSqlToBean(UserMapper.class);
        List<User> users = userMapper.getUsers();
        System.out.println(users);
    }

    @Test
    public void test2(){
        UserMapper userMapper = SqlAnnoHandler.getInstance().executeSqlToBean(UserMapper.class);
        User user = new User();
        user.setId("1");
        User one = userMapper.getOne(user);
        System.out.println(one);
    }
}
