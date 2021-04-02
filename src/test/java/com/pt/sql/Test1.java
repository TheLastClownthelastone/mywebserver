package com.pt.sql;
import net.sf.cglib.proxy.Enhancer;
import org.junit.Test;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.lang.reflect.Proxy;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;

public class Test1
{

    private String a = "1111";

    @Test
    public void test1(){
        MethodProxy proxy = new MethodProxy();
        Object o = Proxy.newProxyInstance(InterFaceInverBean.class.getClassLoader(), new Class[]{InterFaceInverBean.class}, proxy);
        InterFaceInverBean o1 = (InterFaceInverBean) o;
        System.out.println(o1.getnum());
    }

    @Test
    public void test2(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(InterFaceInverBean.class);
        enhancer.setCallback(new CglibProxy());
        InterFaceInverBean interFaceInverBean = (InterFaceInverBean) enhancer.create();
        System.out.println(interFaceInverBean.getnum());
    }

    /**
     * 测试获取ip
     */
    @Test
    public void test3() throws Exception {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && inetAddress instanceof Inet4Address
                        && !inetAddress.isLoopbackAddress()
                        && inetAddress.getHostAddress().indexOf(":") == -1) {
                    System.out.println(inetAddress.getHostAddress());
                }
            }
        }
    }

    @Test
    public void test4() throws Exception {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
        }
    }

    @Test
    public void test5(){
        PrivilegedAction net = new PrivilegedAction() {
            @Override
            public Object run() {
                return "123";
            }
        };
        Object o = AccessController.doPrivileged(net);
        System.out.println(o);
    }


    @Test
    public void test6() throws IllegalAccessException, InstantiationException {
       a();
    }

    @CallerSensitive
    private  void a() throws IllegalAccessException, InstantiationException {
        Class<?> callerClass = Reflection.getCallerClass(1);
        Test1 test1 = (Test1) callerClass.newInstance();
        System.out.println(test1.a);
        System.out.println(callerClass);
    }

}
