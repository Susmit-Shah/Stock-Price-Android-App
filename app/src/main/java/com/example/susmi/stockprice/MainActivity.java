package com.example.susmi.stockprice;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String myURL = "https://www.google.com/finance/info?q=MUTF_IN:AXIS_LT_EQUI_1TCMI7H";

        //HttpRequest getRequest = new HttpRequest();
        //getRequest.execute(myURL);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            fragment = new card_fragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }


    public HashMap<String, String> parseStringToJson(String requiredOutput){

        HashMap<String, String> hMap = new HashMap<String, String>();

        try {
            JSONArray arr = new JSONArray(requiredOutput);
            Log.d("Array JSON :: ",""+arr);

            JSONObject obj = arr.getJSONObject(0);

            //Log.d("Price :: ", ""+obj.getString("l"));
            hMap.put("Price",obj.getString("l"));
            hMap.put("Change",obj.getString("c"));
            hMap.put("PercentChange",obj.getString("cp"));

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

    public class HttpRequest extends AsyncTask<String, Void, HashMap<String, String>>{

        @Override
        protected HashMap<String, String> doInBackground(String... params) {

            StringBuilder outputString = new StringBuilder();
            String output;
            String stringUrl = params[0];

            HashMap<String, String> result = new HashMap<String, String>();

            try{
                URL url = new URL(stringUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200){
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));


                while (( output = br.readLine()) != null){
                    outputString.append(output);
                }

                Log.d("Output String :: ", "" + outputString.toString());

            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            String requiredOutput = outputString.toString().substring(3);
            Log.d("Required String :: ", "" + requiredOutput);

            result = parseStringToJson(requiredOutput);

            Log.d("Result :: ", ""+result);

            return result;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {
            //super.onPostExecute(stringStringHashMap);

            /*TextView priceView = (TextView)findViewById(R.id.price);
            TextView changeView = (TextView)findViewById(R.id.change);
            TextView percentChange = (TextView)findViewById(R.id.changePercent);


            priceView.setText(result.get("Price"));
            changeView.setText(result.get("Change"));
            percentChange.setText(result.get("PercentChange"));*/

        }
    }
}
