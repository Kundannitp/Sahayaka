package com.pranks.doctalk.BlogsAndHome;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.pranks.doctalk.BlogsAndHome.Adapters.LiveAdapter;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.LiveClass;
import com.pranks.doctalk.Home;
import com.pranks.doctalk.R;
import com.pranks.doctalk.TestingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;


public class UpdateLive extends Fragment {

    public UpdateLive(){

    }
    View v;
    RecyclerView mRecyclerView;
    ArrayList<String> nameofcountry;
    ArrayList<LiveClass> liveClasses;
    LiveAdapter liveAdapter;
    private String TAG = UpdateLive.class.getSimpleName();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.liveupdates, container, false);

        ((Home) getActivity()).getSupportActionBar().setTitle("People Affected");
        new SetDataToTrain().execute();
        mRecyclerView=v.findViewById(R.id.live_update_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FloatingActionButton fab = v.findViewById(R.id.scroll_to_top);
        fab.setOnClickListener(view -> {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView
                    .getLayoutManager();
            layoutManager.smoothScrollToPosition(mRecyclerView, null, 0);
        });

        EditText searchField=v.findViewById(R.id.searchview);

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


        return v;
    }

    void filter(String text){
        ArrayList<LiveClass> liveClassestemp = new ArrayList();
        for(int k=0;k<liveClasses.size();k++){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(liveClasses.get(k).getCountryname().trim().toLowerCase().contains(text)){
                liveClassestemp.add(liveClasses.get(k));
            }
        }
        //update recyclerview
        liveAdapter.updateList(liveClassestemp);
    }


    public void setDataset(ArrayList<LiveClass> liveClasses1,String name){
        liveAdapter=new LiveAdapter(getContext(),liveClasses1);
//        Toast.makeText(getContext(), nameofcountry1.get(0)+" "+nameofcountry1.get(2), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
        mRecyclerView.setAdapter(liveAdapter);
        liveClasses=new ArrayList<>(liveClasses1);
        ProgressBar progressBar=v.findViewById(R.id.progress_circle2);
        progressBar.setVisibility(View.GONE);
    }

    public class SetDataToTrain extends AsyncTask<Void, Void, Void> {

        ArrayList<LiveClass> liveClasses=new ArrayList<>();
        String name="hello";
//        SetDataToTrain(){
//            liveClasses.add(new LiveClass("",0,0,0,0,0));
//
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.covid19api.com/summary";
            String jsonStr = sh.makeServiceCall(url);

            Log.i(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray areas=jsonObj.getJSONArray("Countries");
                    //LiveClass liveClass1=new LiveClass(jsonObj.getString(""));
                    for(int i=0;i<areas.length();i++){
                        JSONObject js=areas.getJSONObject(i);
                        String nameofcountry="";
                        int totalcases=0,totaldeath=0,totalrecovered=0,activecases=0,seriouscri=0;
                        try {
                            nameofcountry = js.getString("Country");
                        }catch (Exception e){}
                        try{
                            totalcases = js.getInt("TotalConfirmed");
                        }catch (Exception e){}
                        try {
                            totaldeath = js.getInt("TotalDeaths");
                        }catch (Exception e){}
                        try {
                            totalrecovered = js.getInt("TotalRecovered");
                        }catch (Exception e){}
                        activecases = totalcases - totaldeath - totalrecovered;
                        seriouscri = totaldeath;
                        LiveClass liveClass=new LiveClass(nameofcountry,totalcases,totaldeath,totalrecovered,activecases,seriouscri);
                        liveClasses.add(liveClass);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setDataset(liveClasses,name);
        }

    }


}
