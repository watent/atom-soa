package com.watent.soa.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Atom
 */
public class NettyUtil {

    private NettyUtil() {
    }

    public static void startServer(String host, String port) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new NettyServerInHandler());
                        }
                    }).
                    option(ChannelOption.SO_BACKLOG, 128);

            ChannelFuture f = b.bind(Integer.parseInt(port)).sync();
            //服务端阻塞
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    public static String sendMsg(String host, String port, String sendmsg) throws Exception {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final StringBuffer resultmsg = new StringBuffer();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new NettyClientInHandler(resultmsg, sendmsg));
                }
            });
            //这个是连接服务端，一直在等待着服务端的返回消息，返回的信息封装到future，可以监控线程的返回
            ChannelFuture f = b.connect(host, Integer.parseInt(port))
                    .channel()
                    .closeFuture()
                    .await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
        return resultmsg.toString();
    }


}
