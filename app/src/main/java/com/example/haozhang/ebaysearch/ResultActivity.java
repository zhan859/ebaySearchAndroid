package com.example.haozhang.ebaysearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ResultActivity extends ActionBarActivity {
    public static final String LARGE_IMAGE_URL = "largeImageUrl";
    public static final String ITEM_TITLE = "itemTitle";
    public static final String ITEM_PRICE = "item_price";
    public static final String SELLER_ADDRESS = "seller_address";
    public static final String ITEM_RATING = "item_rating";
    public static final String ITEM_URL = "item_url";
    public static final String ITEM_CATEGORY = "item_category";
    public static final String ITEM_CONDITION = "item_condition";
    public static final String ITEM_BUYING_FORMAT = "item_buyingFormat";
    public static final String ITEM_SELLER_INFO = "item_sellerInfo";
    public static final String ITEM_SHIPPING_INFO = "item_shippingInfo";
    TableRow line1;
    TableRow row1;
    TableRow line2;
    TableRow row2;
    TableRow line3;
    TableRow row3;
    TableRow line4;
    TableRow row4;
    TableRow line5;
    TableRow row5;
    JSONObject subObj1,subObj2,subObj3,subObj4,subObj5;
    JSONObject sellerInfoObj1,sellerInfoObj2,sellerInfoObj3,sellerInfoObj4,sellerInfoObj5;
    JSONObject shippingInfoObj1,shippingInfoObj2,shippingInfoObj3,shippingInfoObj4,shippingInfoObj5;
    JSONObject basicInfoObj1,basicInfoObj2,basicInfoObj3,basicInfoObj4,basicInfoObj5;
    String sellerInfo1,sellerInfo2,sellerInfo3,sellerInfo4,sellerInfo5;
    String shippingInfo1,shippingInfo2,shippingInfo3,shippingInfo4,shippingInfo5;
    String title1,title2,title3,title4,title5;
    String url1,url2,url3,url4,url5;
    String imageUrl1,imageUrl2,imageUrl3,imageUrl4,imageUrl5;
    String lImageUrl1,lImageUrl2,lImageUrl3,lImageUrl4,lImageUrl5;
    String VImageUrl1,VImageUrl2,VImageUrl3,VImageUrl4,VImageUrl5;
    String location1,location2,location3,location4,location5;
    String topListing1,topListing2,topListing3,topListing4,topListing5;
    String catgName1,catgName2,catgName3,catgName4,catgName5;
    String condition1,condition2,condition3,condition4,condition5;
    String buyingFormat1,buyingFormat2,buyingFormat3,buyingFormat4,buyingFormat5;
    String price1,price2,price3,price4,price5;
    String cost1,cost2,cost3,cost4,cost5;
    String priceInfo1,priceInfo2,priceInfo3,priceInfo4,priceInfo5;
    ImageView itemImage1,itemImage2,itemImage3,itemImage4,itemImage5;
    TextView btn1,btn2,btn3,btn4,btn5;
    TextView priceI1,priceI2,priceI3,priceI4,priceI5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String keywords = getIntent().getStringExtra(MainActivity.KEYWORD);
        TextView toKeywords = (TextView) findViewById(R.id.headline);
        toKeywords.setText("Results for '"+ keywords +"'");
        String result = getIntent().getStringExtra(MainActivity.JSONSTRING);
        //Log.i("ResultActivity", result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            parseJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
    private void parseJson(JSONObject jsonObject){
        try {
            int counter = Integer.valueOf(jsonObject.getString("resultCount"));
            if(1 <= counter){
                subObj1 = jsonObject.getJSONObject("item0");
                basicInfoObj1 = subObj1.getJSONObject("basicInfo");
                sellerInfoObj1 = subObj1.getJSONObject("sellerInfo");
                shippingInfoObj1 = subObj1.getJSONObject("shippingInfo");
                sellerInfo1 = sellerInfoObj1.toString();
                shippingInfo1 = shippingInfoObj1.toString();
                title1 = basicInfoObj1.getString("title");
                url1 = basicInfoObj1.getString("viewItemURL");
                imageUrl1 = basicInfoObj1.getString("gallerURL");
                lImageUrl1 = basicInfoObj1.getString("pictureURLSuperSize");
                topListing1 = basicInfoObj1.getString("topRatedListing");
                catgName1 = basicInfoObj1.getString("categoryName");
                condition1 = basicInfoObj1.getString("conditionDisplayName");
                buyingFormat1 = basicInfoObj1.getString("listingType");
                //Log.i("ResultActivity",lImageUrl1);
                location1= basicInfoObj1.getString("location");
                itemImage1 = (ImageView)findViewById(R.id.image1);
                if(imageUrl1.equals("")&&lImageUrl1.equals("")){
                    itemImage1.setImageResource(R.drawable.noimage);
                }else {
                    if (lImageUrl1.equals("")) {
                        VImageUrl1 = imageUrl1;
                    } else {
                        VImageUrl1 = lImageUrl1;
                    }
                    itemImage1.setTag(VImageUrl1);
                    new DownLoadImageTask().execute(itemImage1);
                }
                itemImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openWebURL(url1);
                    }
                });
                price1 = basicInfoObj1.getString("convertedCurrentPrice");
                cost1 = basicInfoObj1.getString("shippingServiceCost");
                if(cost1.equals("0")  || cost1.equals("0.0")||cost1.equals("")){
                    priceInfo1 = "Price: $" + price1 + " (Free Shipping)";
                }else{
                    priceInfo1 = "Price: $" + price1 + " (+ $" + cost1 + " Shipping)";
                }
                priceI1 = (TextView)findViewById(R.id.rtext1);
                priceI1.setText(priceInfo1);
                btn1 = (TextView)findViewById(R.id.rbutton1);
                btn1.setText(title1);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detailsIntent = new Intent(ResultActivity.this, DetailsActivity.class);
                        if(lImageUrl1.equals("")){
                            detailsIntent.putExtra(LARGE_IMAGE_URL,imageUrl1);
                        }else{
                            detailsIntent.putExtra(LARGE_IMAGE_URL,lImageUrl1);
                        }
                        detailsIntent.putExtra(ITEM_TITLE, title1);
                        detailsIntent.putExtra(ITEM_PRICE,priceInfo1);
                        detailsIntent.putExtra(SELLER_ADDRESS,location1);
                        detailsIntent.putExtra(ITEM_RATING,topListing1);
                        detailsIntent.putExtra(ITEM_URL,url1);
                        detailsIntent.putExtra(ITEM_CATEGORY,catgName1);
                        detailsIntent.putExtra(ITEM_CONDITION,condition1);
                        detailsIntent.putExtra(ITEM_BUYING_FORMAT,buyingFormat1);
                        detailsIntent.putExtra(ITEM_SELLER_INFO,sellerInfo1);
                        detailsIntent.putExtra(ITEM_SHIPPING_INFO,shippingInfo1);
                        startActivity(detailsIntent);
                    }
                });
            }else{
                line1 = (TableRow)findViewById(R.id.tableRow1);
                row1 = (TableRow)findViewById(R.id.tableRow2);
                line1.setVisibility(View.GONE);
                row1.setVisibility(View.GONE);
            }

            if(2 <= counter){
                subObj2 = jsonObject.getJSONObject("item1");
                basicInfoObj2 = subObj2.getJSONObject("basicInfo");
                sellerInfoObj2 = subObj2.getJSONObject("sellerInfo");
                shippingInfoObj2 = subObj2.getJSONObject("shippingInfo");
                sellerInfo2 = sellerInfoObj2.toString();
                shippingInfo2 = shippingInfoObj2.toString();
                title2 = basicInfoObj2.getString("title");
                url2 = basicInfoObj2.getString("viewItemURL");
                imageUrl2 = basicInfoObj2.getString("gallerURL");
                lImageUrl2 = basicInfoObj2.getString("pictureURLSuperSize");
                topListing2 = basicInfoObj2.getString("topRatedListing");
                catgName2 = basicInfoObj2.getString("categoryName");
                condition2 = basicInfoObj2.getString("conditionDisplayName");
                buyingFormat2 = basicInfoObj2.getString("listingType");
                //Log.i("ResultActivity",lImageUrl2);
                location2 = basicInfoObj2.getString("location");
                itemImage2 = (ImageView)findViewById(R.id.image2);
                if(imageUrl2.equals("") && lImageUrl2.equals("")){
                    itemImage2.setImageResource(R.drawable.noimage);
                }else {
                    if (lImageUrl2.equals("")) {
                        VImageUrl2 = imageUrl2;
                    } else {
                        VImageUrl2 = lImageUrl2;
                    }
                    itemImage2.setTag(VImageUrl2);
                    new DownLoadImageTask().execute(itemImage2);
                }
                itemImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openWebURL(url2);
                    }
                });
                price2 = basicInfoObj2.getString("convertedCurrentPrice");
                cost2 = basicInfoObj2.getString("shippingServiceCost");
                if(cost2.equals("0")  || cost2.equals("0.0")||cost2.equals("")){
                    priceInfo2 = "Price: $" + price2+ " (Free Shipping)";
                }else{
                    priceInfo2 = "Price: $" + price2 + " (+ $" + cost2 + " Shipping)";
                }
                priceI2 = (TextView)findViewById(R.id.rtext2);
                priceI2.setText(priceInfo2);
                btn2 = (TextView)findViewById(R.id.rbutton2);
                btn2.setText(title2);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detailsIntent = new Intent(ResultActivity.this, DetailsActivity.class);
                        if(lImageUrl2.equals("")){
                            detailsIntent.putExtra(LARGE_IMAGE_URL,imageUrl2);
                        }else{
                            detailsIntent.putExtra(LARGE_IMAGE_URL,lImageUrl2);
                        }
                        detailsIntent.putExtra(ITEM_TITLE, title2);
                        detailsIntent.putExtra(ITEM_PRICE,priceInfo2);
                        detailsIntent.putExtra(SELLER_ADDRESS,location2);
                        detailsIntent.putExtra(ITEM_RATING,topListing2);
                        detailsIntent.putExtra(ITEM_URL,url2);
                        detailsIntent.putExtra(ITEM_CATEGORY,catgName2);
                        detailsIntent.putExtra(ITEM_CONDITION,condition2);
                        detailsIntent.putExtra(ITEM_BUYING_FORMAT,buyingFormat2);
                        detailsIntent.putExtra(ITEM_SELLER_INFO,sellerInfo2);
                        detailsIntent.putExtra(ITEM_SHIPPING_INFO,shippingInfo2);
                        startActivity(detailsIntent);
                    }
                });
            }else{
                line2 = (TableRow)findViewById(R.id.tableRow3);
                row2 = (TableRow)findViewById(R.id.tableRow4);
                line2.setVisibility(View.GONE);
                row2.setVisibility(View.GONE);
            }


            if(3 <= counter){
                subObj3 = jsonObject.getJSONObject("item2");
                basicInfoObj3 = subObj3.getJSONObject("basicInfo");
                sellerInfoObj3 = subObj3.getJSONObject("sellerInfo");
                shippingInfoObj3 = subObj3.getJSONObject("shippingInfo");
                sellerInfo3 = sellerInfoObj3.toString();
                shippingInfo3 = shippingInfoObj3.toString();
                title3 = basicInfoObj3.getString("title");
                url3 = basicInfoObj3.getString("viewItemURL");
                imageUrl3 = basicInfoObj3.getString("gallerURL");
                lImageUrl3 = basicInfoObj3.getString("pictureURLSuperSize");
                topListing3 = basicInfoObj3.getString("topRatedListing");
                catgName3 = basicInfoObj3.getString("categoryName");
                condition3 = basicInfoObj3.getString("conditionDisplayName");
                buyingFormat3 = basicInfoObj3.getString("listingType");
                //Log.i("ResultActivity",lImageUrl3);
                location3 = basicInfoObj3.getString("location");
                itemImage3 = (ImageView)findViewById(R.id.image3);
                if(imageUrl3.equals("")&&lImageUrl3.equals("")){
                    itemImage3.setImageResource(R.drawable.noimage);
                }else {
                    if (lImageUrl3.equals("")) {
                        VImageUrl3 = imageUrl3;
                    } else {
                        VImageUrl3 = lImageUrl3;
                    }
                    itemImage3.setTag(VImageUrl3);
                    new DownLoadImageTask().execute(itemImage3);
                }
                itemImage3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openWebURL(url3);
                    }
                });
                price3 = basicInfoObj3.getString("convertedCurrentPrice");
                cost3 = basicInfoObj3.getString("shippingServiceCost");
                if(cost3.equals("0")  || cost3.equals("0.0")||cost3.equals("")){
                    priceInfo3 = "Price: $" + price3 + " (Free Shipping)";
                }else{
                    priceInfo3 = "Price: $" + price3 + " (+ $" + cost3 + " Shipping)";
                }
                priceI3 = (TextView)findViewById(R.id.rtext3);
                priceI3.setText(priceInfo3);
                btn3 = (TextView)findViewById(R.id.rbutton3);
                btn3.setText(title3);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detailsIntent = new Intent(ResultActivity.this, DetailsActivity.class);
                        if(lImageUrl3.equals("")){
                            detailsIntent.putExtra(LARGE_IMAGE_URL,imageUrl3);
                        }else{
                            detailsIntent.putExtra(LARGE_IMAGE_URL,lImageUrl3);
                        }
                        detailsIntent.putExtra(ITEM_TITLE, title3);
                        detailsIntent.putExtra(ITEM_PRICE,priceInfo3);
                        detailsIntent.putExtra(SELLER_ADDRESS,location3);
                        detailsIntent.putExtra(ITEM_RATING,topListing3);
                        detailsIntent.putExtra(ITEM_URL,url3);
                        detailsIntent.putExtra(ITEM_CATEGORY,catgName3);
                        detailsIntent.putExtra(ITEM_CONDITION,condition3);
                        detailsIntent.putExtra(ITEM_BUYING_FORMAT,buyingFormat3);
                        detailsIntent.putExtra(ITEM_SELLER_INFO,sellerInfo3);
                        detailsIntent.putExtra(ITEM_SHIPPING_INFO,shippingInfo3);
                        startActivity(detailsIntent);
                    }
                });
            }else{
                line3 = (TableRow)findViewById(R.id.tableRow5);
                row3 = (TableRow)findViewById(R.id.tableRow6);
                line3.setVisibility(View.GONE);
                row3.setVisibility(View.GONE);
            }


            if(4 <= counter){
                subObj4 = jsonObject.getJSONObject("item3");
                basicInfoObj4 = subObj4.getJSONObject("basicInfo");
                sellerInfoObj4 = subObj4.getJSONObject("sellerInfo");
                shippingInfoObj4 = subObj4.getJSONObject("shippingInfo");
                sellerInfo4 = sellerInfoObj4.toString();
                shippingInfo4 = shippingInfoObj4.toString();
                title4 = basicInfoObj4.getString("title");
                url4 = basicInfoObj4.getString("viewItemURL");
                imageUrl4 = basicInfoObj4.getString("gallerURL");
                lImageUrl4 = basicInfoObj4.getString("pictureURLSuperSize");
                topListing4 = basicInfoObj4.getString("topRatedListing");
                catgName4 = basicInfoObj4.getString("categoryName");
                condition4 = basicInfoObj4.getString("conditionDisplayName");
                buyingFormat4 = basicInfoObj4.getString("listingType");
                //Log.i("ResultActivity",lImageUrl4);
                location4 = basicInfoObj4.getString("location");
                itemImage4 = (ImageView)findViewById(R.id.image4);
                if(imageUrl4.equals("")&&lImageUrl4.equals("")){
                    itemImage4.setImageResource(R.drawable.noimage);
                }else {
                    if (lImageUrl4.equals("")) {
                        VImageUrl4 = imageUrl4;
                    } else {
                        VImageUrl4 = lImageUrl4;
                    }
                    itemImage4.setTag(VImageUrl4);
                    new DownLoadImageTask().execute(itemImage4);
                }
                itemImage4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openWebURL(url4);
                    }
                });
                price4 = basicInfoObj4.getString("convertedCurrentPrice");
                cost4 = basicInfoObj4.getString("shippingServiceCost");
                if(cost4.equals("0")  || cost4.equals("0.0")||cost4.equals("")){
                    priceInfo4 = "Price: $" + price4 + " (Free Shipping)";
                }else{
                    priceInfo4 = "Price: $" + price4 + " (+ $" + cost4 + " Shipping)";
                }
                priceI4 = (TextView)findViewById(R.id.rtext4);
                priceI4.setText(priceInfo4);
                btn4 = (TextView)findViewById(R.id.rbutton4);
                btn4.setText(title4);
                btn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detailsIntent = new Intent(ResultActivity.this, DetailsActivity.class);
                        if(lImageUrl4.equals("")){
                            detailsIntent.putExtra(LARGE_IMAGE_URL,imageUrl4);
                        }else{
                            detailsIntent.putExtra(LARGE_IMAGE_URL,lImageUrl4);
                        }
                        detailsIntent.putExtra(ITEM_TITLE, title4);
                        detailsIntent.putExtra(ITEM_PRICE,priceInfo4);
                        detailsIntent.putExtra(SELLER_ADDRESS,location4);
                        detailsIntent.putExtra(ITEM_RATING,topListing4);
                        detailsIntent.putExtra(ITEM_URL,url4);
                        detailsIntent.putExtra(ITEM_CATEGORY,catgName4);
                        detailsIntent.putExtra(ITEM_CONDITION,condition4);
                        detailsIntent.putExtra(ITEM_BUYING_FORMAT,buyingFormat4);
                        detailsIntent.putExtra(ITEM_SELLER_INFO,sellerInfo4);
                        detailsIntent.putExtra(ITEM_SHIPPING_INFO,shippingInfo4);
                        startActivity(detailsIntent);
                    }
                });
            }else{
                line4 = (TableRow)findViewById(R.id.tableRow7);
                row4 = (TableRow)findViewById(R.id.tableRow8);
                line4.setVisibility(View.GONE);
                row4.setVisibility(View.GONE);
            }


            if(5 <= counter){
                subObj5 = jsonObject.getJSONObject("item4");
                basicInfoObj5 = subObj5.getJSONObject("basicInfo");
                sellerInfoObj5 = subObj5.getJSONObject("sellerInfo");
                shippingInfoObj5 = subObj5.getJSONObject("shippingInfo");
                sellerInfo5 = sellerInfoObj5.toString();
                shippingInfo5 = shippingInfoObj5.toString();
                title5 = basicInfoObj5.getString("title");
                url5 = basicInfoObj5.getString("viewItemURL");
                imageUrl5 = basicInfoObj5.getString("gallerURL");
                lImageUrl5 = basicInfoObj5.getString("pictureURLSuperSize");
                topListing5 = basicInfoObj5.getString("topRatedListing");
                catgName5 = basicInfoObj5.getString("categoryName");
                condition5 = basicInfoObj5.getString("conditionDisplayName");
                buyingFormat5 = basicInfoObj5.getString("listingType");
                //Log.i("ResultActivity",lImageUrl5);
                location5 = basicInfoObj5.getString("location");
                itemImage5 = (ImageView)findViewById(R.id.image5);

                if(imageUrl5.equals("") && lImageUrl5.equals("")){
                    itemImage5.setImageResource(R.drawable.noimage);
                }else {
                    if (lImageUrl5.equals("")) {
                        VImageUrl5 = imageUrl5;
                    } else {
                        VImageUrl5 = lImageUrl5;
                    }
                    itemImage5.setTag(VImageUrl5);
                    new DownLoadImageTask().execute(itemImage5);
                }
                itemImage5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openWebURL(url5);
                    }
                });
                price5 = basicInfoObj5.getString("convertedCurrentPrice");
                cost5 = basicInfoObj5.getString("shippingServiceCost");
                if(cost5.equals("0")  || cost5.equals("0.0")||cost5.equals("")){
                    priceInfo5 = "Price: $" + price5 + " (Free Shipping)";
                }else{
                    priceInfo5 = "Price: $" + price5 + " (+ $" + cost5 + " Shipping)";
                }
                priceI5 = (TextView)findViewById(R.id.rtext5);
                priceI5.setText(priceInfo5);
                btn5 = (TextView)findViewById(R.id.rbutton5);
                btn5.setText(title5);
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detailsIntent = new Intent(ResultActivity.this, DetailsActivity.class);
                        if(lImageUrl5.equals("")){
                            detailsIntent.putExtra(LARGE_IMAGE_URL,imageUrl5);
                        }else{
                            detailsIntent.putExtra(LARGE_IMAGE_URL,lImageUrl5);
                        }
                        detailsIntent.putExtra(ITEM_TITLE, title5);
                        detailsIntent.putExtra(ITEM_PRICE,priceInfo5);
                        detailsIntent.putExtra(SELLER_ADDRESS,location5);
                        detailsIntent.putExtra(ITEM_RATING,topListing5);
                        detailsIntent.putExtra(ITEM_URL,url5);
                        detailsIntent.putExtra(ITEM_CATEGORY,catgName5);
                        detailsIntent.putExtra(ITEM_CONDITION,condition5);
                        detailsIntent.putExtra(ITEM_BUYING_FORMAT,buyingFormat5);
                        detailsIntent.putExtra(ITEM_SELLER_INFO,sellerInfo5);
                        detailsIntent.putExtra(ITEM_SHIPPING_INFO,shippingInfo5);
                        startActivity(detailsIntent);
                    }
                });
            }else{
                line5 = (TableRow)findViewById(R.id.tableRow9);
                row5 = (TableRow)findViewById(R.id.tableRow10);
                line5.setVisibility(View.GONE);
                row5.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class DownLoadImageTask extends AsyncTask<ImageView, Void, Bitmap>{
        ImageView imageView = null;
        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image((String)imageView.getTag());
        }
        @Override
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

    private Bitmap download_Image(String url){
        Bitmap bitmap = null;
        try{
            URL urln = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)urln.openConnection();
            InputStream in = httpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            if(bitmap != null){
                return bitmap;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void openWebURL(String url){
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browser);
    }
}
