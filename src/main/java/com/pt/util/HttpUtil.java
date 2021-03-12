package com.pt.util;
import com.alibaba.fastjson.JSONObject;
import com.pt.context.BeanContext;
import com.pt.exception.SystemStatusEnum;
import com.pt.router.RouterInfo;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;
public class HttpUtil
{
    /**
     * 获取路由对象
     * @param context
     * @param request
     * @return
     */
    public static RouterInfo getRouterInfo(BeanContext context,Object... request){
        HttpRequest httpRequest = (HttpRequest) request[0];
        String key = CharUtil.removeSlash(httpRequest.uri());
        // 存在往uri中写入参数的情况
        if (key.contains("?"))
        {
            key = key.split("\\?")[0];
        }
        RouterInfo routerInfo = context.getRouterInfo(key);
        return routerInfo;
    }
    /**
     * get请求拆分uri 获取一个map
     * @param uri
     * @return
     */
    public static Map<String,Object> splitUriToMap(String uri){
        Map<String,Object> map = new HashMap<>();
        SystemAssert.isTrue(uri.contains("?"), SystemStatusEnum.URI_MUST_HAVE_PARAMETERS);
        // 获取问号后参数串
        String substring = uri.substring(uri.indexOf("?")+1);
        SystemAssert.isTrue(uri.contains("&") && uri.contains("="),SystemStatusEnum.URI_FORMAT_ERROR);
        // 将这部分参数串进行解析
        String[] split = substring.split("&");
        for (String s : split)
        {
            String[] split1 = s.split("=");
            map.put(split1[0],split1[1]);
        }
        return map;
    }


    /**
     * 404
     * @return
     */
    public static HttpResponse build_404(){
        String msg = "<html><head><title>test</title></head><body>404</body></html>";
        // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND,
                Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        return response;
    }

    public static HttpResponse build_500(){
        String msg = "<html><head><title>test</title></head><body>error</body></html>";
        // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        return response;
    }

    /**
     * 正常构建HttpResponse
     * @param o
     * @return
     */
    public static HttpResponse buildResponse(Object o){
        String jsonString = JSONObject.toJSONString(o);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(jsonString, CharsetUtil.UTF_8));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        return response;
    }
}
