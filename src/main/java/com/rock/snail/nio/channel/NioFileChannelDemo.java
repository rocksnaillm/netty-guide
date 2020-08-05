package com.rock.snail.nio.channel;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannelDemo {


    private ByteBuffer buffer;

    public static void main(String[] args) {

    }



    public static void fileChannelDemo01() throws Exception{
        String str = "测试buffer写入数据到channel";
        FileOutputStream outputStream = new FileOutputStream("");
        ByteBuffer buffer = ByteBuffer.allocate(str.getBytes().length);
        buffer.put(str.getBytes());
        FileChannel channel = outputStream.getChannel();
        buffer.flip();
        channel.write(buffer);
        channel.close();
        outputStream.close();
    }


    public static void fileChannelDemo02() throws Exception{
        File file = new File("");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        channel.read(buffer);
        buffer.flip();
        String msg = new String(buffer.array());
        System.out.println(msg);
        channel.close();
        fileInputStream.close();
    }


    public static void copyFile01() throws Exception{
        File file = new File("");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream outputStream = new FileOutputStream("");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true){
            buffer.clear();
            int read = inputStreamChannel.read(buffer);
            if(read == -1){
                break;
            }
            buffer.flip();
            outputStreamChannel.write(buffer);
        }
        inputStreamChannel.close();
        fileInputStream.close();
        outputStreamChannel.close();
        outputStream.close();
    }

    public static void copyFile02() throws Exception{
        File file = new File("");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream outputStream = new FileOutputStream("");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();
        outputStreamChannel.transferFrom(inputStreamChannel,0,inputStreamChannel.size());
        inputStreamChannel.close();
        fileInputStream.close();
        outputStreamChannel.close();
        outputStream.close();
    }
}
