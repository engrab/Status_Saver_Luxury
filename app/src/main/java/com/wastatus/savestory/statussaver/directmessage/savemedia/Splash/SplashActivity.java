package com.wastatus.savestory.statussaver.directmessage.savemedia.Splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wastatus.savestory.statussaver.directmessage.savemedia.Util;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.start.MainActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class SplashActivity extends AppCompatActivity {

    InterstitialAd interstitial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Util.isNetworkAvailable(SplashActivity.this)) {
            if (AdmobAdsManager.isAdmob) {

                AdmobAdsManager.loadInterstitial(this);
            }
            else {
                gotoHomeScreen();
            }
        }
        else {
            gotoHomeScreen();
        }


    }

    private void showInterstitialAds() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(() -> {

            interstitial = AdmobAdsManager.getInterstitial();
            if (interstitial != null){

                showInterstitialAds();

            }else {
                gotoHomeScreen();
            }
        }, 2500);



    }

    public void gotoHomeScreen() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }
}