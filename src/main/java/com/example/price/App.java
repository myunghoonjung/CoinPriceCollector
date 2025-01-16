package com.example.price;

import com.example.price.collector.PriceCollectorThread;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
    	PriceCollectorThread collectorThread = new PriceCollectorThread();
        Thread thread = new Thread(collectorThread);
        thread.start();
    }
}
