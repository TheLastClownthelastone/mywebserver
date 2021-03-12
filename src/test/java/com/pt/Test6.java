package com.pt;
import com.pt.beans.TestWaitAndNotifyBean;
import com.pt.util.DateUtil;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;
/**
 * 测试Object类中的wait方法和notify方法
 */
public class Test6
{

    @Test
    public void test1() throws InterruptedException
    {
        TestWaitAndNotifyBean testWaitAndNotifyBean = new TestWaitAndNotifyBean();
        // 两个生成这生成30 次  两个消费消费30次
        new Thread(()->{
            for (int i = 0; i < 30; i++)
            {
                try
                {

                    testWaitAndNotifyBean.produce();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        },"生产者1").start();

//        new Thread(()->{
//            for (int i = 0; i < 30; i++)
//            {
//                try
//                {
//                    testWaitAndNotifyBean.produce();
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        },"生产者2").start();



        new Thread(()->{
            for (int i = 0; i < 30; i++)
            {
                try
                {
                    testWaitAndNotifyBean.consumer();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        },"消费者1").start();


//        new Thread(()->{
//            for (int i = 0; i < 30; i++)
//            {
//                try
//                {
//                    testWaitAndNotifyBean.consumer();
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        },"消费者2").start();


    }

    @Test
    public void test3(){
        System.out.println(0 % 3);
        System.out.println(282294129135018L - 282284129135018L);
        System.out.println(10000000000L / (1000L * 1000000L * 10L));
    }

    @Test
    public void test4(){
        Date date = DateUtil.HMSToDate("16:31:00");
        System.out.println(date.toGMTString());
    }


    @Test
    public void test5(){
        long javaNano = DateUtil.getJavaNano(new Date());
        System.out.println(javaNano);
    }

    @Test
    public void test6(){
        System.out.println("当前时间的纳秒值："+System.nanoTime());
        long l = TimeUnit.MILLISECONDS.toNanos(new Date().getTime());
        System.out.println("通过计算出来的纳秒值："+l);
    }

    @Test
    public void test7(){
        System.out.println("系统当前的毫秒值为：" + System.currentTimeMillis());
        System.out.println("通过date对象获取到的毫秒值为："+new Date().getTime());
    }


}
