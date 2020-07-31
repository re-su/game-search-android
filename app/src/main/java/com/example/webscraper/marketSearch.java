package com.example.webscraper;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class marketSearch extends Thread {
    private String ToFind;
    private CountDownLatch countDownLatch;
    private Market market;

    public marketSearch(String toFind, Market market, CountDownLatch countDownLatch_){
        countDownLatch = countDownLatch_;
        ToFind = toFind;
        this.market = market;
    }

    @Override
    public void run(){
        market.getProduct(ToFind);
        countDownLatch.countDown();
    }

}
