package com.example.webscraper;
import android.widget.ImageView;

import java.util.*;

public abstract class Market{
    protected ImageView marketIcon;
    protected String shortcut;
    protected LinkedList<Result> results;
    public abstract void getProduct(String title);

    public Market() {
        results = new LinkedList<Result>();
    }

    public Market(LinkedList<Result> results) {
        this.results = results;
    }
    public LinkedList<Result> getList(){return results;}
}
