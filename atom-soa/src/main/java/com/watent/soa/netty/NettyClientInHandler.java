package com.watent.soa.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Atom
 */
public class NettyClientInHandler extends ChannelInboundHandlerAdapter {

    private StringBuffer message;

    private String sendMsg;

    public NettyClientInHandler(StringBuffer message, String sendMsg) {
        this.message = message;
        this.sendMsg = sendMsg;
    }

    //链接成功以后触发  完成消息发送
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("------- channelActive -------");
        ByteBuf encoded = ctx.alloc().buffer(4 * sendMsg.length());
        encoded.writeBytes(sendMsg.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("------- channelRead -------");
        ByteBuf byteBuf = (ByteBuf) msg;
        //初始化一个字节数组
        byte[] bytes = new byte[byteBuf.readableBytes()];
        //ByteBuf读到字节数组
        byteBuf.readBytes(bytes);
        String byteBufStr = new String(bytes);
        System.out.println("server response msg:" + byteBufStr);
        message.append(byteBufStr);
        byteBuf.release();
    }
}
