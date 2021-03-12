package com.pt.beans;
public class TestWaitAndNotifyBean
{
    private Integer num = 0;

    /**
     * 生产者
     */
    public synchronized void produce() throws InterruptedException
    {
        // 不为的0的话停止生产
        while (num != 0)
        {
            this.wait();
        }
        // 生产
        num++;
        System.out.println(Thread.currentThread().getName() + "的资源数为：" + num);
        // 释放
        this.notifyAll();
    }

    /**
     * 消费者
     */
    public synchronized void consumer() throws InterruptedException
    {
        // 为0的话停止消费
        while (num == 0)
        {
            this.wait();
        }

        // 消费
        num --;
        System.out.println(Thread.currentThread().getName() + "的资源数为：" + num);

        // 释放
        this.notifyAll();
    }
}
