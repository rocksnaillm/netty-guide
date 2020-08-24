package com.rock.snail.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    public static void main(String[] args) throws Exception {
        //创建缓存线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        //创建serverSocket,指定端口
        ServerSocket serverSocket = new ServerSocket(6666);
        //等待客户端连接
        while (true){
            System.out.println("等待客户端连接...");
            Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端...");
            //放到线程池处理核心业务
            executorService.execute(()->{
                try {
                    handle(socket);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

        }
    }

    public static void handle(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        InputStream inputStream = socket.getInputStream();
        try {
            while (true){
                int read = inputStream.read(bytes);
                if(read != -1){
                    String msg = new String(bytes, 0, read);
                    System.out.println(msg);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            inputStream.close();
        }
    }
}
