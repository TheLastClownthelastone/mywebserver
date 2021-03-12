package com.pt.task;
import com.pt.annotation.PTJob;
import lombok.Data;
/**
 * 任务info
 */
@Data
public class TaskInfo
{
    private PTJob ptJob;

    private Task task;
}
