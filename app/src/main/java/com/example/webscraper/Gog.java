package com.example.webscraper;


import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class Gog extends Market {
    public Gog() {
        super();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void getProduct(final String title) {

        try {
            Log.i("START -> ", "GOG");
            String url = "https://www.gog.com/games/ajax/filtered?mediaType=game&page=1&search="+title+"&sort=popularity";

            JSONObject json, jsonUrl;
            StringBuilder imageUrlBuiler = new StringBuilder();
            String priceStr, titleStr, urlStr, imageUrl;
            double price;

            jsonUrl = readJsonFromUrl(url);
            int count = Integer.parseInt(jsonUrl.getString("totalResults"));

            for(int i = 0; i < count; i++){
                json = jsonUrl.getJSONArray("products").getJSONObject(i);
                titleStr = json.getString("title");
                urlStr = json.getString("url");
                priceStr = json.getJSONObject("price").getString("amount");

                imageUrlBuiler.setLength(0);
                imageUrlBuiler.append(json.getString("image")+"_product_tile_256.jpg");

                imageUrl = imageUrlBuiler.toString();

                imageUrl = imageUrl.substring(2, imageUrl.length());

                imageUrl = "https://"+imageUrl;


                Log.i("GOG_IMG","URL:"+imageUrl);
                Log.i("TAG2", "GOG");

                price = Double.parseDouble(priceStr);
                Result temp = new Result(titleStr, urlStr, price, imageUrl,"gog");
                results.add(temp);
            }

        } catch (Exception e) {
            Log.i("Exception(GOG) ", e.toString());
        }
    }

}
