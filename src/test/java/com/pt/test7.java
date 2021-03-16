package com.pt;
import org.junit.Test;

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




}
