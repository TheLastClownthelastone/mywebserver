package com.pt.mqtt;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttVersion;
/**
 * mqtt协议通道处理器
 */
public class MqttHandler extends ChannelDuplexHandler
{
    /**
     * 连接创建成功的时候进行用户名 密码的验证
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
//        MqttVersion verinfo = MqttVersion.MQTT_3_1_1;
//        //super.channelActive(ctx);
//        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(verinfo.protocolName(),
//                verinfo.protocolLevel(), true, true, false,
//                0, false, false, 60);
//        MqttConnectPayload mqttConnectPayload = new MqttConnectPayload("","",
//                new byte[1048], "", info.getPassword());
//        MqttConnectMessage mqttSubscribeMessage = new MqttConnectMessage(mqttFixedHeader, mqttConnectVariableHeader,
//                mqttConnectPayload);
//        ctx.writeAndFlush();
    }
    // 发送数据的方法
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
    {
        super.write(ctx, msg, promise);
    }
    // 读取数据的方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        super.channelRead(ctx, msg);
    }
}
