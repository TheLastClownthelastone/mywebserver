package com.pt;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 测试java中的countDownLatch
 * 主线程中声明CountDownLatch（次数）
 * 调用countDown 方法的话次数会递减 当减到0的时候主线程的await方法才会结束阻塞
 */
public class test7
{
    private volatile String name;

    private CountDownLatch latch = new CountDownLatch(2);

    @Test
    public void test1(){
        final CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程开始执行…… ……");
        //第一个子线程执行
        ExecutorService es1 = Executors.newSingleThreadExecutor();
        es1.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println("子线程："+Thread.currentThread().getName()+"执行");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });
        es1.shutdown();

        //第二个子线程执行
        ExecutorService es2 = Executors.newSingleThreadExecutor();
        es2.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("子线程："+Thread.currentThread().getName()+"执行");
                latch.countDown();
            }
        });
        es2.shutdown();
        System.out.println("等待两个线程执行完毕…… ……");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("两个子线程都执行完毕，继续执行主线程");
    }

    @Test
    public void test2() throws InterruptedException
    {
        System.out.println("开始执行》》》》》");
        new Thread(() ->
        {
            try
            {
                System.out.println(Thread.currentThread().getName()+"：开始执行》》》》》");
                TimeUnit.SECONDS.sleep(3);
                name = "pt";
                latch.countDown();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        },"线程1").start();

        new Thread(()->{
            try
            {
                System.out.println(Thread.currentThread().getName()+"：开始执行》》》》》");
                TimeUnit.SECONDS.sleep(3);
                name = "onePrice";
                latch.countDown();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        },"线程2").start();
        System.out.println("等待线程执行完毕");
        latch.await();
        System.out.println(name);
    }


    @Test
    public void test3(){
        String hello = "Hello", lo = "lo";
        System.out.println(hello == ("Hel"+lo));
        System.out.println(hello == ("Hel"+lo).intern());
        System.out.println(hello == ("Hel"+lo).toString());
        System.out.println(hello == String.valueOf("Hel"+lo));
    }

    @Test
    public void test4(){
        String str = "<Target name=\"${0}\" dz10MV=\"${1}\" dz20MV=\"${2}\" dz30MV=\"${3}\" dz50MV=\"${4}\" ny10MV=\"${5}\" ny20MV=\"${6}\" ny30MV=\"${7}\"  ny50MV=\"${8}\" wd10MV=\"${9}\"/>";

        List<String> strings = Arrays.asList("山地或地形复杂地区调增  0.133	0.112	0.105	0.103	0.157	0.132	0.125	0.126",
                "高海拔地区调增  0.322	0.26	0.237	0.235	0.355	0.289	0.266	0.268",
                "独立基础调增  0.188	0.181	0.188	0.188					             ",
                "PHC 桩（桩长 5M）调增  0.41	0.403	0.41	0.372					             ",
                "砖混大棚调增  				0.276	0.276	0.276	0.257	             ",
                "混凝土屋面调增  								0.406                        "
        );
        for (String string : strings) {
            String[] split = string.split("\\s+");

            String rep = rep(str, split, 0);
            System.out.println(rep);
        }

    }

    private String rep(String string ,String[] split,int i){
        String replace = string.replace("${" + i + "}", split[i]);
        i++;
        if (replace.contains("$")) {
            return rep(replace,split,i);
        }
        return  replace;
    }


}
