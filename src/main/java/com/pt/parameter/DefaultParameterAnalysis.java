package com.pt.parameter;
import com.alibaba.fastjson.JSONObject;
import com.pt.annotation.RequestBody;
import com.pt.constant.SystemConstant;
import com.pt.exception.PtException;
import com.pt.exception.SystemStatusEnum;
import com.pt.util.BeanUtil;
import com.pt.util.HttpUtil;
import com.pt.util.SystemAssert;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
/**
 * 默认参数解析器
 */
public class DefaultParameterAnalysis implements ParameterAnalysis
{
    private DefaultParameterAnalysis(){}

    public static ParameterAnalysis getInstance(){
        return DefaultParameterAnalysisHodler.parameterAnalysis;
    }

    @Override
    public <T> T getParamToBean(FullHttpRequest request, Class<T> clazz, Method method)
    {
        String type = getMethodType(request);
        // 处理get请求的参数
        if (SystemConstant.HttpMethod.GET.name().equals(type))
        {
            String uri = request.uri();
            Map<String, Object> map = HttpUtil.splitUriToMap(uri);
            return BeanUtil.MapCastToBean(map,clazz);
        }
        // 处理post请求的参数
        if (SystemConstant.HttpMethod.POST.name().equals(type))
        {

            Class<?>[] parameterTypes = method.getParameterTypes();
            SystemAssert.isTrue(parameterTypes.length>0, SystemStatusEnum.MISSING_FORMAL_PARAMETER_IN_METHOD);
            // 判断参数中是否加入@RequestBody注解，如果有该注解的话从body中取数据
            Class<?> aClass = checkParamWithBody(method);
            if (aClass != null)
            {
                // 获取body中的json串
                String bodyJsonString = getBodyJsonString(request);
                // 将json串转换成对象返回
                Object o = JSONObject.parseObject(bodyJsonString, aClass);
                return (T)o;
            }else {
             // 否则从uri上获取数据转成对象
                Class<?> parameterType = parameterTypes[0];
                Map<String, Object> map = HttpUtil.splitUriToMap(request.uri());
                Object o = BeanUtil.MapCastToBean(map, parameterType);
                return (T)o;
            }

        }
        throw PtException.error(SystemStatusEnum.UNKNOWN_REQUEST_TYPE);
    }
    @Override
    public Map<String, Object> getParamToMap(FullHttpRequest request)
    {
        String type = getMethodType(request);
        // 处理get请求的参数
        if (SystemConstant.HttpMethod.GET.name().equals(type))
        {
            // 获取uri上数据转成map
            return HttpUtil.splitUriToMap(request.uri());
        }
        // 处理post请求的参数
        if (SystemConstant.HttpMethod.POST.name().equals(type))
        {
            String bodyJsonString = getBodyJsonString(request);
            return JSONObject.parseObject(bodyJsonString, Map.class);
        }
        throw PtException.error(SystemStatusEnum.UNKNOWN_REQUEST_TYPE);
    }

    private static final class DefaultParameterAnalysisHodler{
        private static ParameterAnalysis parameterAnalysis = new DefaultParameterAnalysis();
    }
    /**
     * 获取请求方法的类型
     * @param request
     */
    private String getMethodType(FullHttpRequest request){
        HttpMethod method = request.method();
        return method.name();
    }
    /**
     * 判断方法中形参是否含有@RequestBody
     * @param method
     * @return
     */
    private Class<?> checkParamWithBody(Method method){
        Class<?> clazz = null;
        // 获取方法中所有的形式参数
        Class<?>[] parameterTypes = method.getParameterTypes();
        // 获取方法形参上的所有的注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        // 增加断点表达式，直接break跳到该位置
        a:{
            for (int i = 0; i < parameterTypes.length; i++)
            {
                for (Annotation annotation : parameterAnnotations[i])
                {
                    if (annotation instanceof RequestBody)
                    {
                        clazz = parameterTypes[i];
                        break a;
                    }
                }
            }
        }
        return clazz;
    }
    /**
     * 获取body中对应的json串
     * @param request
     * @return
     */
    private String getBodyJsonString(FullHttpRequest request){
        ByteBuf content = request.content();
        return content.toString(CharsetUtil.UTF_8);
    }



}
