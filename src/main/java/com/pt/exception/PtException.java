package com.pt.exception;
public class PtException extends RuntimeException
{
    private PtException()
    {
    }

    private PtException(SystemStatusEnum statusEnum)
    {
        super("【errorCode】：" + statusEnum.code + "," + "【errorMessage】：" + statusEnum.msg);
    }

    private PtException(String errorMsg)
    {
        super("【errorMessage】："+ errorMsg);
    }


    public  static PtException error(SystemStatusEnum statusEnum)
    {
        return  new PtException(statusEnum);
    }

    public static PtException error(String errorMsg){
        return new PtException(errorMsg);
    }
}
