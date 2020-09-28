package com.rock.snail.netty.groupchat;

import com.sun.org.apache.bcel.internal.generic.NEW;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

   private static ChannelGroup channelGroup =   new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("客户端:"+ctx.channel().remoteAddress()+",加入");
        channelGroup.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //会自动 将 ChannelGroup 减少
        channelGroup.writeAndFlush("客户端:"+ctx.channel().remoteAddress()+",离开");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ",上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ",离线了");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch->{
            if(ch!=channel){
                ch.writeAndFlush("客户端:"+ctx.channel().remoteAddress()+",发送了消息:"+msg);
            }else {
                ch.writeAndFlush("[自己]:"+ctx.channel().remoteAddress()+",发送了消息:"+msg);
            }
        });
    }




}
