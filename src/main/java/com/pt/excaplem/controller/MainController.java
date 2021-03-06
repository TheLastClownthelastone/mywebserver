package com.pt.excaplem.controller;
import com.pt.annotation.AutoWire;
import com.pt.annotation.Controller;
import com.pt.annotation.RequestBody;
import com.pt.annotation.RequestMapping;
import com.pt.annotation.Value;
import com.pt.constant.SystemConstant;
import com.pt.excaplem.bean.Man;
import com.pt.excaplem.bean.User;
import com.pt.excaplem.service.MainService;
import com.pt.util.CommonResult;

import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/pt")
public class MainController
{
    @Value("ab.cd.ef.gh.nr")
    private String value;

    @AutoWire
    private MainService mainService;

    @RequestMapping(value = "/say",method = SystemConstant.HttpMethod.GET)
    public void setOne(){
        String one = mainService.getOne();
        System.out.println("执行service方法获取的值为："+one);
    }

    @RequestMapping(value = "/getMan" ,method = SystemConstant.HttpMethod.GET)
    public Man getMan(){
        return mainService.getMan();
    }

    @RequestMapping(value = "/getValue",method = SystemConstant.HttpMethod.GET)
    public CommonResult<String> getValue(){
        return CommonResult.success(value);
    }

    @RequestMapping(value = "getAllUser",method = SystemConstant.HttpMethod.GET)
    public CommonResult<List<User>> getAllUser(){
        return CommonResult.success(mainService.getAllUser());
    }


    @RequestMapping(value = "hgetAll",method = SystemConstant.HttpMethod.GET)
    public CommonResult<List<Map<String,String>>> hgetAll(){
        return CommonResult.success(mainService.hgetAll());
    }
}
