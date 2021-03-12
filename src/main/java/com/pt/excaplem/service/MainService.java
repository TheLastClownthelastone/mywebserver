package com.pt.excaplem.service;
import com.pt.annotation.Service;
import com.pt.excaplem.bean.Man;
@Service
public class MainService
{
    public String getOne(){
        return "1111";
    }

    public Man  getMan(){
        Man man = new Man();
        man.setId("111");
        man.setName("pt");
        return man;
    }
}
