package com.pt.executor;
import com.pt.aspct.Advice;
import com.pt.exception.SystemStatusEnum;
import com.pt.parameter.DefaultParameterAnalysis;
import com.pt.parameter.ParameterAnalysis;
import com.pt.router.RouterInfo;
import com.pt.util.HttpUtil;
import com.pt.util.SystemAssert;
import io.netty.handler.codec.http.*;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
public class HttpResponseExecutor extends AbstractExecutor<HttpResponse> implements Advice
{

    private ParameterAnalysis parameterAnalysis = DefaultParameterAnalysis.getInstance();

    private  HttpResponseExecutor(){}

    @Override
    public HttpResponse doExecute(Object... request) throws InvocationTargetException, IllegalAccessException
    {
        RouterInfo routerInfo = HttpUtil.getRouterInfo(context, request);
        Object info = routerInfo.getInfo();
        Method method = routerInfo.getMethod();
        FullHttpRequest overallRequest = (FullHttpRequest) request[0];
        if (context.isAspect())
        {
            try
            {
                // 如果是请求的方式跟@RequestMapping 没有对应上直接报错
                SystemAssert.isTrue(routerInfo.getRequiar().name().equals(overallRequest.method().name()), SystemStatusEnum.HTTP_REQUEST_MODE_ERROR);
                before(request[0],method,method.getParameters(),null);
                Object invoke = methodInvoke(method, info, overallRequest);
                after(request[0],method,method.getParameters(),null);
                return HttpUtil.buildResponse(invoke);
            }catch (Error e){
                exception(request[0],method,method.getParameters(),null,e);
                return HttpUtil.build_500();
            }
        }else {
            // 404
            if (routerInfo == null)
            {
                return HttpUtil.build_404();
            }
            Object invoke = methodInvoke(method, info, overallRequest);
            return HttpUtil.buildResponse(invoke);
        }
    }
    /**
     * 方法执行
     * @param method
     * @param info
     * @param request
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object methodInvoke(Method method,Object info,FullHttpRequest request) throws InvocationTargetException, IllegalAccessException
    {
        Class paramClassType = getParamClassType(method);
        Object invoke = null;
        if (paramClassType != null)
        {
            // 获取参数形参中的值
            Object paramValue = parameterAnalysis.getParamToBean(request, paramClassType, method);
            invoke = method.invoke(info,paramValue);
        }else {
            invoke = method.invoke(info);
        }
        return invoke;
    }




    /**
     * 提供获取该实例的方法
     * @return
     */
    public static HttpResponseExecutor getInstance(){
        return HttpResponseExecutorHodler.instance;
    }
    @Override
    public void before(Object obj, Method method, Object[] args, MethodProxy proxy)
    {
        List<Advice> beanByType = context.getBeanByType(Advice.class);
        for (Advice advice : beanByType)
        {
            advice.before(obj,method,args,proxy);
        }
    }
    @Override
    public void after(Object obj, Method method, Object[] args, MethodProxy proxy)
    {
        List<Advice> beanByType = context.getBeanByType(Advice.class);
        for (Advice advice : beanByType)
        {
            advice.after(obj,method,args,proxy);
        }
    }
    @Override
    public void exception(Object obj, Method method, Object[] args, MethodProxy proxy, Throwable throwable)
    {
        List<Advice> beanByType = context.getBeanByType(Advice.class);
        for (Advice advice : beanByType)
        {
            advice.exception(obj,method,args,proxy,throwable);
        }
    }
    /**
     * 私有静态内部类禁止该类被实例化
     */
    private static final class HttpResponseExecutorHodler{
        private static HttpResponseExecutor instance = new HttpResponseExecutor();
    }

    /**
     * 获取方法形参类型
     * @param method
     * @return
     */
    private Class getParamClassType(Method method){
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length > 0 ? parameterTypes[0]:null;
    }

}
