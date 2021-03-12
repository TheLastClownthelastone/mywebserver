package com.pt.util;
import cn.hutool.core.convert.Convert;
import com.pt.constant.SystemConstant;
import com.pt.exception.SystemStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
/**
 * 用来解析yml的工具类
 */
@Slf4j
public class YmlUtil
{

    private static Map<String,Object> ymlMap;
    // 加载配置
    static {
        InputStream resourceAsStream = null;
        try
        {
            Yaml yaml = new Yaml();
            resourceAsStream = YmlUtil.class.getClassLoader().getResourceAsStream(SystemConstant.DEFAULT_CONFIG_PATH);
            ymlMap = yaml.loadAs(resourceAsStream,Map.class);
        } catch (Exception e)
        {
           log.error("【加载配置异常】：{}",e);
        } finally
        {
            IOUtils.closeQuietly(resourceAsStream);
        }
    }

    private YmlUtil(){}
    /**
     * 通过递归一层剥开嵌套map获取里面的key
     * @param key
     * @return
     */
    public static Object getValue(String key){
        SystemAssert.isTrue(StringUtils.isNotBlank(key), SystemStatusEnum.YML_KEY_CAN_NOT_NULL);
        if (key.contains("."))
        {
            // 递归取值
            String[] split = key.split("\\.");
            return recursionGetValue(split,ymlMap,0);
        }else{
            return ymlMap.get(key);
        }
    }
    /**
     * 递归取值
     * @param split
     * @return
     */
    private static Object recursionGetValue(String[] split,Map<String,Object> map,int i){
        String key = split[i];
        i++;
        Object o = map.get(key);
        if (o instanceof Map)
        {
            Map<String, Object> nextMap = (Map<String, Object>) o;
            return recursionGetValue(split,nextMap,i);
        }else{
            return o;
        }
    }


    /**
     * 获取yml配置中属性
     * @param key
     * @return
     */
    public static int getInt(String key){
        Object value = getValue(key);
        // 使用hutool工具类类型转换
        return Convert.toInt(value);
    }
    /**
     * 获取yml中对应键的String值
     * @param key
     * @return
     */
    public static String getString(String key){
        Object value = getValue(key);
        return Convert.toStr(value);
    }
    /**
     * 获取yml中对应键的boolean值
     * @param key
     * @return
     */
    public static boolean getBoolean(String key){
        Object value = getValue(key);
        return Convert.toBool(value);
    }


}
