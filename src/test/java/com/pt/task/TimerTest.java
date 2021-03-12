package com.pt.task;
import com.pt.task.timer.MyTimeUnit;
import com.pt.task.timer.MyTimer;
import com.pt.task.timer.MyTimerTask;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
public class TimerTest
{


    @Test
    public void test3(){
        long l = TimeUnit.SECONDS.toDays(60*60*24);
        System.out.println(l);
    }


    @Test
    public void test4() throws InterruptedException
    {
       MyTimer myTimer = new com.pt.task.timer.MyTimer();
        myTimer.schedule(new MyTimerTask()
                         {
                             @Override
                             public void doExecute()
                             {
                                 System.out.println(System.currentTimeMillis()+":"+"每10秒钟执行一次");
                             }
                         },
                10,
                MyTimeUnit.SECONDS);
    }
    @Test
    public void test5() throws InterruptedException
    {
        com.pt.task.timer.MyTimer myTimer = new MyTimer();
        myTimer.schedule(new MyTimerTask()
        {
            @Override
            public void doExecute()
            {
                System.out.println("我执行了在09:26:00");
            }
        },"09:26:00");
    }
}
