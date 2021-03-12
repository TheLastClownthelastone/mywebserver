package com.pt.handler;
/**
 * 处理器接口
 */
public interface Handler<T>
{
    /**
     * 处理方法
     */
    void handler(T t);


}
