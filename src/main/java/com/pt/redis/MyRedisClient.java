package com.pt.redis;
import com.pt.annotation.Value;
import com.pt.util.YmlUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.CountDownLatch;
/**
 * 基于netty实现redis客户端
 */
public class MyRedisClient
{
    // redis 服务器主机
    private String host;
    // redis 服务器的端口
    private int port;
    // 增加线程阻塞获取值
    private CountDownLatch lathc;
    // 管道初始化
    private MyHandlerInitialization myHandlerInitialization;
    // 线程组
    private EventLoopGroup group;
    // 程序启动器
    private Bootstrap bootstrap;
    // 异步通道
    private ChannelFuture cf;



    private static class SingletonHolder {
        static final MyRedisClient instance = new MyRedisClient();
    }

    public static MyRedisClient getInstance(){
        return SingletonHolder.instance;
    }

    private MyRedisClient()
    {
        this.host = YmlUtil.getString("redis.host");
        this.port = YmlUtil.getInt("redis.port");
        lathc = new CountDownLatch(0);
        myHandlerInitialization = new MyHandlerInitialization(lathc);
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(myHandlerInitialization);

    }

    /**
     * 创建redis连接
     */
    public void conn(){
        try
        {
            this.cf = bootstrap.connect(host,port).sync();
            System.out.println("【服务器连接成功】》》》》》》》》》");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
        }
    }

    /**
     * 获取cf对象
     * @return
     */
    public ChannelFuture getChannelFuture(){
        if (this.cf == null)
        {
            this.conn();
        }
        if (!this.cf.channel().isActive())
        {
            this.conn();
        }
        return this.cf;
    }

    /**
     * redis客户端发送数据
     * @param message
     * @return
     * @throws InterruptedException
     */
    public String sendMessage(String message) throws InterruptedException
    {
        ChannelFuture channelFuture = getInstance().getChannelFuture();
        byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        channelFuture.channel().writeAndFlush(byteBuf);

        lathc = new CountDownLatch(1);
        myHandlerInitialization.resetLathc(lathc);
        lathc.await();
        return myHandlerInitialization.getResult();
    }
    public static void main(String[] args) throws InterruptedException
    {
        MyRedisClient localhost = MyRedisClient.getInstance();
        String str = localhost.sendMessage("HGETALL user:1");
        System.out.println(str);
    }


}
