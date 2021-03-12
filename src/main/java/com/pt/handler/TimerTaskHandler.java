package com.pt.handler;

import com.pt.annotation.PTJob;
import com.pt.exception.SystemStatusEnum;
import com.pt.task.Task;
import com.pt.task.TaskDoType;
import com.pt.task.TaskInfo;
import com.pt.task.timer.MyTimeUnit;
import com.pt.task.timer.MyTimer;
import com.pt.task.timer.MyTimerTask;
import com.pt.util.SystemAssert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
/**
 * 定时任务执行器
 */
@Data
@Slf4j
public class TimerTaskHandler implements Handler<ThreadPoolExecutor>
{
    /**
     * 定时任务
     */
    private List<TaskInfo> taskInfos;

    private  TimerTaskHandler(){}

    @Override
    public void handler(ThreadPoolExecutor threadPoolExecutor)
    {
        for (TaskInfo taskInfo : taskInfos)
        {

            PTJob ptJob = taskInfo.getPtJob();
            Task task = taskInfo.getTask();
            TaskDoType type = ptJob.type();
            if (TaskDoType.ACCORDING.equals(type))
            {
                according(threadPoolExecutor,task,ptJob.lValue(),ptJob.cycle());
            }
            if (TaskDoType.SEPARATE.equals(type))
            {
                separate(threadPoolExecutor,task,ptJob.lValue());
            }
            if (TaskDoType.SLICE.equals(type))
            {
                 slice(threadPoolExecutor,task,ptJob.time());
            }
        }
    }

    public static Handler getInstance(Object... obj)
    {
        SystemAssert.isTrue(obj.length > 0, SystemStatusEnum.UNTIMELY_TASK);
        List<TaskInfo> taskInfos = (List<TaskInfo>) obj[0];
        TimerTaskHandler handler = TimerTaskHandlerHodler.handler;
        handler.setTaskInfos(taskInfos);
        return handler;
    }
    private static final class TimerTaskHandlerHodler{
        private static  TimerTaskHandler handler = new TimerTaskHandler();
    }
    /**
     * 延后多久开始执行 以毫秒做为单位
     * @param threadPoolExecutor
     * @param task
     * @param time
     */
    private void separate(ThreadPoolExecutor threadPoolExecutor,Task task, long time){
        SystemAssert.isTrue(time != 0,"定时配置错误错误");
        Thread thread = new Thread(() ->
        {
            MyTimer myTimer = new MyTimer();
            myTimer.schedule(new MyTimerTask()
            {
                @Override
                public void doExecute()
                {
                    task.doExecute();
                }
            }, time);
        });
        threadPoolExecutor.execute(thread);
    }
    /**
     * 根据时间单位计算多久执行一次
     * @param threadPoolExecutor
     * @param task
     * @param sum
     * @param timeUnit
     */
    private void according(ThreadPoolExecutor threadPoolExecutor, Task task, long sum, MyTimeUnit timeUnit){
        SystemAssert.isTrue(sum != 0,"定时配置错误错误");
        SystemAssert.isTrue(timeUnit !=null,"定时配置错误错误");
        Thread thread = new Thread(() ->
        {
            MyTimer myTimer = new MyTimer();
            try
            {
                myTimer.schedule(new MyTimerTask()
                {
                    @Override
                    public void doExecute()
                    {
                        task.doExecute();
                    }
                }, sum,timeUnit);
            } catch (InterruptedException e)
            {
                log.error("{}:【任务执行失败】:{}",Thread.currentThread().getName(),e);
            }
        });
        threadPoolExecutor.execute(thread);
    }
    /**
     * 指定每天对应的时间执行任务
     * @param threadPoolExecutor
     * @param task
     * @param dateTime
     */
    public void slice(ThreadPoolExecutor threadPoolExecutor,Task task,String dateTime){
        SystemAssert.isTrue(dateTime != null,"定时配置错误错误");
        Thread thread = new Thread(() ->
        {
            MyTimer myTimer = new MyTimer();
            try
            {
                myTimer.schedule(new MyTimerTask()
                {
                    @Override
                    public void doExecute()
                    {
                        task.doExecute();
                    }
                }, dateTime);
            } catch (InterruptedException e)
            {
                log.error("{}:【任务执行失败】:{}",Thread.currentThread().getName(),e);
            }
        });
        threadPoolExecutor.execute(thread);
    }



}
