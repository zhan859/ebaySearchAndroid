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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.PendingIntent.getActivity;
import static com.facebook.share.Sharer.*;


public class DetailsActivity extends ActionBarActivity {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String url;
    String price;
    String title;
    String address;
    String itemUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_details);
        url = getIntent().getStringExtra(ResultActivity.LARGE_IMAGE_URL);
        title = getIntent().getStringExtra(ResultActivity.ITEM_TITLE);
        price = getIntent().getStringExtra(ResultActivity.ITEM_PRICE);
        address = getIntent().getStringExtra(ResultActivity.SELLER_ADDRESS);
        String topRating = getIntent().getStringExtra(ResultActivity.ITEM_RATING);
        itemUrl = getIntent().getStringExtra(ResultActivity.ITEM_URL);
        String category = getIntent().getStringExtra(ResultActivity.ITEM_CATEGORY);
        String condition = getIntent().getStringExtra(ResultActivity.ITEM_CONDITION);
        String buyingFormat = getIntent().getStringExtra(ResultActivity.ITEM_BUYING_FORMAT);
        String sellerInfo = getIntent().getStringExtra(ResultActivity.ITEM_SELLER_INFO);
        //Log.i("DetailsActivity",sellerInfo);
        String shippingInfo =getIntent().getStringExtra(ResultActivity.ITEM_SHIPPING_INFO);
        //Log.i("DetailsActivity",shippingInfo);
        ImageView imageView = (ImageView)findViewById(R.id.detail_image);
        imageView.setTag(url);
        new DownLoadImageTask().execute(imageView);
        TextView textView = (TextView)findViewById(R.id.detail_title);
        textView.setText(title);
        TextView textViewPrice = (TextView)findViewById(R.id.detail_shippingPrice);
        textViewPrice.setText(price);
        TextView textViewAddress = (TextView)findViewById(R.id.detail_sellerAddress);
        textViewAddress.setText(address);
        ImageView imageViewTopRating = (ImageView)findViewById(R.id.detail_topRatting);
        if(topRating.equals("false")){
            imageViewTopRating.setVisibility(View.GONE);
        }
        ImageView imageViewBuyNow = (ImageView)findViewById(R.id.detail_buyNow);
        imageViewBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebURL(itemUrl);
            }
        });
        if(!category.equals("")){
            TextView textViewCatg = (TextView)findViewById(R.id.detail_etCategoryName);
            textViewCatg.setText(category);
        }
        if(!condition.equals("")){
            TextView textViewCond = (TextView)findViewById(R.id.detail_etCondition);
            textViewCond.setText(condition);
        }
        if(!buyingFormat.equals("")){
            TextView textViewbF = (TextView)findViewById(R.id.detail_etBuyingFormat);
            textViewbF.setText(buyingFormat);
        }
        try {
            JSONObject sellerInfoJson = new JSONObject(sellerInfo);
            parseSellerJson(sellerInfoJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject shippingInfoJson = new JSONObject(shippingInfo);
            parseShippingJson(shippingInfoJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Button btnBI= (Button)findViewById(R.id.detail_basicInfoBtn);
        final Button btnS= (Button)findViewById(R.id.detail_sellerBtn);
        final Button btnSP= (Button)findViewById(R.id.detail_shippingBtn);
        final RelativeLayout relativeLayoutBI =(RelativeLayout)findViewById(R.id.detail_basicInfo);
        final RelativeLayout relativeLayoutS =(RelativeLayout)findViewById(R.id.detail_sellerInfo);
        final RelativeLayout relativeLayoutSP =(RelativeLayout)findViewById(R.id.detail_shippingInfo);
        btnBI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnS.setBackgroundResource(R.drawable.btn_greybackground);
                relativeLayoutS.setVisibility(View.GONE);
                btnSP.setBackgroundResource(R.drawable.btn_greybackground);
                relativeLayoutSP.setVisibility(View.GONE);
                btnBI.setBackgroundResource(R.drawable.btn_bluebackground);
                relativeLayoutBI.setVisibility(View.VISIBLE);
            }
        });
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               btnBI.setBackgroundResource(R.drawable.btn_greybackground);
               relativeLayoutBI.setVisibility(View.GONE);
               btnSP.setBackgroundResource(R.drawable.btn_greybackground);
               relativeLayoutSP.setVisibility(View.GONE);
               btnS.setBackgroundResource(R.drawable.btn_bluebackground);
               relativeLayoutS.setVisibility(View.VISIBLE);
            }
        });
        btnSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBI.setBackgroundResource(R.drawable.btn_greybackground);
                relativeLayoutBI.setVisibility(View.GONE);
                btnS.setBackgroundResource(R.drawable.btn_greybackground);
                relativeLayoutS.setVisibility(View.GONE);
                btnSP.setBackgroundResource(R.drawable.btn_bluebackground);
                relativeLayoutSP.setVisibility(View.VISIBLE);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>(){
            @Override
            public void onSuccess(Result result) {
                String ID = result.getPostId();
                if(ID != null) {
                    Toast.makeText(DetailsActivity.this, "Posted Story, ID: " + ID, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "Post Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancel() {
                Toast.makeText(DetailsActivity.this, "Post Cancelled", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error){

            }
        });
        Button fbBtn =(Button)findViewById(R.id.detail_connectFacebook);
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareFaceBook();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

    private class DownLoadImageTask extends AsyncTask<ImageView, Void, Bitmap> {
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
    private void parseSellerJson(JSONObject jsonObject){
        try {
            String userName= jsonObject.getString("sellerUserName");
            if(!userName.equals("")){
                TextView textViewUserN = (TextView)findViewById(R.id.detail_etUserName);
                textViewUserN.setText(userName);
            }
            String feedbackScore = jsonObject.getString("feedbackScore");
            if(!feedbackScore.equals("")){
                TextView textViewFBS = (TextView)findViewById(R.id.detail_etFeedBack);
                textViewFBS.setText(feedbackScore);
            }
            String positiveFeedback = jsonObject.getString("positiveFeedbackPercent");
            if(!positiveFeedback.equals("")){
                TextView textViewPFB = (TextView)findViewById(R.id.detail_etPositiveFeedBack);
                textViewPFB.setText(positiveFeedback);
            }
            String feedbackRating = jsonObject.getString("feedbackRatingStar");
            if(!feedbackRating.equals("")){
                TextView textViewFBR= (TextView)findViewById(R.id.detail_etFeedBackRating);
                textViewFBR.setText(feedbackRating);
            }
            String topRated = jsonObject.getString("topRatedSeller");
            if(!topRated.equals("false")){
                ImageView imageViewTR = (ImageView)findViewById(R.id.detail_etTopRate);
                imageViewTR.setImageResource(R.drawable.checkyes);
            }
            String store = jsonObject.getString("sellerStoreName");
            if(!store.equals("")){
                TextView textViewS = (TextView)findViewById(R.id.detail_etStore);
                textViewS.setText(store);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void parseShippingJson(JSONObject jsonObject){
        try {
            String shippingType = jsonObject.getString("shippingType");
            //String TestShippingType = "FlatDemoBlaBla";
            shippingType = shippingType.replaceAll("(.)([A-Z])", "$1, $2");
            //Log.i("DetailsActivity", "Test Result is:" + TestShippingType);
            if(!shippingType.equals("")){
                TextView textViewST = (TextView)findViewById(R.id.detail_etShippingType);
                textViewST.setText(shippingType);
            }
            String shippingLocations = jsonObject.getString("shipToLocations");
            if(!shippingLocations.equals("")){
                TextView textViewSL = (TextView)findViewById(R.id.detail_etShippingLocation);
                textViewSL.setText(shippingLocations);
            }
            String handlingTime = jsonObject.getString("handlingTime");
            if(!handlingTime.equals("")){
                TextView textViewHT = (TextView)findViewById(R.id.detail_etHandingTime);
                textViewHT.setText(handlingTime);
            }
            String expeditedShipping = jsonObject.getString("expeditedShipping");
            if(!expeditedShipping.equals("false")){
                ImageView imageViewEX = (ImageView)findViewById(R.id.detail_etExpShipping);
                imageViewEX.setImageResource(R.drawable.checkyes);
            }
            String oneDayShipping = jsonObject.getString("oneDayShippingAvailable");
            if(!oneDayShipping.equals("false")){
                ImageView imageViewOD = (ImageView)findViewById(R.id.detail_etOneDayShipping);
                imageViewOD.setImageResource(R.drawable.checkyes);
            }
            String returnsAccepted = jsonObject.getString("returnsAccepted");
            if(!returnsAccepted.equals("false")){
                ImageView imageViewRA = (ImageView)findViewById(R.id.detail_etReturn);
                imageViewRA.setImageResource(R.drawable.checkyes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void ShareFaceBook(){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(itemUrl))
                    .setContentTitle(title)
                    .setImageUrl(Uri.parse(url))
                    .setContentDescription(price + ", Location: " + address)
                    .build();
            shareDialog.show(content);
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
