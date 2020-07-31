package com.example.webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.util.Log;
import java.io.IOException;


public class Steam extends Market {
    int Mistakes = 0;
    public Steam() {
        super();
        shortcut = "steam";
    }

    @Override
    public void getProduct(final String title) {
        //result.clear();
        try {
            Log.i("START -> ", "STEAM");

            Document doc = Jsoup.connect("https://store.steampowered.com/search/?term="+title).get();
            Elements links = doc.select("a[href]");
            Elements spans = doc.select("span.title");
            Elements prices = doc.select("div.search_price");
            Elements imgSrcs = doc.select("img[src~=(steam/apps)|(steam/subs)|(steam/bundles)]");
            String priceTxt;
            StringBuilder tempStr = new StringBuilder();
            int i = 0;
            double price = 0;

            for(Element link: links) {
                if (link.attr("data-ds-appid") != "" || link.attr("data-ds-bundleid") != "")
                {
                    Log.i("TYTUL: ", spans.get(i).text());
                    Log.i("URK: ", link.attr("href").toString());
                    Log.i("==", "====================");

                    //Parsing price

                    priceTxt = prices.get(i).text();

                    if(priceTxt.length() == 0) {i++; continue;}
                    else if((int)priceTxt.charAt(0) > 57 || (int)priceTxt.charAt(0) < 48) price = 0;
                    else{
                        int j = priceTxt.length() - 1;

                        while(j >= 0){
                            if((int)priceTxt.charAt(j) <= 57 && (int)priceTxt.charAt(j) >= 48) tempStr.append(priceTxt.charAt(j));
                            if(priceTxt.charAt(j) == ',') tempStr.append('.');

                            if(tempStr.length() > 0 && priceTxt.charAt(j) == ' ') {break;}
                            j--;
                        }

                        tempStr.reverse();

                        price = Double.parseDouble(tempStr.toString());
                        tempStr = new StringBuilder();
                    }

                    Result temp = new Result(spans.get(i).text(), link.attr("href").toString(), price, imgSrcs.get(i).attr("src"), "steam");
                    results.add(temp);
                    i++;
                }
            }
        }
        catch(IOException e){
            //Mistakes++;
            //if(Mistakes < 200) getProduct(title);
            Log.i("STEAM EXCEPTION", e.toString());
        }
    }
}
