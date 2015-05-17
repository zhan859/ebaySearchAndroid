package com.example.haozhang.ebaysearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends ActionBarActivity {
    public static final String JSONSTRING = "jsonstring";
    public static final String KEYWORD = "keyword";
    public EditText editKeyword;
    public EditText editPriceFrom;
    public EditText editPriceTo;
    public Spinner editSortBy;
    public ArrayAdapter<CharSequence> adapter;
    public TextView invalidKeyword;
    public TextView invalidPriceFrom;
    public TextView invalidPriceCompare;
    public TextView noResults;
    String true_key_word;
    String key_word;
    String min_price;
    String max_price;
    String sort_by = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editKeyword = (EditText) findViewById(R.id.etKeyword);
        editPriceFrom = (EditText) findViewById(R.id.etPriceFrom);
        editPriceTo = (EditText) findViewById(R.id.etPriceTo);
        editSortBy = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.sort_arrays,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editSortBy.setAdapter(adapter);
        invalidKeyword = (TextView)findViewById(R.id.handleKeyword);
        invalidPriceFrom = (TextView)findViewById(R.id.positiveNumberHandler);
        invalidPriceCompare= (TextView)findViewById(R.id.priceCompareHandler);
        noResults = (TextView)findViewById(R.id.noResultHandler);
        invalidKeyword.setVisibility(View.GONE);
        invalidPriceFrom.setVisibility(View.GONE);
        invalidPriceCompare.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnClick(View view) {
        float minPrice = 0;
        float maxPrice = 0;

        invalidKeyword.setVisibility(View.GONE);
        invalidPriceFrom.setVisibility(View.GONE);
        invalidPriceCompare.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);

        true_key_word = editKeyword.getText().toString();
        //Log.i("MainActivity",true_key_word);
        if (true_key_word.length() == 0) {
            invalidKeyword.setVisibility(View.VISIBLE);
            return;
        }
        try {
            key_word = URLEncoder.encode(true_key_word,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Log.i("MainActivity",key_word);
        min_price = editPriceFrom.getText().toString();
        //Log.i("MainActivity", "min_price = " + min_price);
        max_price = editPriceTo.getText().toString();
        if (min_price.length() != 0) {
            minPrice = Float.valueOf(min_price);
            if (minPrice < 0) {
                invalidPriceFrom.setVisibility(View.VISIBLE);
                return;
            }
        }
        if (max_price.length() != 0) {
            maxPrice = Float.valueOf(max_price);
            if(maxPrice < 0) {
                invalidPriceFrom.setVisibility(View.VISIBLE);
                return;
            }
            if(minPrice > maxPrice) {
                invalidPriceCompare.setVisibility(View.VISIBLE);
                return;
            }
        }
        String temp = editSortBy.getSelectedItem().toString();
        if(temp.equals("Best Match")){
            sort_by = "BestMatch";
        }else if(temp.equals( "Price: highest first" )){
            sort_by = "CurrentPriceHighest";
        }else if(temp.equals( "Price + Shipping: highest first")){
            sort_by = "PricePlusShippingHighest";
        }else if(temp.equals("Price + Shipping: lowest first")){
            sort_by = "PricePlusShippingLowest";
        }

        new MyAsyncTask().execute(key_word, min_price, max_price, sort_by, "5", "1");

    }

    public void btnClear(View view) {
        editKeyword.setText("");
        editPriceFrom.setText("");
        editPriceTo.setText("");
        invalidKeyword.setVisibility(View.GONE);
        invalidPriceFrom.setVisibility(View.GONE);
        invalidPriceCompare.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);
        editSortBy.setSelection(0);
    }

   public class MyAsyncTask extends AsyncTask<String,Void,String>{
       InputStream inputStream = null;
       StringBuilder sb = new StringBuilder();
       String jsonString = "";


       @Override
       protected String doInBackground(String... strings) {
           final String SERVER_URL = "http://ebaysearchserverhw9-env.elasticbeanstalk.com/index.php?";
           final String KEY_WORD_PARAM = "keywords";
           final String MIN_PRICE_PARAM = "pricefrom";
           final String MAX_PRICE_PARAM = "priceto";
           final String SORT_BY_PARAM = "sort";
           final String ITEMSPERPAGE_PARAM = "perPage";
           final String PAGENUM_PARAM = "curPageNum";
           //build url
           StringBuilder sbUrl = new StringBuilder();
           sbUrl.append(SERVER_URL)
             .append(KEY_WORD_PARAM + "=")
             .append(key_word +"&")
             .append(MIN_PRICE_PARAM+"=")
             .append(min_price+"&")
             .append(MAX_PRICE_PARAM+"=")
             .append(max_price+"&")
             .append(SORT_BY_PARAM+"=")
             .append(sort_by+"&")
             .append(ITEMSPERPAGE_PARAM+"=")
             .append("5&")
             .append(PAGENUM_PARAM+"=")
             .append("1");

           //send request to awr and fetch the jSON data
           HttpURLConnection connection = null;
           BufferedReader reader = null;
           try {
               URL url = new URL(sbUrl.toString());
               //Log.i("MainActivity", sbUrl.toString());
               connection = (HttpURLConnection) url.openConnection();
               connection.setDoOutput(true);
               connection.setReadTimeout(10000);
               connection.setConnectTimeout(15000);
               connection.setRequestMethod("GET");
               connection.connect();
               inputStream = connection.getInputStream();
               reader = new BufferedReader(new InputStreamReader(inputStream));
               String line = "";
               while ((line = reader.readLine()) != null) {
                   sb.append(line);
                   sb.append("\n");
               }
               jsonString = sb.toString();
               return jsonString;

           } catch (MalformedURLException e) {
               Log.e("Error", e.getMessage());
               return null;
           } catch (IOException e) {
               Log.e("Error", e.getMessage());
               return null;
           } finally {
               if (connection != null) {
                    connection.disconnect();
               }
               if(reader != null){
                   try{
                       reader.close();
                   }catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
       }

       @Override
       protected void onPostExecute(String jsonString){
           try {
               if(jsonString != null) {
                   JSONObject JSON = new JSONObject(jsonString);
                   String result = JSON.getString("ack");
                   if (!result.equals("Success")) {
                       noResults.setVisibility(View.VISIBLE);
                   } else {
                       //Log.i("MainActivity",jsonString);
                       Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);

                       resultIntent.putExtra(KEYWORD,true_key_word);
                       resultIntent.putExtra(JSONSTRING, jsonString);
                       startActivity(resultIntent);
                   }
               }
           }catch (JSONException e) {
               Log.e("Error", e.getMessage());
           }

       }
   }
}
