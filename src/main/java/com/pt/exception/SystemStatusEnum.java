package com.pt.exception;
public enum  SystemStatusEnum
{
    // 连接相关
    SUCCESS("200","成功"),
    ERROR("500","失败"),


    // 服务器初始化相关
    SERVER_NUM_MORE_ONE("1001","PtServer 只能出现一次"),
    SERVER_ROUTER_KEY_REPEAT("1002","路由键重复了！请仔细检查"),


    // yml解析相关
    YML_KEY_CAN_NOT_NULL("2001","key不能为空"),



    // 解析参数相关
    URI_MUST_HAVE_PARAMETERS("3001","URI中无参数解析符号"),
    URI_FORMAT_ERROR("3002","URI格式错误"),
    MISSING_FORMAL_PARAMETER_IN_METHOD("3003","方法中缺少形参"),
    UNKNOWN_REQUEST_TYPE("3004","未知请求类型"),
    HTTP_REQUEST_MODE_ERROR("3005","HTTP 请求方式错误"),


    // 定时任务相关
    UNTIMELY_TASK("4001","无定时任务")
    ,

    ;
    SystemStatusEnum(String code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }
    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public String getMsg()
    {
        return msg;
    }
    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    String code;
    String msg;
}