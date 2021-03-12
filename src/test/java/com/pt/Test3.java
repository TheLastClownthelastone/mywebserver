package com.pt;
import com.pt.util.YmlUtil;
import org.junit.Test;
/**
 * 测试工具类
 */
public class Test3
{

    @Test
    public void test1(){
        Object value = YmlUtil.getValue("ab.cd.ef.gh.nr");
        System.out.println(value);
        Object value1 = YmlUtil.getValue("");
        System.out.println(value1);
    }
}
