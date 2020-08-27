package com.rock.snail.nio;

public class TestEsx {
    public static void main(String[] args) {
        try {
            throw new RuntimeException("111");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
