package com.pt.excaplem.task;
import com.pt.annotation.Component;
import com.pt.annotation.PTJob;
import com.pt.task.Task;
import com.pt.task.TaskDoType;
import com.pt.task.timer.MyTimeUnit;

@Component
@PTJob(type = TaskDoType.ACCORDING,lValue = 10L,cycle = MyTimeUnit.SECONDS)
public class MyTask implements Task
{
    @Override
    public void doExecute()
    {
        System.out.println("看我我每五秒就会执行一次》》》》》》》》》》》》》》》》》》》》》");
    }
}
