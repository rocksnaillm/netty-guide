package com.rock.snail.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServerNetty {

    private int port;

    public GroupChatServerNetty(int port) {
        this.port = port;
    }

    public void run(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);//1
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();//8

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder",new StringDecoder());
                            ch.pipeline().addLast("encoder",new StringEncoder());
                            ch.pipeline().addLast(new GroupChatServerHandler());
                        }
                    });

            System.out.println("netty 服务器准备启动");

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        new GroupChatServerNetty(7000).run();
    }
}
