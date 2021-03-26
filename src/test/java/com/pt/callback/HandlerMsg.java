package com.pt.callback;

import java.util.Map;

public class HandlerMsg {
    private IHandlerCallBack callBack;

    //回调方法设置
    public void setCallBack(IHandlerCallBack callBack) {
        this.callBack = callBack;
    }

    //执行
    public void sendMsg(Map<String, String> msg) {
        callBack.execute(msg);
    }


}
