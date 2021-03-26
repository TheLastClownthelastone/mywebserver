package com.pt.download;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;

/**
 * 进度条
 */
public class ProgressBar implements Runnable{
    // 文件大小
    private long fileSize;

    // 当前下载的文件大小
    private double curren;

    private boolean isRun = true;

    public Object lock = new Object();

    // 增加下载的数量
    public void addSize(int size){
        this.curren = this.curren+size;
    }

    public void finsh(){
        this.isRun = false;
    }

    public ProgressBar(long fileSize){
        this.fileSize = fileSize;
    }


    @Override
    public void run() {
        while (isRun){
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("当前下载了："+curren +"kb");
                    System.out.println("总文件大小："+fileSize+"kb");
                    System.out.println("当前进度："+(curren/fileSize)*100 +"%");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        System.out.println("下载完成");
    }

    public static void main(String[] args){
        File file = new File("D:\\Project_Pro_2013_64Bit.ISO");
        ProgressBar progressBar = new ProgressBar(file.length());
        new Thread(progressBar).start();
        InputStream inputStream = null;
        FileOutputStream os = null;
        try {
            inputStream  = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            os = new FileOutputStream("D:\\testexcel\\1.ISO");
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1){
                os.write(bytes,0,len);
                progressBar.addSize(len);
                synchronized (progressBar.lock){
                    progressBar.lock.notify();
                }
            }
            progressBar.finsh();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(os);
        }

    }


}
