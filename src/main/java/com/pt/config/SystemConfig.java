package com.pt.config;
import com.pt.util.YmlUtil;
/**
 * 系统配置类
 */
public class SystemConfig
{

    private SystemConfig (){}


    private static int defaultInt(Integer first, Integer second){
        if (first == null)
        {
            return second;
        }else{
            return first;
        }
    }

    public static SystemConfig getInstance(){
        return SystemConfigHodler.systemConfig;
    }


    private static final class SystemConfigHodler{
        private static SystemConfig systemConfig = new SystemConfig();
    }

    public int getPort(){
        return defaultInt(YmlUtil.getInt("server.port"),8080);
    }

    public int getOption(){
        return defaultInt(YmlUtil.getInt("netty.option"),1024);
    }

}
