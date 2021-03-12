package com.pt.util;
import com.pt.exception.PtException;
import com.pt.exception.SystemStatusEnum;
/**
 * 系统断言工具
 */
public class SystemAssert
{
    /**
     * 如果第一个参数为false 抛出异常
     * 否则继续往下执行
     * @param flag
     * @param statusEnum
     */
    public  static void isTrue(boolean flag, SystemStatusEnum statusEnum){
        if (!flag)
        {
            throw PtException.error(statusEnum);
        }
    }
    /**
     * 如果第一个参数为false 抛出异常
     * 否则继续往下执行
     * @param flag
     * @param msg
     */
    public static void isTrue(boolean flag,String msg){
        if (!flag)
        {
            throw PtException.error(msg);
        }
    }

}
