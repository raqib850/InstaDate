package com.example.raqib.instadate;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class WebViewActivity extends AppCompatActivity {
    static  String link;
    WebView myWebView;
    ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;
    private String android_id;
    RelativeLayout webViewLayout;
    boolean addShownIsTrue = true;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        link = getIntent().getExtras().getString("WebPage Link");

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("Dev Id", android_id);

        webViewLayout = (RelativeLayout) findViewById(R.id.webViewLayout);



        //ADS DISPLAY ON APP'S WEB ACTIVITY
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        //DISPLAY AN INTERSTITIAL(FULL SCREEN) AD IF ALREADY LOADED

//        showAd();
        webViewLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                int dy = i3 - i1;
                if(dy > 20){

                    if (mInterstitialAd.isLoaded() && addShownIsTrue) {
                        mInterstitialAd.show();
                        addShownIsTrue = false;
                    }
                }

            }
        });


//        getSupportActionBar().setElevation(0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarWebView);
        String linkToDisplayOnToolbar = link.substring(11);
        toolbar.setTitle(linkToDisplayOnToolbar);
        toolbar.collapseActionView();
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }




//        Log.e("LINK is ", link);
        myWebView = (WebView) findViewById(R.id.webview);
        try{
            if (myWebView != null) {
                myWebView.loadUrl(link);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                builder.setMessage("There Is A Problem Opening The WebPage");
                builder.setCancelable(true);

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } catch(Exception e){
            Log.e("Exception WebPage", String.valueOf(e));
        }
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setKeepScreenOn(true);
        myWebView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.e("DragEvent", String.valueOf(event));
                return false;
            }
        });



//        myWebView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                progressBar.setProgress(progress);
//                if (progress == 100) {
//                    progressBar.setVisibility(View.GONE);
//
//                } else {
//                    progressBar.setVisibility(View.VISIBLE);
//
//                }
//            }
//        });

    }

    private void requestNewInterstitial() {

        AdRequest adRequestInterstitial = new AdRequest.Builder()
                .addTestDevice(android_id)
                .build();

        mInterstitialAd.loadAd(adRequestInterstitial);


    }

    private void showAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // CHECK IF THE KEY EVENT WAS THE BACK BUTTON AND IF THERE'S HISTORY
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_FORWARD) && myWebView.canGoForward()) {
            myWebView.goForward();
            return true;
        }
        // IF IT WASN'T THE BACK KEY OR THERE'S NO WEB PAGE HISTORY, BUBBLE UP TO THE DEFAULT
        // SYSTEM BEHAVIOR (PROBABLY EXIT THE ACTIVITY)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_activity, menu);
        return true;
    }

    public void openInBrowser(MenuItem item) {
        Uri webPage = Uri.parse(link);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webPage);

        //TO GET A LIST OF APPS THAT CAN HANDLE THE PARTICULAR INTENT
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if(isIntentSafe) {
            startActivity(webIntent);
        }

    }

    public void shareLinkOutside(MenuItem item) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");

        //TO GET A LIST OF APPS THAT CAN HANDLE THE PARTICULAR INTENT
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(sendIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if(isIntentSafe) {
            startActivity(sendIntent);
        }
    }

    public void copyToClipboard(MenuItem item) {
        getBaseContext();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Link", link);
        clipboard.setPrimaryClip(clip);
        Toast toast = Toast.makeText(WebViewActivity.this, "Link Copied!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}