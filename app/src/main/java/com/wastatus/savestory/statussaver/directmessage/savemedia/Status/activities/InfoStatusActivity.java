package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityStatusInfoBinding;
import com.google.android.gms.ads.AdView;


public class InfoStatusActivity extends AppCompatActivity {
private ActivityStatusInfoBinding binding;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(getString(R.string.info_status));
        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(v -> InfoStatusActivity.super.onBackPressed());

        if (AdmobAdsManager.isAdmob) {

            adView = AdmobAdsManager.banner(InfoStatusActivity.this, binding.llAds);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null){
            adView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (adView != null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }
}