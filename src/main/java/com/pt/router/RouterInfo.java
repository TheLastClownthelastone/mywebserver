package com.pt.router;
import com.pt.constant.SystemConstant;
import lombok.Data;

import java.lang.reflect.Method;
/**
 * 路由键实例
 */
@Data
public class RouterInfo
{
    /**
     * 指定执行方法的class模板对象
     */
    private Class<?> clazz;

    /**
     * 需要被执行的方法
     */
    private Method method;

    /**
     * 对象实例
     */
    private Object info;
    /**
     * 请求形式
     */
    private SystemConstant.HttpMethod requiar;
}
