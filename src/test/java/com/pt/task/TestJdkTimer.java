package com.pt.task;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 测试jdk中自带的定时器
 */
@Slf4j
public class TestJdkTimer
{

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (System.currentTimeMillis() - scheduledExecutionTime() >= 500) {
                return; // Too late; skip this execution.
            }
            // Perform the task
            System.out.println("This is my timer task." + new Date());
        }
    }

    @Test
    public void tes1(){
        testTimer();
    }

    public void testTimer() {
        Timer timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();

        timer.schedule(myTimerTask, 1000, 3000); // delay 1 秒后，每秒运行一次
        timer.cancel(); // 主线程醒来，结束timer
    }
}
