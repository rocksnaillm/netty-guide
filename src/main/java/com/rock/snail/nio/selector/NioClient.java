package com.rock.snail.nio.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {

    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                Thread.sleep(1000);
                System.out.println("建立连接需要时间,请等待一会...");
            }
        }
        String mag = "测试客户端连接";
        ByteBuffer byteBuffer = ByteBuffer.wrap(mag.getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
