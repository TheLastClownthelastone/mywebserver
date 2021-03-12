package com.pt.task;
/**
 * 定时任务执行方式
 */
public enum TaskDoType
{
    // 延后多久开始执行 以毫秒做为单位
    SEPARATE,
    // 执行方法
    //     * 根据计算多久执行一次
    ACCORDING,
    // 指定每天对应的时间执行任务 以天作为单位
    SLICE

}
