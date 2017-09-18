package com.example.susmi.stockprice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

/**
 * Created by susmi on 24-Aug-17.
 */

public class card_fragment extends android.support.v4.app.Fragment {

    ArrayList<StockModel> listStockItems = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar spin;

    HashMap<String, HashMap<String, String>> finalStockOutput = new HashMap<>();
    String StockNamesList[] = {"AXIS", "ICICI", "UTI","AXIS", "ICICI", "UTI"};
    String StockRates[] = {"25", "35", "45","25", "35", "45"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String jsonInputString = loadJsonInput();
        loadAndParseJsonInput(jsonInputString);
        //initializeList();
        getActivity().setTitle("The Stock App");
    }

    //Load JSON input file
    public String loadJsonInput(){
        String jsonInputString = null;
        try{
            //InputStream inputStream = getActivity().getAssets().open("input.json");
            InputStream inputStream = getActivity().getAssets().open("modified_input.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            jsonInputString = new String(buffer, "UTF-8");
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
        Log.d("JSON String ::", jsonInputString);

        return jsonInputString;
    }

    //Parse the input
    public JSONObject loadAndParseJsonInput(String jsonStringInput){
        JSONObject jsonInputObject;
        try{
            jsonInputObject = new JSONObject(jsonStringInput);
            //Log.d("JSON Obj :: ", ""+jsonInputObject.toString());
            JSONArray inputStockArray = jsonInputObject.getJSONArray("stocks");
            //Log.d("JSON ARR ::: ", arr.toString());

            HttpRequest newRequest = new HttpRequest();
            newRequest.execute(inputStockArray);

            /*for ( int i = 0 ; i < inputStockArray.length() ; i++ ){
                JSONObject eachStockObject = inputStockArray.getJSONObject(i);

                Log.d("Object :::", eachStockObject.toString());

                String eachStockName = eachStockObject.get("stock_name").toString();
                String eachStockUrl = eachStockObject.get("api_url").toString();

                Log.d("Key :::", eachStockName);
                Log.d("Url :::", eachStockUrl);

                HttpRequest newRequest = new HttpRequest();
                newRequest.execute(eachStockUrl, eachStockName);
            }*/
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return jsonInputObject;
    }

    // Creates stock model for each stock and adds it to list
    public void initializeList(){
        listStockItems.clear();

        for ( int i = 0 ; i < StockNamesList.length ; i++ ){
            StockModel stockModel = new StockModel();
            stockModel.setStockName(StockNamesList[i]);
            stockModel.setStockPrice(StockRates[i]);
            listStockItems.add(stockModel);
        }
    }

    // Creates stock model for each stock and adds it to list
    public void initializeListWithFinalOutput(){
        listStockItems.clear();

        List<String> sortedStockNameList = new ArrayList<>(finalStockOutput.keySet());
        Collections.sort(sortedStockNameList);
        Log.d("Sorted Name",sortedStockNameList.toString());

        for( String str : sortedStockNameList){
            StockModel stockModel = new StockModel();
            stockModel.setStockName(str);

            HashMap<String, String> value = new HashMap<>(finalStockOutput.get(str));
            Log.d("Str "+str+ " :::",value.toString());
            stockModel.setStockPrice(value.get("Price").toString());
            stockModel.setStockNav(value.get("Nav").toString());
            stockModel.setStockPriceChange(value.get("Change").toString());
            stockModel.setStockPriceChangePercent(value.get("PercentChange").toString());
            listStockItems.add(stockModel);
        }

        /*Iterator it = finalStockOutput.entrySet().iterator();
        while(it.hasNext()) {
            StockModel stockModel = new StockModel();
            HashMap.Entry pair = (HashMap.Entry)it.next();
            stockModel.setStockName(pair.getKey().toString());
            HashMap<String, String> value = (HashMap<String, String>) pair.getValue();
            stockModel.setStockPrice(value.get("Price").toString());
            stockModel.setStockNav(value.get("Nav").toString());
            stockModel.setStockPriceChange(value.get("Change").toString());
            stockModel.setStockPriceChangePercent(value.get("PercentChange").toString());
            listStockItems.add(stockModel);
        }*/

        /*for ( int i = 0 ; i < StockNamesList.length ; i++ ){
            StockModel stockModel = new StockModel();
            stockModel.setStockName(StockNamesList[i]);
            stockModel.setStockPrice(StockRates[i]);
            listStockItems.add(stockModel);
        }*/
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {

        public TextView stockNameTextView;
        public TextView stockPriceTextView, stockNavTextView, stockChangeTextView, stockChangePercentageTextView;

        public StockViewHolder(View v){
            super(v);

            stockNameTextView = (TextView) v.findViewById(R.id.StockNameTextView);
            stockPriceTextView = (TextView) v.findViewById(R.id.StockPriceTextView);
            stockNavTextView = (TextView) v.findViewById(R.id.StockNavTextView);
            stockChangeTextView = (TextView) v.findViewById(R.id.StockChangeTextView);
            stockChangePercentageTextView = (TextView) v.findViewById(R.id.StockChangePercentageTextView);
        }

    }

    public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

        private ArrayList<StockModel> list;

        public StockAdapter(ArrayList<StockModel> data){
            list = data;
        }

        @Override
        public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Create new view

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);
            StockViewHolder holder = new StockViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final StockViewHolder holder, int position) {
            holder.stockNameTextView.setText(list.get(position).getStockName());
            holder.stockPriceTextView.setText(list.get(position).getStockPrice());
            holder.stockNavTextView.setText(list.get(position).getStockNav());
            holder.stockChangeTextView.setText(list.get(position).getStockPriceChange());
            holder.stockChangePercentageTextView.setText(list.get(position).getStockPriceChangePercent());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.cardRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
        myLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listStockItems.size() > 0 & recyclerView != null){
            recyclerView.setAdapter(new StockAdapter(listStockItems));
        }
        recyclerView.setLayoutManager(myLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public class HttpRequest extends AsyncTask<JSONArray, Void, HashMap<String, HashMap<String, String>>> {

        @Override
        protected HashMap<String, HashMap<String, String>> doInBackground(JSONArray... jsonArrays) {

            JSONArray stockArray = jsonArrays[0];

            //Log.d("Params JSON ARRAY",stockArray.toString());
            for ( int i = 0 ; i < stockArray.length() ; i++ ){
                try{
                    StringBuilder outputString = new StringBuilder();
                    String output;
                    String price = "null", change = "null", change_percent = "null";

                    JSONObject eachStockObject = stockArray.getJSONObject(i);

                    Log.d("Object :::", eachStockObject.toString());

                    String eachStockName = eachStockObject.get("stock_name").toString();
                    String eachStockUrl = eachStockObject.get("api_url").toString();
                    String eachStockNav = eachStockObject.get("deposit_nav").toString();
                    String eachStockunits = eachStockObject.get("units").toString();

                    Log.d("Key :::", eachStockName);
                    Log.d("Url :::", eachStockUrl);

                    HashMap<String, String> result = new HashMap<String, String>();

                    try{
                        /*URL url = new URL(eachStockUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");

                        if (conn.getResponseCode() != 200){
                            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                        }

                        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));


                        while (( output = br.readLine()) != null){
                            outputString.append(output);
                        }

                        Log.d("Output String :: ", "" + outputString.toString());*/

                        //Connect ot the website
                        Document document = Jsoup.connect(eachStockUrl).get();

                        //Using elements to get the class data
                        Elements span = document.select("span[class=pr]");
                        //Elements chg = document.select("span[class=id-price-change nwp] span[class=ch bld]");
                        Elements main_change_span = document.select("span[class=ch bld]");
                        Element change_span = main_change_span.first();


                        price = span.html();
                        change = change_span.child(0).html();
                        change_percent = change_span.child(1).html();

                        //String change = chg.html();
                        Log.d("Scrape :: ", span.toString());
                        Log.d("Output scrape 1:: ", price);
                        Log.d("Output scrape 3:: ", change);
                        //Log.d("Output scrape :: ", change_percent);
                        Log.d("Output scrape 4:: ", change_percent.substring(1,6));



                    }catch (MalformedURLException e) {
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    //String requiredOutput = outputString.toString().substring(3);
                    //Log.d("Required String :: ", "" + requiredOutput);

                    //result = parseStringToJson(requiredOutput, eachStockNav, eachStockunits);
                    result.put("Price", price);
                    result.put("Change", change);
                    result.put("PercentChange", change_percent.substring(1,6));
                    result.put("Nav", eachStockNav);
                    result.put("Units", "0");

                    Log.d("Result :: ", ""+result);

                    finalStockOutput.put(eachStockName, result);

                } catch (JSONException e){
                    e.printStackTrace();
                    return null;
                }
            }
            return finalStockOutput;
        }

        @Override
        protected void onPostExecute(HashMap<String, HashMap<String, String>> result) {
            //super.onPostExecute(stringHashMapHashMap);
            Log.d("Final Output",result.toString());

            initializeListWithFinalOutput();

            spin = (ProgressBar)getView().findViewById(R.id.progressBar);
            spin.setVisibility(View.GONE);

            recyclerView = (RecyclerView) getView().findViewById(R.id.cardRecyclerView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
            myLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            if (listStockItems.size() > 0 & recyclerView != null){
                recyclerView.setAdapter(new StockAdapter(listStockItems));
            }
            recyclerView.setLayoutManager(myLayoutManager);
        }
    }

    public HashMap<String, String> parseStringToJson(String requiredOutput, String nav, String units){

        HashMap<String, String> hMap = new HashMap<String, String>();

        try {
            JSONArray arr = new JSONArray(requiredOutput);
            Log.d("Array JSON :: ",""+arr);

            JSONObject obj = arr.getJSONObject(0);

            //Log.d("Price :: ", ""+obj.getString("l"));
            hMap.put("Price",obj.getString("l"));
            hMap.put("Change",obj.getString("c"));
            hMap.put("PercentChange",obj.getString("cp"));
            hMap.put("Nav", nav);
            hMap.put("Units", units);

            Log.d("R :: ", ""+hMap);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*try {
            JSONObject obj = new JSONObject(requiredOutput);
            Log.d("JSON Object :: ",""+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return hMap;
    }


}
