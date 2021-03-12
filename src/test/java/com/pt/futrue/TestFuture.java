package com.pt.futrue;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/**
 * 测试java中的future对象
 * 将部分代码放入异步线程中跟主线程是串行操作，最终通过get方法将异步结果返回到主线程中
 */
public class TestFuture
{
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    @Test
    public void test() throws InterruptedException, ExecutionException, TimeoutException
    {
        Future<Integer> calculate = calculate(10);
        System.out.println("--------------------------------------");
        System.out.println("执行主线程,阻塞3秒钟");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("--------------------------------------");
        System.out.println("获取异步线程中的结果");
        Integer integer = calculate.get(1,TimeUnit.SECONDS);
        System.out.println("得到最终的结果为："+integer);
    }
    /**
     * get方法获取结果如果是没有参数参数的话
     * 表示阻塞超时不会抛出异常
     *
     * 两个参数 1，时间数值， 2时间单位
     * 如果是get阻塞的时间超过了改时间的话会抛出异常
     * @param input
     * @return
     */
    private static Future<Integer> calculate(Integer input){
        System.out.println("开始执行异步线程");
        Future<Integer> submit = executor.submit(() ->
        {
            try
            {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return input * input;
        });
        return submit;
    }

}
