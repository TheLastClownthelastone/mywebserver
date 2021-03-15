package com.pt.sql;
import com.pt.annotation.Mapper;
import com.pt.annotation.Select;

import java.util.List;
@Mapper
public interface UserMapper
{

    @Select("select * from user")
    List<User> getUsers();

    @Select("select * from user where id =#{id}")
    User getOne(User user);
}
