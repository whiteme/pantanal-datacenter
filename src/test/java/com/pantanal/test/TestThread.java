package com.pantanal.test;

import java.util.concurrent.Executors;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;

class TestThread{

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
    

    public static void main(String args[]){

    }
}