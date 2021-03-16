package com.pt.context;
import cn.hutool.core.convert.Convert;
import com.pt.annotation.Aspect;
import com.pt.annotation.AutoWire;
import com.pt.annotation.Component;
import com.pt.annotation.Controller;
import com.pt.annotation.Mapper;
import com.pt.annotation.PTJob;
import com.pt.annotation.RequestMapping;
import com.pt.annotation.Service;
import com.pt.annotation.Value;
import com.pt.aspct.Advice;
import com.pt.constant.SystemConstant;
import com.pt.exception.SystemStatusEnum;
import com.pt.handler.Handler;
import com.pt.handler.TimerTaskHandler;
import com.pt.redis.RedisTemplate;
import com.pt.router.RouterInfo;
import com.pt.sql.SqlAnnoHandler;
import com.pt.task.Task;
import com.pt.task.TaskInfo;
import com.pt.util.CharUtil;
import com.pt.util.SystemAssert;
import com.pt.util.YmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * bean的上下文对象
 */
@Slf4j
public class DefaultBeanContext implements BeanContext
{
    // 储存全局中含有自定义注解的所有的类
    private static Map<String,Object> beanMap = new HashMap<>();

    // 储存全局的路由
    private static Map<String, RouterInfo> routerMap = new HashMap<>();


    // 全局定时任务
    private static List<TaskInfo> taskList = new ArrayList<>();

    // 是否执行完init方法的判断标识
    private static boolean isInit;

    // 是否开启aop
    private static  boolean aspect;

    // 获取定时处理器
    private static Handler handler = TimerTaskHandler.getInstance(taskList);


    // 全局线程池
    private static ThreadPoolExecutor executor = AsynchronousHodler.executor;

    private DefaultBeanContext(){}


    private static SqlAnnoHandler sqlAnnoHandler = SqlAnnoHandler.getInstance();


    /**
     * 执行初始化方法将所有的bean实例装到一个Map中
     * @param clazzs
     *
     * 注： 改方法只能让执行一次，不然会污染全局Map中的数据
     * 执行改方法之后将class模板锁住不让多线程下出现同时修改出现的问题
     * 增加全局
     */
    public static void doInt(Set<Class<?>> clazzs,boolean isOpenRedis) throws InstantiationException, IllegalAccessException, IntrospectionException, InvocationTargetException
    {
        synchronized (DefaultBeanContext.class){
            if (!isInit)
            {
                for (Class<?> clazz : clazzs)
                {
                    // 1.将对象赋值到map中
                    initBean(clazz,isOpenRedis);
                }
                for (Map.Entry<String, Object> entry : beanMap.entrySet())
                {
                    Object value = entry.getValue();
                    if (value != null)
                    {
                        // 2.依赖注入
                        injectAnnotation(value);

                        // 3.路由键记录
                        initRouterKey(value);

                        // 4. 加载配置文件中的值
                        initValue(value);

                        // 5.是否开启aspect
                        checkAspect(value);

                        // 6.扫描所有的定时任务
                        scanTimingTask(value);

                        if (taskList.size() > SystemConstant.IsInt.NO.getCode())
                        {
                            // 存在定时任务的时候执行定时任务
                            handler.handler(executor);
                        }
                    }
                }

                // 执行完之后设置标识
                isInit = true;
            }
        }
    }

    /**
     * 进行依赖注入可以通过 set方法注入方式 可以通过成员变量注入的方式
     */
    private static void injectAnnotation(Object bean) throws IntrospectionException, InvocationTargetException, IllegalAccessException
    {
        log.info("【开始执行依赖注入】》》》》》》》》》》》》》");
        // set方法注入
        propertyAnnotation(bean);
        // 属性注入
        fieldAnnotation(bean);
    }
    /**
     * 获取bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> T getBean(String name, Class<T> clazz)
    {
        Object o = beanMap.get(name);
        return o == null ? null :(T)o;
    }
    /**
     * 获取bean
     * @param name
     * @return
     */
    @Override
    public Object getBean(String name)
    {
        return beanMap.get(name);
    }
    /**
     * 获取路由对象
     * @param key
     * @return
     */
    @Override
    public RouterInfo getRouterInfo(String key){
        return routerMap.get(key);
    }

    /**
     * 声明私有静态类部类来获取改对象的实例
     */
    private static final class DefaultBeanContextHolder {
        private static  DefaultBeanContext context = new DefaultBeanContext();
    }
    /**
     * 获取上下文实例
     * @return
     */
    public static BeanContext getInstance(){
        return DefaultBeanContextHolder.context;
    }
    /**
     * 初始化bean
     * @param clazz
     */
    private static void initBean(Class<?> clazz,boolean isOpenRedis) throws IllegalAccessException, InstantiationException
    {
        log.info("【初始化bean】。。。。。。。。。。");
        List<Annotation> annotations = Arrays.asList(clazz.getAnnotations());
        // 判断是否开启redis服务
        if (isOpenRedis)
        {
            RedisTemplate instance = RedisTemplate.getInstance();
            beanMap.put("redisTemplate",instance);
        }

        for (Annotation annotation : annotations)
        {
            String name = null;
            if (annotation instanceof Controller)
            {
                Controller controller = ((Controller) annotation);
                name = controller.value();
            }
           if (annotation instanceof Service)
            {
                Service service = ((Service) annotation);
                name = service.value();
            }
           if(annotation instanceof Component){
                Component component = ((Component) annotation);
                name = component.value();
            }
            // 当注解为@Mapper 注解的时候走cglib给接口进行实例
            if (annotation instanceof Mapper)
            {
                String simpleName = clazz.getSimpleName();
                String s = CharUtil.toLowerCaseFirstOne(simpleName);
                Object o1 = sqlAnnoHandler.executeSqlToBean(clazz);
                beanMap.put(s,o1);
            }else {
                Object o = clazz.newInstance();
                if (StringUtils.isNotEmpty(name))
                {
                    beanMap.put(name,o);
                }else {
                    // 进行类名首字母小写放入map中
                    String simpleName = clazz.getSimpleName();
                    String s = CharUtil.toLowerCaseFirstOne(simpleName);
                    beanMap.put(s,o);
                }
            }
        }
    }
    /**
     * set方法进行注入
     */
    private static void propertyAnnotation(Object bean) throws IntrospectionException, InvocationTargetException, IllegalAccessException
    {
        // 获取类中属性和方法信息信息
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
            // 获取对应的setter方法
            Method setter = propertyDescriptor.getWriteMethod();
            // 当该方法是setter方法并且上面写上了@AutoWire注解的时候进行依赖注入
            if (setter != null && setter.isAnnotationPresent(AutoWire.class))
            {
                // 获取AutoWire中的值
                AutoWire autoWire = setter.getAnnotation(AutoWire.class);
                String name;
                Object value = null;
                // 当@AutoWire中是有填写值的话
                if (StringUtils.isNotBlank(autoWire.value()))
                {
                    name = autoWire.value();
                    value = beanMap.get(name);
                }else {
                    // 没有填写值的进行类型匹配
                    for (Map.Entry<String, Object> entry : beanMap.entrySet())
                    {
                        // 进行类型匹配
                        if (propertyDescriptor.getPropertyType().isAssignableFrom(entry.getValue().getClass()))
                        {
                            value = entry.getValue();
                        }
                    }
                }
                // 修饰该setter方法为private可以进行访问
                setter.setAccessible(true);

                // 执行改方法进行赋值
                setter.invoke(bean,value);
            }
        }
    }
    /**
     * 成员变量属性赋值
     * @param bean
     */
    private static void fieldAnnotation(Object bean) throws IllegalAccessException
    {
        // 获取所有的属性，不能使用getFiled 那样只能获取到public修饰了属性
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            // 对属性上含有@AutoWire的属性进行依赖注入
            if (field != null && field.isAnnotationPresent(AutoWire.class))
            {
                AutoWire autoWire = field.getAnnotation(AutoWire.class);
                String name;
                Object value = null;
                // 当@AutoWire中有值的话根据上面配置找到对象
                if (StringUtils.isNotBlank(autoWire.value()))
                {
                    name = autoWire.value();
                    value = beanMap.get(name);
                }else {
                    // 否则根据类型进行匹配
                    for (Map.Entry<String, Object> entry : beanMap.entrySet())
                    {
                        if (field.getType().isAssignableFrom(entry.getValue().getClass()))
                        {
                            value = entry.getValue();
                            break;
                        }
                    }
                }

                // 设置改属性为private的也可以进行访问
                field.setAccessible(true);

                // 给属性赋值
                field.set(bean,value);
            }
        }
    }
    /**
     * 初始化路由键
     */
    private static void initRouterKey(Object value)
    {
        configRouter(value);
    }
    /**
     * 将方法和类 跟路由进行配置
     * @param value
     */
    private static void configRouter(Object value){
        // 获取对应类上的RequestMapping注解
        RequestMapping prefixRouter = value.getClass().getAnnotation(RequestMapping.class);
        String s = null;
        if (prefixRouter != null)
        {
            s = prefixRouter.value();
        }
        Method[] declaredMethods = value.getClass().getDeclaredMethods();
        //将方法上没有标记RequestMapping注解的方法
        final String prefixName = s;
        Arrays.asList(declaredMethods).stream().filter(method -> method.isAnnotationPresent(RequestMapping.class)).forEach(method ->
        {
            String key = null;
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            if (StringUtils.isNotBlank(prefixName))
            {
                key =CharUtil.removeSlash(prefixName) +CharUtil.removeSlash(annotation.value());
            }else {
                key = CharUtil.removeSlash(annotation.value());
            }
            SystemAssert.isTrue(routerMap.get(key) == null, SystemStatusEnum.SERVER_ROUTER_KEY_REPEAT);
            RouterInfo info  = new RouterInfo();
            info.setClazz(value.getClass());
            info.setInfo(value);
            info.setMethod(method);
            info.setRequiar(annotation.method());
            routerMap.put(key,info);
        });


    }
    /**
     * 将配置文件中的内容注入到含有@Value对应的注解的属性中
     */
    private static void initValue(Object value) throws IllegalAccessException
    {
        propertyInjection(value);

    }
    /**
     * 进行属性注入
     * @param value
     */
    private static void propertyInjection(Object value) throws IllegalAccessException
    {
        Class<?> aClass = value.getClass();
        // 获取所有的属性包括私有的
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            if (declaredField.isAnnotationPresent(Value.class))
            {
                Value annotation = declaredField.getAnnotation(Value.class);
                String key = annotation.value();

                Object obj = YmlUtil.getValue(key);

                // 设置所有属性也可以进行操作
                declaredField.setAccessible(true);

                // 进行属性强转
                Class<?> type = declaredField.getType();
                Object convert = Convert.convert(type, obj);
                // 赋值
                declaredField.set(value,convert);
            }
        }

    }
    /**
     * 检查是否开启aop
     */
    private static void checkAspect(Object value)
    {
        Class<?> aClass = value.getClass();
        boolean annotationPresent = aClass.isAnnotationPresent(Aspect.class);
        /**
         * 判断一个是不是某个接口下面的实现
         * 通过接口.class.isAssignableFrom(具体类的类型)
         */
        boolean assignableFrom = Advice.class.isAssignableFrom(aClass);
        if (assignableFrom && annotationPresent)
        {
            aspect = true;
        }
    }
    /**
     *是否aop
     * @return
     */
    @Override
    public boolean isAspect(){
        return aspect;
    }
    @Override
    public <T> List<T> getBeanByType(Class<T> clazz)
    {
        List<T> list = new ArrayList();
        for (Map.Entry<String, Object> entry : beanMap.entrySet())
        {
            Object value = entry.getValue();
            if (clazz.isAssignableFrom(value.getClass()))
            {
                list.add((T) value);
            }
        }
        return list;
    }
    @Override
    public ThreadPoolExecutor getExecutor()
    {
        return executor;
    }
    @Override
    public List<TaskInfo> getTaskInfo()
    {
        return taskList;
    }
    /**
     * 扫描定时任务
     */
    private static void scanTimingTask(Object value){
        configTimingTask(value);
    }
    /**
     * 将所有的定时任务进行初始化操作
     */
    private static void configTimingTask(Object obj){
        Class<?> aClass = obj.getClass();
        boolean assignableFrom = Task.class.isAssignableFrom(aClass);
        boolean annotationPresent = aClass.isAnnotationPresent(PTJob.class);
        // 将实现了Task接口并且加上@PTJob注解的
        if ( assignableFrom && annotationPresent )
        {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTask(((Task) obj));
            taskInfo.setPtJob(aClass.getAnnotation(PTJob.class));
            taskList.add(taskInfo);
        }

    }

    private static final class AsynchronousHodler{
        private static ThreadPoolExecutor executor = new ThreadPoolExecutor(3,Runtime.getRuntime().availableProcessors(),50, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
    }


}
