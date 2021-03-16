package com.pt.excaplem.service;
import com.pt.annotation.AutoWire;
import com.pt.annotation.Service;
import com.pt.excaplem.UserMapper;
import com.pt.excaplem.bean.Man;
import com.pt.excaplem.bean.User;
import com.pt.redis.RedisTemplate;

import java.util.List;
import java.util.Map;
@Service
public class MainService
{
   @AutoWire
   private UserMapper userMapper;

   @AutoWire
   private RedisTemplate redisTemplate;

    public String getOne(){
        return "1111";
    }

    public Man  getMan(){
        Man man = new Man();
        man.setId("111");
        man.setName("pt");
        return man;
    }

    public List<User> getAllUser(){
        return userMapper.getAll();
    }

    public List<Map<String,String>> hgetAll(){
        return redisTemplate.hgetAll("user:1");
    }

}
