package com.pt.redis;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.redis.ArrayRedisMessage;
import io.netty.handler.codec.redis.FullBulkStringRedisMessage;
import io.netty.handler.codec.redis.RedisMessage;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
/**
 * netty 自定义客户端集成 ChannelDuplexHandler 该处理
 * netty 自定义服务器集成 SimpleChannelInboundHandler
 */
public class MyRedisMessageHandler extends ChannelDuplexHandler
{

    private CountDownLatch latch;

    private String result;

    public MyRedisMessageHandler(CountDownLatch latch)
    {
        this.latch = latch;
    }
    /**
     * 向服务器发送消息
     * @param ctx
     * @param msg
     * @param promise
     * @throws Exception
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
    {
        // 发送消息的遵循redis 编码解码
        ByteBuf byteBuf = (ByteBuf) msg;
        String str = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("在监听写的事件的时候获取的参数为："+str);
        // 以为redis协议使用的RESP协议 每一个字符都会带着一个\r\n ，所以将当前的字符进行空白字符截取之后往每一个字符后面添加\r\n 满足redis的协议
        String[] split = str.split("\\s+");
        List<RedisMessage> list = new ArrayList<>();
        for (String s : split)
        {
            System.out.println(s);
            list.add(new FullBulkStringRedisMessage(ByteBufUtil.writeUtf8(ctx.alloc(), s)));
        }
        RedisMessage request = new ArrayRedisMessage(list);
        ctx.write(request, promise);
    }
    /**
     * 读取服务器发送过来消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        System.out.println("执行redis服务器返回数据的读取");
        //处理 get set
        if (msg instanceof FullBulkStringRedisMessage)
        {
            FullBulkStringRedisMessage info = (FullBulkStringRedisMessage) msg;
            ByteBuf content = info.content();
            this.result = content.toString(CharsetUtil.UTF_8);
        }
        // 处理Hget Hset
        if (msg instanceof ArrayRedisMessage)
        {
            StringBuilder sb = new StringBuilder();
            ArrayRedisMessage info = (ArrayRedisMessage) msg;
            List<RedisMessage> children = info.children();
            for (RedisMessage child : children)
            {
                FullBulkStringRedisMessage message = (FullBulkStringRedisMessage) child;
                sb.append(message.content().toString(CharsetUtil.UTF_8));
                sb.append("@#");
            }
            this.result = sb.toString();
        }

        latch.countDown();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("执行异常了 :"+cause.getMessage());
    }

    public void resetLatch(CountDownLatch latch){
        this.latch = latch;
    }

    public String getResult(){
        return this.result;
    }

}
