package com.example.webscraper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Results {
    HashMap<Integer, LinkedList<Result>> resultsHashMap; // Number of records is similar to number of shops // Each records includes list of results from this shop
    List<Result> list; // List is used to easier store and display results
    int size;


    public Results(){
        resultsHashMap = new HashMap<>();
        size = 0;
    }

    public void put(int key, LinkedList<Result> list){
        this.resultsHashMap.put(key, list);
    }

    public void put(HashMap<String, Market> markets){
        list = new LinkedList<Result>();
        int newKey = 0;
        for(String key : markets.keySet()){
            put(newKey++, markets.get(key).getList());
        }
    }


    public int getSize() {
        int temp = 0;
        for (int key : resultsHashMap.keySet()) {
            temp += resultsHashMap.get(key).size();
        }

        return temp;
    }

    public void orderByAccuracy(){
        list = new LinkedList<>();
        LinkedList<Iterator<Result>> iterators = new LinkedList<>();

        for(int key : resultsHashMap.keySet()){
           iterators.add(resultsHashMap.get(key).iterator());
        }

        int tempIndex = 0, addedItems = 0;
        while(addedItems < getSize()){
           if(iterators.get(tempIndex % resultsHashMap.size()).hasNext()) {
               list.add(iterators.get(tempIndex % resultsHashMap.size()).next());
               addedItems++;
           }

           tempIndex++;
           //Log.i("ROZMIAR_HASH", Integer.toString(resultsHashMap.size()) + " " + Integer.toString(shopsDone));
        }

    }

    public void orderByShop(){
        list = new LinkedList<>();
        LinkedList<Iterator<Result>> iterators = new LinkedList<>();

        for(int key : resultsHashMap.keySet()){
            iterators.add(resultsHashMap.get(key).iterator());
        }

        int listNum = 0;
        for(int i = 0; i < getSize() + resultsHashMap.size(); i++){
            if(iterators.get(listNum).hasNext()) list.add(iterators.get(listNum).next());
            else listNum++;
        }
    }


    public Result get(int index){
        try{
            return list.get(index);
        } catch(Exception e){}
        return null;
    }

    public List<Result> getList(){
        try{
            return list;
        } catch(Exception e){}
        return null;
    }

    public void aSort(){
        list = new LinkedList<Result>();

        for (int key : resultsHashMap.keySet()) {
            list.addAll(resultsHashMap.get(key));
        }
        Collections.sort(list);
    }

    public void dSort(){
        list = new LinkedList<Result>();

        for (int key : resultsHashMap.keySet()) {
            list.addAll(resultsHashMap.get(key));
        }

        Collections.sort(list, Collections.reverseOrder());
    }

    public void display(Activity activity, final Context context, String toFind) throws MalformedURLException {
        try {


            StringBuilder resultTxt = new StringBuilder();
            StringBuilder priceResultTxt = new StringBuilder();

            LinearLayout layout = (LinearLayout) ((Activity) context).findViewById(R.id.LinearLayout2);

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT);

            layout.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView layoutHeader = new TextView(context);
            layoutHeader.setText("Szukana fraza: " + toFind);
            layoutHeader.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(layoutHeader);
/*==========================================
            If there is no match
* ===========================================*/
            if (getSize() == 0) {
                resultTxt.append("Brak wyników");

                TextView tv = new TextView(context);
                tv.setLayoutParams(lparams);
                tv.setText(resultTxt.toString());
                layout.addView(tv);
            }
/*==========================================
            If there is a match
* ===========================================*/
            for (int i = 0; i < getSize(); i++) {
                resultTxt.setLength(0);
                priceResultTxt.setLength(0);

/*============================================================
            MARKET ICON
* ============================================================*/
                ImageView marketIcon = new ImageView(context);
                RelativeLayout.LayoutParams marketIconParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                marketIconParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                marketIconParams.setMargins(10, 0, 0, 0);
                marketIcon.setLayoutParams(marketIconParams);

                if (get(i).getMarketType() == "steam") {
                    marketIcon.setImageResource(R.drawable.ic_steam);
                } else if (get(i).getMarketType() == "gog") { ;
                    marketIcon.setImageResource(R.drawable.ic_gog);
                } else if (get(i).getMarketType() == "ws") {
                    marketIcon.setImageResource(R.drawable.ic_ws);
                }


/*==========================================
            PRICE / PRICE TEXT_VIEW
* ===========================================*/

                TextView priceTV = new TextView(context);
                RelativeLayout.LayoutParams priceParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                priceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                priceParams.setMargins(0, 0, 10, 0);

                priceTV.setLayoutParams(priceParams);

                if (get(i).getPrice() == 0) {
                    priceResultTxt.append("Free to Play\n");
                }
                else if (get(i).getPrice() == -1) {
                    priceResultTxt.append("Wchodzi w skład Game Pass\n");
                } else {
                    String price = Double.toString(get(i).getPrice());
                    priceResultTxt.append(price + " PLN\n");
                }

                priceTV.setText(priceResultTxt.toString());
                priceTV.setTextColor(Color.rgb(255, 255, 255));
/*==========================================
            BOX - LINEAR LAYOUT SETTINGS
* ===========================================*/
                LinearLayout box = new LinearLayout(context);
                LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(
                        MATCH_PARENT, WRAP_CONTENT);
                box.setLayoutParams(boxParams);
                box.setGravity(Gravity.CENTER_HORIZONTAL);
                box.setOrientation(LinearLayout.VERTICAL);

                box.setBackgroundResource(R.drawable.tags_rounded_corners);
                GradientDrawable drawableLayout = (GradientDrawable) box.getBackground();
                drawableLayout.setColor(Color.rgb(62, 64, 64));
/*============================================================
            ImageView - download image / set_params
* ============================================================*/
                ImageView imageView = new ImageView(context);

                new DownLoadImageTask(imageView).execute(get(i).getImgSrc());
                //setting image position
                imageView.setLayoutParams(new LinearLayout.LayoutParams(400,
                        800));
                imageView.getLayoutParams().height = 250;


/*============================================================
            BOTTOM LAYOUT - PRICE AND MARKET ICON
* ============================================================*/
                RelativeLayout bottom = new RelativeLayout(context);
                RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT);
                bottom.setLayoutParams(bottomParams);
/*============================================================
            TITLE TEXT_VIEW
* ============================================================*/
                resultTxt.append(get(i).getTitle() + "  " + i + "\n");
                TextView resultTitle = new TextView(context);
                resultTitle.setText(resultTxt.toString());
                resultTitle.setGravity(Gravity.CENTER);
                resultTitle.setTextColor(Color.rgb(255, 255, 255));
                resultTitle.setTypeface(null, Typeface.BOLD);
/*============================================================
                        OnClick Listener
* ============================================================*/
                final  Uri uri = Uri.parse(get(i).getURI());
                box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                });
/*============================================================
                Setting box margins / adding layouts
* ============================================================*/
                boxParams.setMargins(0, 0, 0, 20);

                box.addView(imageView);
                box.addView(resultTitle);
                bottom.addView(marketIcon);
                bottom.addView(priceTV);
                box.addView(bottom);
                layout.addView(box);
            }
        }
        catch(Exception e){
            Log.i("Exception - result.java", e.getMessage());
        }
    }

    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
