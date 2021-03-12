package com.pt.context;
import com.pt.router.RouterInfo;
import com.pt.task.TaskInfo;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
/**
 * 上下文对象的接口，规定上下文对象中获取到内容
 */
public interface BeanContext
{
    /**
     * 通过名称和类型获取对应bean的实例
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getBean(String name,Class<T> clazz);

    /**
     * 通过bean的名称获取bean的实例
     * @param name
     * @return
     */
    Object getBean(String name);

    /**
     * 获取路由对象
     * @param key
     * @return
     */
    RouterInfo getRouterInfo(String key);

    /**
     *是否aop
     * @return
     */
    boolean isAspect();
    /**
     * 根据类型查询容器中的对象集合
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> getBeanByType(Class<T> clazz);

    /**
     * 获取全局的线程池
     * @return
     */
    ThreadPoolExecutor getExecutor();

    /**
     * 获取系统定时任务
     */
    List<TaskInfo> getTaskInfo();
}
