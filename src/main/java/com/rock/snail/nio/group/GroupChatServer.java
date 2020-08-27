package com.rock.snail.nio.group;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 群聊系统服务端
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final static int PORT=6667;

    //构造器,初始化操作
    public GroupChatServer() {
        try {
            //获得一个Selector
            selector = Selector.open();
            //获得一个ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定网络端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //注册OP_ACCEPT监听事件
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try {
            while (true){
                int count = selector.select();
                if(count > 0){
                    //获取事件的SelectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        //监听到accept事件
                        if(key.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            //设置为非阻塞
                            socketChannel.configureBlocking(false);
                            //注册OP_READ监听事件
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() +" 上线了");
                        }
                        //监听到READ事件
                        if(key.isReadable()){
                            readData(key);
                        }
                        //移除SelectionKey
                        iterator.remove();
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 读取消息
     */
    public void readData(SelectionKey key){
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(buffer);
            if(count > 0){
                String msg = new String(buffer.array());
                System.out.println("form 客户端:"+msg);
                this.sendInfoToOther(msg,socketChannel);
            }
        }catch (Exception e){
            try {
                System.out.println(socketChannel.getRemoteAddress()+" 离线了");
                key.cancel();
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *  发消息给其他客户端
     */
    public void sendInfoToOther(String mag,SocketChannel self){
        for (SelectionKey key : selector.keys()) {
            Channel channel = key.channel();
            if(channel instanceof SocketChannel){
                try {
                    SocketChannel socketChannel = (SocketChannel) channel;
                    socketChannel.write(ByteBuffer.wrap(mag.getBytes()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        new GroupChatServer().listen();
    }
}
