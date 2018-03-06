package com.watent.soa.netty;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.watent.soa.bean.Service;
import com.watent.soa.invoke.ReflectionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Atom
 */
public class NettyServerInHandler extends ChannelInboundHandlerAdapter {

    //Netty客户端有消息过来的时候调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        //初始化一个字节数组
        byte[] bytes = new byte[byteBuf.readableBytes()];
        //ByteBuf读到字节数组
        byteBuf.readBytes(bytes);
        String byteBufStr = new String(bytes);
        System.out.println(byteBufStr);

        byteBuf.release();
        String response = invokeService(byteBufStr);

        ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
        encoded.writeBytes(response.getBytes());
        ctx.writeAndFlush(encoded);
        ctx.close();

    }

    /**
     * JSONObject requestparam = JSONObject.parseObject(param);
     * //要从远程的生产者的spring容器中拿到对应的serviceid实例
     * String serviceId = requestparam.getString("serviceId");
     * String methodName = requestparam.getString("methodName");
     * JSONArray paramTypes = requestparam.getJSONArray("paramTypes");
     * //这个对应的方法参数
     * JSONArray methodParamJa = requestparam.getJSONArray("methodParams");
     * //这个就是反射的参数
     * Object[] objs = null;
     * if (methodParamJa != null) {
     * objs = new Object[methodParamJa.size()];
     * int i = 0;
     * for (Object o : methodParamJa) {
     * objs[i++] = o;
     * }
     * }
     */

    private String invokeService(String param) {

        JSONObject requestParam = JSONObject.parseObject(param);
        String serviceId = requestParam.getString("serviceId");
        String methodName = requestParam.getString("methodName");
        JSONArray paramTypes = requestParam.getJSONArray("paramTypes");
        //方法参数
        JSONArray methodParamJa = requestParam.getJSONArray("methodParams");
        //反射参数
        Object[] objs = null;
        if (null != methodParamJa) {
            objs = new Object[methodParamJa.size()];
            int i = 0;
            for (Object o : methodParamJa) {
                objs[i] = o;
            }
        }
        //拿到spring的上下文
        ApplicationContext applicationContext = Service.getApplicationContext();
        //服务实例
        Object serviceBean = applicationContext.getBean(serviceId);
        //这个方法的获取，要考虑到这个方法的重载
        Method method = ReflectionUtil.getMethod(serviceBean, methodName, paramTypes);
        if (null != method) {
            try {
                Object result = method.invoke(serviceBean, objs);
                return result.toString();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            return "------ No such method ------" + methodName;
        }
        return null;
    }

}
