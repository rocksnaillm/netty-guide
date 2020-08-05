package com.rock.snail.nio.buffer;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(5);
        for (int i=0;i<buffer.capacity();i++){
            buffer.put(i*2);
        }

        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
