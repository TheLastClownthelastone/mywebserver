package com.pt.server.handler;
import com.pt.executor.Executor;
import com.pt.executor.HttpResponseExecutor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
/**
 * 控制路由分发处理器
 */
@Slf4j
public class ControllerDispatcher extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private Executor<HttpResponse> executor = HttpResponseExecutor.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception
    {
        // 异步执行
        
        // 同步执行
        HttpResponse execute = executor.execute(request);
        ctx.writeAndFlush(execute).addListener(ChannelFutureListener.CLOSE);
    }
}
