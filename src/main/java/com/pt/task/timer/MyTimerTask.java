package com.pt.task.timer;
import com.pt.task.Task;
/**
 * 任务执行器实现类Runnable 接口
 */
public abstract class MyTimerTask implements Runnable, Task
{
    @Override
    public void run()
    {
        doExecute();
    }
    @Override
    public abstract void doExecute();
}
