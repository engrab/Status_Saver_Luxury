package com.wastatus.savestory.statussaver.directmessage.savemedia.ads;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdmobAdsManager {
    public static int counter = 1;
    public static boolean isAdmob = true;
    public static InterstitialAd mInterstitialAd;
    public static InterstitialAd getInterstitial() {
        return mInterstitialAd;
    }
    public static void loadInterstitial(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, context.getResources().getString(R.string.app_inters), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                mInterstitialAd = null;
            }
        });
    }


    public static AdView banner(Context context, LinearLayout linearLayout) {
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(context.getResources().getString(R.string.app_banner));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        return adView;
    }

    public static void showInterAd(final Activity context, final Intent intent) {
        if (counter == 4 && mInterstitialAd != null) {
            counter = 1;
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
//                    Log.d("TAG", "The ad was dismissed.");
                    loadInterstitial(context);
                    startActivity(context, intent);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
//                     Called when fullscreen content failed to show.
//                    Log.d("TAG", "The ad failed to show.");
                }


                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    mInterstitialAd = null;
//                    Log.d("TAG", "The ad was shown.");
                }
            });
            mInterstitialAd.show(context);
        } else {
            if (counter == 4){
                counter = 1;
            }
            if (mInterstitialAd == null){
                loadInterstitial(context);
            }
            startActivity(context, intent);
        }
    }


    static void startActivity(Context context, Intent intent) {
        if (intent != null) {
            context.startActivity(intent);
        }
    }


}
