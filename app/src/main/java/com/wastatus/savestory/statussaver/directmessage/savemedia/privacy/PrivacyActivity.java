package com.wastatus.savestory.statussaver.directmessage.savemedia.privacy;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityPrivacyBinding;


public class PrivacyActivity extends AppCompatActivity {
    public static String privacyPolicy = "https://sites.google.com/view/centerspotsolutions/home";
    private ActivityPrivacyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        binding.wvPrivacy.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                binding.toolbar.setTitle("Loading...");
                setProgress(progress * 100);

                if(progress == 100){
                    binding.toolbar.setTitle(R.string.app_name);
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
        binding.wvPrivacy.getSettings().setJavaScriptEnabled(true);
        binding.wvPrivacy.loadUrl(privacyPolicy);
    }
}
