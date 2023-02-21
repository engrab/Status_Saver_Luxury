package com.wastatus.savestory.statussaver.directmessage.savemedia.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;

import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityWebMainBinding;

import java.util.Objects;

public class ScanWhatsappActivity extends AppCompatActivity {
    private ActivityWebMainBinding binding;
    public static Handler handler;
    private static ValueCallback<Uri[]> mUploadMessageArr;
    String TAG = ScanWhatsappActivity.class.getSimpleName();


    @SuppressLint("HandlerLeak")
    private static class btnInitHandlerListener extends Handler {
        @SuppressLint({"SetTextI18n"})
        public void handleMessage(Message msg) {
        }
    }

    private static class webChromeClients extends WebChromeClient {
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.e("CustomClient", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }


    private class MyBrowser extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            binding.progressBar.setVisibility(View.VISIBLE);
            Log.e(TAG, "binding.progressBar");
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "binding.progressBar GONE");
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    //Initialisation Method
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        InitHandler();

        checkPermission();
        binding.webScan.clearFormData();
        binding.webScan.getSettings().setSaveFormData(true);
        binding.webScan.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
        binding.webScan.setLayoutParams(new LayoutParams(-1, -1));
        binding.webScan.setWebChromeClient(new webChromeClients());
        binding.webScan.setWebViewClient(new MyBrowser());
        binding.webScan.getSettings().setAllowFileAccess(true);
        binding.webScan.getSettings().setJavaScriptEnabled(true);
        binding.webScan.getSettings().setDefaultTextEncodingName( "UTF-8");
        binding.webScan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.webScan.getSettings().setDatabaseEnabled(true);
        binding.webScan.getSettings().setBuiltInZoomControls(false);
        binding.webScan.getSettings().setSupportZoom(false);
        binding.webScan.getSettings().setUseWideViewPort(true);
        binding.webScan.getSettings().setDomStorageEnabled(true);
        binding.webScan.getSettings().setAllowFileAccess(true);
        binding.webScan.getSettings().setLoadWithOverviewMode(true);
        binding.webScan.getSettings().setLoadsImagesAutomatically(true);
        binding.webScan.getSettings().setBlockNetworkImage(false);
        binding.webScan.getSettings().setBlockNetworkLoads(false);
        binding.webScan.getSettings().setLoadWithOverviewMode(true);
        binding.webScan.loadUrl("https://web.whatsapp.com/%F0%9F%8C%90/en");
    }

    public void checkPermission() {
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION"}, 123);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001) {
            mUploadMessageArr.onReceiveValue(FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = true;
        if (keyCode == 4) {
            try {
                if (binding.webScan.canGoBack()) {
                    binding.webScan.goBack();
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
        z = super.onKeyDown(keyCode, event);
        return z;
    }

    protected void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        binding.webScan.clearCache(true);
    }

    public void onDestroy() {
        super.onDestroy();
        binding.webScan.clearCache(true);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        binding.webScan.clearCache(true);
        super.onStop();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint({"HandlerLeak"})
    private void InitHandler() {
        handler = new btnInitHandlerListener();
    }



}
