package com.pt.excaplem;
import com.pt.annotation.Mapper;
import com.pt.annotation.Select;
import com.pt.excaplem.bean.User;

import java.util.List;
@Mapper
public interface UserMapper
{

    @Select("select * from user")
    public List<User> getAll();
}
