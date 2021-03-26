package com.pt.callback.demo;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 执行的方法
 */
public class DoSomethings {

    private  CallBack callBack;

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
        doEveryThing();
    }

    public void doEveryThing(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM- dd HH:mm:ss");
                    String format = sdf.format(new Date());
                    callBack.doExcute(format);
                }
            }
        }).start();
    }


   @Test
    public void test1(){
        DoSomethings doSomethings = new DoSomethings();

        doSomethings.setCallBack(new CallBack() {
            @Override
            public void doExcute(String time) {
                System.out.println(time);
            }
        });

       while (true) {

       }
   }


}
