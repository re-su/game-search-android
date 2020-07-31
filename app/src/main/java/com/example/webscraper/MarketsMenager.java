package com.example.webscraper;

import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class MarketsMenager {

    private HashMap<String, Market> markets;
    private CountDownLatch cdl;
    private String toFind;
    private Results results;

    public MarketsMenager(@Nullable Bundle savedInstanceState){
        markets = new HashMap<>();
        this.toFind = savedInstanceState.getString("find");

        results = new Results();

        if(savedInstanceState.getBoolean("steam")) markets.put("steam", new Steam());
        if(savedInstanceState.getBoolean("gog")) markets.put("gog", new Gog());
        if(savedInstanceState.getBoolean("ws")) markets.put("windowsstore", new WindowsStore());

        cdl = new CountDownLatch(markets.size());
    }

    public void start(){
        for (String key : markets.keySet()) {
            new marketSearch(toFind, markets.get(key), cdl).run();
        }
    }


    public HashMap<String, Market> getMarkets(){
        return markets;
    }

    public CountDownLatch getCdl(){
        return cdl;
    }

}
