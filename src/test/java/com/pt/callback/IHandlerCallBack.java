package com.pt.callback;

import java.util.Map;

public interface IHandlerCallBack {
    // 执行回调操作的方法
    void execute(Map<String, String> msg);
}
