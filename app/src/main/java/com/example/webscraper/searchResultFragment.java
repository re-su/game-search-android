package com.example.webscraper;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class searchResultFragment extends Fragment {


    public searchResultFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        new search(savedInstanceState).execute();
    }


    public class search extends AsyncTask< Void, Void, Void > {
        Bundle savedInstanceState;
        MarketsMenager marketsMenager;

        public search(@Nullable Bundle savedInstanceState){
            this.savedInstanceState = getArguments();
            marketsMenager = new MarketsMenager(this.savedInstanceState);
        }

        @Override
        protected Void doInBackground(Void... params) {

            marketsMenager.start();

            try{
                marketsMenager.getCdl().await();
            } catch(Exception e){
                Log.i("searchResultFragment.java error", e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            int option = savedInstanceState.getInt("option");

            Results myResults = new Results();

            myResults.put(marketsMenager.getMarkets());

            if(option == 0) myResults.orderByAccuracy();
            if(option == 1) myResults.aSort();
            if(option == 2) myResults.dSort();
            if(option == 3) myResults.orderByShop();

            try{
                myResults.display(getActivity(), getContext(), savedInstanceState.getString("find"));
            }
            catch(Exception e){
                Log.i("searchResultFragment.java error", e.getMessage());
            }
        }
    }

}
