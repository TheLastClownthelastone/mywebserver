package com.pt.redis;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;

import java.util.concurrent.CountDownLatch;
/**
 * 管道初始化
 * 泛型加入管道类型
 */
public class MyHandlerInitialization extends ChannelInitializer<Channel>
{

    private CountDownLatch latch;

    private MyRedisMessageHandler handler;

    public MyHandlerInitialization(CountDownLatch latch)
    {
        this.latch = latch;
    }
    @Override
    protected void initChannel(Channel channel) throws Exception
    {
        // 通过管道获取通道
        ChannelPipeline pipeline = channel.pipeline();
        // redis协议解码
        pipeline.addLast(new RedisDecoder());
        // redisString字符处理
        pipeline.addLast(new RedisBulkStringAggregator());
        // redis 数组类型处理
        pipeline.addLast(new RedisArrayAggregator());
        // redis 协议编码
        pipeline.addLast(new RedisEncoder());
        // redis 自定义业务处理器
        this.handler = new MyRedisMessageHandler(this.latch);
        pipeline.addLast(this.handler);
    }

    public void resetLathc(CountDownLatch latch){
        handler.resetLatch(latch);
    }

    public String getResult(){
        return handler.getResult();
    }
}
