package com.pt.beans;
import java.util.Date;
public class TestBean2
{
    private String id;
    private String name;
    private int age;
    private String sex;

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public int getAge()
    {
        return age;
    }
    public void setAge(int age)
    {
        this.age = age;
    }
    public String getSex()
    {
        return sex;
    }
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    private void privateMethod(){
        System.out.println("私有方法");
    }


    public Date setDate (){
        return null;
    }
}
