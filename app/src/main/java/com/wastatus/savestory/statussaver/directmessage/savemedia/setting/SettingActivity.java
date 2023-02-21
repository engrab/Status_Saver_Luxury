package com.wastatus.savestory.statussaver.directmessage.savemedia.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.activities.InfoStatusActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivitySettingBinding;
import com.wastatus.savestory.statussaver.directmessage.savemedia.privacy.PrivacyActivity;
import com.google.android.gms.ads.AdView;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (AdmobAdsManager.isAdmob){

            adView = AdmobAdsManager.banner(this, binding.llAds);
        }

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        String path = Utils.downloadWhatsAppDir.toString();
        binding.tvSaveFolder.setText(path);

        getVersionName();

        binding.llInfo.setOnClickListener(v -> {
            goToStatus();
        });

        binding.llShare.setOnClickListener(v -> {
           shareApp();
        });

        binding.llRate.setOnClickListener(v -> {
            rateApp();
        });
        binding.llPrivacy.setOnClickListener(v -> {
            goToPrivacy();


        });


    }
    private void shareApp(){
        try {
            String text = "Download & Share with your friends\n https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
            txtIntent.setType("text/plain");
            txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Whatsapp Status Saver");
            txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(txtIntent, "" + getString(R.string.app_name)));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "can not share text", Toast.LENGTH_SHORT).show();
        }
    }

    private void rateApp() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void goToPrivacy(){
        Intent intent = new Intent(SettingActivity.this, PrivacyActivity.class);

        if (AdmobAdsManager.isAdmob) {
            AdmobAdsManager.counter++;

            AdmobAdsManager.showInterAd(this, intent);
        } else {
            startActivity(intent);
        }
    }
    private void goToStatus(){
        Intent intent = new Intent(SettingActivity.this, InfoStatusActivity.class);

        if (AdmobAdsManager.isAdmob) {
            AdmobAdsManager.counter++;

            AdmobAdsManager.showInterAd(this, intent);
        } else {
            startActivity(intent);
        }
    }

    private void getVersionName(){
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            String version = pInfo.versionName;
            binding.tvVersion.setText("Version: "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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