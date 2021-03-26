package com.pt.mqtt;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
/**
 * mqtt服务器通道初始化器
 */
public class MqttInitializer extends ChannelInitializer<Channel>
{
    @Override
    protected void initChannel(Channel channel) throws Exception
    {
        ChannelPipeline pipeline = channel.pipeline();
        // mqtt协议解码
        pipeline.addLast(new MqttDecoder());
        // mqtt协议编码
        pipeline.addLast(MqttEncoder.INSTANCE);
        // 增加业务处理
        pipeline.addLast(new MqttHandler());
    }
}
