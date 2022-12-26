package com.pt;


import com.pt.util.YmlUtil;
import org.junit.Test;

import java.util.List;

//BIO,NIO,AIO
public class Test10 {


    @Test
    public void test1(){
        Object value = YmlUtil.getValue("array.zz");
        System.out.println(value);
        if (value instanceof List) {
            System.out.println(true);
            List<String> value1 = (List<String>) value;
            System.out.println(value1);
        }

    }


}
