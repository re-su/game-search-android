package com.example.webscraper;

public class Result implements Comparable<Result> {
    private String Title;
    private String Uri;
    private String Market;
    private Double Price;
    private String imageUrl;

    public Result(String title, String uri, double price, String imgUrl, String market){
        Title = title;
        Uri = uri;
        Price = price;
        Market = market;
        imageUrl = imgUrl;
    }

    public Result(String title, String uri) {
        Title = title;
        Uri = uri;
        Price = 0.0;
    }

    @Override
    public int compareTo(Result o){
        return this.getPrice().compareTo(o.getPrice());
    }

    public String getTitle(){
        return Title;
    }

    public String getURI(){
        return Uri;
    }

    public Double getPrice(){
        return Price;
    }

    public String getMarketType(){ return Market; }

    public String getImgSrc(){ return imageUrl; }
}