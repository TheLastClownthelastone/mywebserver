package com.pt.aspct;
import com.pt.annotation.Aspect;
import com.pt.annotation.Component;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
/**
 * 默认的advice 进行日志处理
 */
@Slf4j
@Aspect
@Component
public class DefaultAdvice implements LogginAdvice
{
    @Override
    public void before(Object obj, Method method, Object[] args, MethodProxy proxy)
    {

        HttpRequest request = (HttpRequest) obj;
        log.info("【请求的URL】：{}",request.getUri());
        log.info("【请求的方法名称】：{}",obj.getClass().getSimpleName()+"."+method.getName());
        log.info("【请求的参数】：{}",args);
    }
    @Override
    public void after(Object obj, Method method, Object[] args, MethodProxy proxy)
    {
    }
    @Override
    public void exception(Object obj, Method method, Object[] args, MethodProxy proxy, Throwable throwable)
    {
    }
}
