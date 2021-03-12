package com.pt.parameter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

import java.lang.reflect.Method;
import java.util.Map;
/**
 * 参数解析器
 */
public interface ParameterAnalysis
{
    /**
     * 将request对象中的参数进行解析成javabean
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getParamToBean(FullHttpRequest request, Class<T> clazz, Method method);


    /**
     * 将request对象中的对象解析成map
     * @param request
     * @return
     */
    Map<String,Object> getParamToMap(FullHttpRequest request);



}
