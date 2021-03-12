package com.pt.constant;
import io.netty.handler.codec.http.HttpMethod;
/**
 * 系统常量
 */
public class SystemConstant
{

    public static final String DEFAULT_CONFIG_PATH = "application.yml";


    public static enum Is{
        NO("0","否"),YES("1","是");
        Is(String code, String value)
        {
            this.code = code;
            this.value = value;
        }
        String code;
        String value;
        public String getCode()
        {
            return code;
        }
        public void setCode(String code)
        {
            this.code = code;
        }
        public String getValue()
        {
            return value;
        }
        public void setValue(String value)
        {
            this.value = value;
        }
    }

    public static enum  IsInt{
        NO(0,"否"),YES(1,"是");
        int code;
        String value;
        IsInt(int code, String value)
        {
            this.code = code;
            this.value = value;
        }
        public int getCode()
        {
            return code;
        }
        public void setCode(int code)
        {
            this.code = code;
        }
        public String getValue()
        {
            return value;
        }
        public void setValue(String value)
        {
            this.value = value;
        }
    }


    public static enum  HttpMethod
    {
        GET("GET"),POST("POST");
        String value;
         HttpMethod(String value)
        {
            this.value = value;
        }
    }

}
