package com.pt.callback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallbackTest {
    private static HandlerMsg handlerMsg;

    public static void main(String[] args) {
        handlerMsg = new HandlerMsg();
        handlerMsg.setCallBack(new IHandlerCallBack() {
            @Override
            public void execute(Map<String, String> msg) {
                System.out.println("执行回调操作");
                // 回调操作代码
                System.out.println(msg.get("msg"));
            }
        });
        // 限定数量的线程
        ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                public void run() {
                    try {
                        Map<String, String> msg = new HashMap<>();
                        msg.put("msg", index + "线程");
                        handlerMsg.sendMsg(msg);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        singleThreadExecutor.shutdown();
    }
}
