package com.pt.redis;
import com.pt.exception.PtException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * redis模板对象
 */
public class RedisTemplate
{
    private String BLANK = " ";

    private String GET = "get";

    private String SET = "set";

    private String HSET = "hset";

    private String HGET = "hget";

    private String HGETALL = "HGETALL";


    private MyRedisClient client;

    private RedisTemplate(){
        client = MyRedisClient.getInstance();
        client.conn();
    }

    public static RedisTemplate getInstance(){
        return new RedisTemplate();
    }
    /**
     * 获取key
     * @param key
     * @return
     */
    public String get(String key){
        try
        {
            return client.sendMessage(GET+BLANK+key);
        } catch (InterruptedException e)
        {
            throw PtException.error(e.getMessage());
        }
    }
    /**
     * 设置key
     * @param key
     */
    public void set(String key,Object value){
        try
        {
            client.sendMessage(SET+BLANK+key+BLANK+value);
        } catch (InterruptedException e)
        {
            throw PtException.error(e.getMessage());
        }
    }
    /**
     * hset
     * @return
     */
    public void hset(String prefix,String key,String value){
        try
        {
            client.sendMessage(HSET+BLANK+prefix+BLANK+key+BLANK+value);
        } catch (InterruptedException e)
        {
            throw PtException.error(e.getMessage());
        }
    }
    /**
     * hget
     * @param prefix
     * @param key
     * @return
     */
    public String hget(String prefix,String key){
        try
        {
            return client.sendMessage(HGET + BLANK + prefix + BLANK + key);
        } catch (InterruptedException e)
        {
            throw PtException.error(e.getMessage());
        }
    }
    /**
     * hgetAll
     * @param prefix
     * @return
     */
    public List<Map<String,String>> hgetAll(String prefix){
        try
        {
            List<Map<String,String>> list = new ArrayList<>();
            String s = client.sendMessage(HGETALL + BLANK + prefix);
            String[] split = s.split("@#");
            for (int i = 0; i < split.length; i++)
            {
                if (i % 2 == 0)
                {
                    Map<String,String> map = new HashMap<>();
                    map.put(split[i],split[i+1]);
                    list.add(map);
                }
            }
            return list;
        } catch (InterruptedException e)
        {
            throw PtException.error(e.getMessage());
        }
    }

}
