package com.pt.executor;
import com.pt.context.BeanContext;
import com.pt.context.DefaultBeanContext;
import com.pt.util.CommonResult;
import com.pt.util.HttpUtil;
import com.pt.util.YmlUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
/**
 * 定义抽象执行执行器，子类实现指定的泛型
 * @param <T>
 */
@Slf4j
public abstract class AbstractExecutor <T> implements Executor<T>
{

    protected BeanContext context = DefaultBeanContext.getInstance();



    @Override
    public T execute(Object... request) throws InvocationTargetException, IllegalAccessException
    {
        boolean bool = YmlUtil.getBoolean("server.exception.capture");
        // 开启全局异常处理
        if (bool)
        {
            T t;
            try
            {
                t = doExecute(request);
            } catch (Exception e){
                CommonResult error = CommonResult.error(e);
                e.printStackTrace();
                t = (T) HttpUtil.buildResponse(error);
            }
            return t;
        }else {
            return doExecute(request);
        }
    }

    /**
     * 增加子类中异步实现
     * @param promise
     * @param request
     * @return
     */
    @Override
    public Future<T> asyncExecute(Promise<T> promise, Object... request)
    {

        return null;
    }
    /**
     * 子类具体实现的执行方法
     * @param request
     * @return
     */
    public abstract T doExecute(Object... request) throws InvocationTargetException, IllegalAccessException;



}
