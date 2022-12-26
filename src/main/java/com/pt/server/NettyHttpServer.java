package com.pt.server;
import com.pt.annotation.PtServer;
import com.pt.config.SystemConfig;
import com.pt.init.InitExecutor;
import com.pt.server.handler.NettyHttpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
/**
 * netty服务端
 */
@Slf4j
@PtServer
public class NettyHttpServer implements Server
{
    private SystemConfig systemConfig = SystemConfig.getInstance();

    private Class<?> clazz;

    public  NettyHttpServer (Class<?> clazz){
        this.clazz = clazz;
    }
    /**
     * 通过netty启动项目
     */
    @Override
    public void start()
    {
        // 创建两个线程组一个处理连接事件 一个用处理具体业务
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_BACKLOG,systemConfig.getOption())
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new NettyHttpServerInitializer());
            ChannelFuture sync = serverBootstrap.bind(systemConfig.getPort()).sync();
            log.info("【服务器绑定端口】：----------------{}-------------------",systemConfig.getPort());
            sync.addListener((GenericFutureListener) future ->
            {
                if (future.isSuccess())
                {
                    log.info("服务器启动完毕》》》》》》》》》》》》》》》》》》》》》》");
                }
            });
            // 开启异步关闭通道
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e)
        {
            log.error("【服务器启动失败】:{}",e);
        } finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    /**
     * 项目启动之前的工作
     */
    public void preStart() throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        // 加载类信息
        InitExecutor.init(this.clazz);
    }
}
