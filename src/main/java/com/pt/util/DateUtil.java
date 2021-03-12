package com.pt.util;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
/**
 * 时间工具类
 */
@Slf4j
public class DateUtil
{
    private static String patten = "yyyy-MM-dd HH:mm:ss";

    private static long  SECONDS = 1000L;

    private static long MINUTES = SECONDS * 60L;

    private static  long HOURS = MINUTES * 60L;

    private static long DAY =  HOURS * 24;




    /**
     * 根据时间秒的字符串，获取当前时间对应的改时分秒的Date对象
     * @param timeStr
     * @return
     */
    public static Date HMSToDate(String timeStr){
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patten);
        // 获取当前时间
        Date now = new Date();
        String format = simpleDateFormat.format(now);
        String[] s = format.split(" ");
        Date date = null;
        try
        {
            String str = s[0] +" "+ timeStr;
            date = simpleDateFormat.parse(str);
        } catch (ParseException e)
        {
            log.error("【时间转化异常】:{}",e);
        }
        return date;
    }
    /**
     * 获取一天的纳秒数
     * @return
     */
    public static long getDayNano(){
        return DAY*1000000L;
    }

    /**
     * 获取一天的毫秒数
     * @return
     */
    public static long getDAYMillisecond(){
        return DAY;
    }
    /**
     *
     * @param date
     * @return
     */
    public static long getJavaNano(Date date){
        System.out.println("当前的纳秒数："+System.nanoTime());
        // 当前时间的毫秒数
        long time = date.getTime();

        long nano = time - parse("1970-01-01 00:00:00").getTime();


        return nano;
    }

    public static Date parse(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patten);
        try
        {
            return simpleDateFormat.parse(str);
        } catch (ParseException e)
        {
            log.error("【时间格式异常】：{}",e);
            return null;
        }
    }

}
