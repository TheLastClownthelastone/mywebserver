package com.pt.util;
import com.pt.exception.SystemStatusEnum;
import lombok.Data;
/**
 * 影响返回对象
 * @param <T>
 */
@Data
public class CommonResult<T>
{

    private String code;

    private T data;

    private String msg;

    private CommonResult(){}


    private CommonResult(T data, SystemStatusEnum statusEnum){
        this.data = data;
        this.code = statusEnum.getCode();
        this.msg = statusEnum.getMsg();
    }

    private CommonResult(Exception e){
        this.data = null;
        this.code = SystemStatusEnum.ERROR.getCode();
        this.msg = e.getMessage();
    }


    public static CommonResult success(Object data){
        return new CommonResult(data,SystemStatusEnum.SUCCESS);
    }

    public static CommonResult success(){
        return new CommonResult(null,SystemStatusEnum.SUCCESS);
    }

    public static CommonResult error(Exception e){
        return new CommonResult(e);
    }



}
