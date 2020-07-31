package com.example.webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.util.Log;
import java.io.IOException;

public class WindowsStore extends Market {
    int Mistakes = 0;
    public WindowsStore() {
        super();
        shortcut = "ws";
    }

    @Override
    public void getProduct(final String title) {
        try {
            Log.i("START -> ", "WindowsStore");

            Document doc = Jsoup.connect("https://www.microsoft.com/pl-pl/search/shop/games?q="+title).get();
            Elements titles = doc.select("div[data-id~=(^[0-9])|(^[a-z])] .c-subheading-6");
            //Elements prices = doc.select("span[content~=(^[0-9])|(^B)|(^W)]");
            Elements prices = doc.select("span[itemprop='price']").not("span[content~=(^aria-hidden)");
            Elements urls = doc.select("div[data-id~=(^[0-9])|(^[a-z])] > a[href]");
            Elements imageUrls = doc.select("div[data-id~=(^[0-9])|(^[a-z])] source");

            StringBuilder priceTempStrB = new StringBuilder();
            StringBuilder imgUrlB = new StringBuilder();

            int i = 0;
            double price;

            /*for(Element priceTest: prices){
                Log.i("Price: ", priceTest.toString());
                Log.i("Price: ", priceTest.attr("content"));
            }

             */
            int n = 0;
            for(Element t: titles){
                Log.i("tyt" + n, t.text());
                n++;
            }
            for(Element game_title: titles){
                priceTempStrB.setLength(0);
                imgUrlB.setLength(0);

                if(prices.get(i).text().charAt(0) > 57 || prices.get(i).text().charAt(0) < 48){
                    price = 0;
                }
                else{
                    price = Double.parseDouble(prices.get(i).attr("content").toString().replace(",", "."));
                }

                int j = 0;
                while(imageUrls.get(i).attr("data-srcset").charAt(j) != '?' && j < imageUrls.get(i).attr("data-srcset").length()){
                    imgUrlB.append(imageUrls.get(i).attr("data-srcset").charAt(j));
                    j++;
                }

                Log.i("------------> "+i+": ", imgUrlB.toString());

                Result temp = new Result(game_title.text(), "https://microsoft.com"+urls.get(i).attr("href"), price, imageUrls.get(i).attr("data-srcset"),"ws");
                results.add(temp);
                i++;
            }


        }
        catch(IOException e){
            //Mistakes++;
            //if(Mistakes < 200) getProduct(title);
            Log.i("IOExc", e.toString());
        }
    }

}
