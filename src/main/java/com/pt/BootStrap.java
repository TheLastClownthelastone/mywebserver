package com.pt;
import com.pt.server.NettyHttpServer;
import com.pt.server.Server;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
/**
 * 启动器
 */

public class BootStrap
{
    public static void main(String[] args) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException
    {
        Server server = new NettyHttpServer(BootStrap.class);
        server.preStart();
        server.start();
    }
}
