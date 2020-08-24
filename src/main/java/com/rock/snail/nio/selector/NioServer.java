package com.rock.snail.nio.selector;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    public static void main(String[] args) throws Exception{
        //打开一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //获得一个Selector
        Selector selector = Selector.open();
        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(6666));
        //设置非阻塞
        serverSocketChannel.configureBlocking(false);
        //通道向选择器注册监听事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环监听,等待客户端连接
        while (true){
            if(selector.select(1000)==0){
                System.out.println("等待1s没有连接事件发生...");
                continue;
            }
            //返回关注事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //根据key对应的通道做对应的处理
                //客户端连接事件
                if(selectionKey.isAcceptable()){
                    //获取对应的socket
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端有一个新的连接生成,hashCode= " + socketChannel.hashCode());
                    //设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector,事件类型为OP_READ,并设置默认缓冲区
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                //OP_READ事件
                if(selectionKey.isReadable()){
                    //获取对应的SocketChannel
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    //获取对应的Buffer
                    ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
                    int read = 0;
                    while ( (read = channel.read(byteBuffer)) > 0){
                        byteBuffer.flip();
                        System.out.println("客户端发送消息:----" + new java.lang.String(byteBuffer.array()));
                        byteBuffer.clear();
                    }
                }
                //手动移除selectionKey,防止重复操作
                iterator.remove();
            }
        }
    }
}
