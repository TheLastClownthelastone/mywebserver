package com.pt.executor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

import java.lang.reflect.InvocationTargetException;
/**
 * 执行器
 * @param <T>
 */
public interface Executor<T>
{
    /**
     * 同步执行方法
     * @param request
     * @return
     */
    T execute(Object ... request) throws InvocationTargetException, IllegalAccessException;

    /**
     * 异步执行方法
     * @param promise
     * @param request
     * @return
     */
    Future<T> asyncExecute(Promise<T> promise, Object... request);
}
