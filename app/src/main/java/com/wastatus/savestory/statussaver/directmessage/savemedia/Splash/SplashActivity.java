package com.wastatus.savestory.statussaver.directmessage.savemedia.Splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.start.MainActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AdmobAdsManager.isAdmob) {
            AdmobAdsManager.loadInterstitial(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInterstitialAds();
                }
            }, 2500);
        }else {
            gotoHomeScreen();
        }


    }

    private void showInterstitialAds() {
        InterstitialAd interstitial = AdmobAdsManager.getInterstitial();
        if (interstitial != null) {
            interstitial.show(this);
            interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    gotoHomeScreen();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    gotoHomeScreen();
                }
            });
        } else {
            gotoHomeScreen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void gotoHomeScreen() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }
}