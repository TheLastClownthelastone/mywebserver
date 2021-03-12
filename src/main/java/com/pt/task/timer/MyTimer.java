package com.pt.task.timer;
import com.pt.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;
/**
 * 阅读完jdk定时器源码之后，实现自定义定时器
 */
@Slf4j
public class MyTimer
{


    // 声明任务队列
    private TimerQueue queue = new TimerQueue();
    // 声明任务执行器
    private TimerThread thread = new TimerThread(queue);
    /**
     * 设置内置线程名称并且线程启动
     * @param name
     */
    public MyTimer(String name){
        thread.setName(name);
        thread.start();
    }

    public MyTimer(){
        this("【PT-TIME—STARTER】");
    }


    /**
     * 内部类集成Thread 定时器中的线程
     */
    class TimerThread extends  Thread{

        private TimerQueue queue;
        /**
         * 声明有参构造将任务队列放入任务执行器中
         * @param queue
         */
        TimerThread(TimerQueue queue){
            this.queue = queue;
        }

        /**
         * 重写改线程类的run方法
         */
        @Override
        public void run()
        {
            loop();
        }

        void loop(){
            while (true){
                synchronized (queue){
                    try
                    {
                        queue.wait();
                        // TODO  加上判断
                        log.info("任务：{}开始执行>>>>>>>>>>>>>>>>",Thread.currentThread().getName());
                        queue.queue[0].run();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }finally
                    {
                        if (!queue.isImplement)
                        {
                            break;
                        }
                    }
                }
            }
        }



    }
    /**
     * 该内部类主要用来作为任务队列
     */
    class TimerQueue {
        // 声明任务数组,参考jdk源码给定长度为128
        private  MyTimerTask[] queue = new MyTimerTask[128];

        private boolean isImplement  = true;

    }
    /**
     * 延后多久开始执行
     * @param myTimerTask
     * @param delayTime
     */
    public void schedule(MyTimerTask myTimerTask,long delayTime){
        queue.queue[0] = myTimerTask;
        long l = System.currentTimeMillis()+delayTime;
        while (true){
            long millis = System.currentTimeMillis();
            // 使用wait 方法或者是notify方法的是要进行上锁不然或抛出 java.lang.IllegalMonitorStateException 异常
            synchronized (queue){
                if (millis == l)
                {
                    // 通过改对象唤醒 被wait的线程
                    queue.notifyAll();
                    queue.isImplement = false;
                    break;
                }else {
                    continue;
                }
            }
        }
    }
    /**
     * 执行方法
     * 根据计算多久执行一次
     * @param myTimerTask
     * @param num
     * @param timeUnit
     */
    public void schedule(MyTimerTask myTimerTask , long num, MyTimeUnit timeUnit) throws InterruptedException
    {
        queue.queue[0] = myTimerTask;
        // 当前毫秒数
        long l = System.nanoTime();
        while (true){

            long l1 = System.nanoTime();
           long check = l1 - l;

           synchronized (queue){
               // 秒
               if (timeUnit.equals(MyTimeUnit.SECONDS))
               {
                   if (check % (num*1000L*1000000L) == 0 && check != 0L)
                   {
                       // 通过改对象唤醒 被wait的线程
                       queue.notify();
                   }else {
                       continue;
                   }
               }
               // 分钟
               if (timeUnit.equals(MyTimeUnit.MINUTES))
               {
                   if (check % (num*1000L*1000000L*60L) == 0 && check != 0L)
                   {
                       // 通过改对象唤醒 被wait的线程
                       queue.notify();
                   }else {
                       continue;
                   }
               }
               // 小时
               if (timeUnit.equals(MyTimeUnit.HOURS))
               {
                   if (check % (num*1000L*1000000L*60L*60L) == 0 && check != 0L)
                   {
                       // 通过改对象唤醒 被wait的线程
                       queue.notify();
                   }else {
                       continue;
                   }
               }
               // 天
               if (timeUnit.equals(MyTimeUnit.DAYS))
               {
                   if (check % (num*1000L*1000000L*60L*60L*24L) == 0 && check != 0L)
                   {
                       // 通过改对象唤醒 被wait的线程
                       queue.notify();
                   }else {
                       continue;
                   }
               }
           }
       }
    }
    /**
     * 指定每天对应的时间执行任务
     * @param dateTime 时间字符串
     */
    public void schedule(MyTimerTask myTimerTask,String dateTime) throws InterruptedException
    {
        queue.queue[0] = myTimerTask;
        Date date = DateUtil.HMSToDate(dateTime);
        // 获取系统的毫秒数
        long time = date.getTime();
        while (true){
            long l = System.currentTimeMillis();
            long check = l - time;
            synchronized (queue)
            {
                if (check == 0 || check % DateUtil.getDAYMillisecond() == 0)
                {
                    queue.notify();
                    // 执行成功之后休眠 八秒钟
                    TimeUnit.SECONDS.sleep(8);
                }
            }

        }
    }



}
