package com.pt.server.handler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
/**
 * 业务处理器
 */
@Slf4j
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel>
{
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // http 服务器编码解码
        pipeline.addLast(new HttpServerCodec());
        // http 协议块状发送数据
        pipeline.addLast(new ChunkedWriteHandler());
        // 如果要使用FullHttpRequest 作为传输的话那么，在pipeline中就要申明 消息聚合区
        pipeline.addLast(new HttpObjectAggregator(512*1024)); // http 消息聚合器
        // 往管道中添加处理器
        // 路由分发器
        pipeline.addLast(new ControllerDispatcher());

    }
}
