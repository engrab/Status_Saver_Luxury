package com.wastatus.savestory.statussaver.directmessage.savemedia.directChat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityChatDirectBinding;
import com.google.android.gms.ads.AdView;
import com.hbb20.CountryCodePicker;
import com.hbb20.CountryCodePicker.OnCountryChangeListener;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;

public class ChatDirectActivity extends AppCompatActivity {

    CountryCodePicker codePicker;
    private SharedPreferences preference;
    AdView adView;
    private ActivityChatDirectBinding binding;


    private class btnSendMessageListener implements OnClickListener {
        public void onClick(View v) {
            String message = binding.etMessage.getText().toString();
            String number = binding.etNumber.getText().toString();
            String mainNumber = codePicker.getSelectedCountryCode() + number;
            if (message.length() == 0) {
                Toast.makeText(ChatDirectActivity.this, R.string.enter_message, Toast.LENGTH_SHORT).show();
            } else if (number.length() == 0) {
                Toast.makeText(ChatDirectActivity.this, R.string.message_number_empty, Toast.LENGTH_SHORT).show();
            } else if (number.length() < 7 || message.length() <= 0) {
                Toast.makeText(ChatDirectActivity.this, R.string.message_number_error, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    PackageManager packageManager = getPackageManager();
                    Intent intent = new Intent("android.intent.action.VIEW");
                    try {
                        String str3 = "https://api.whatsapp.com/send?phone=" + mainNumber + "&text=" + message;
                        intent.setPackage("com.whatsapp");
                        intent.setData(Uri.parse(str3));
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    Toast.makeText(ChatDirectActivity.this, "Error/n" + e2, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private class btnCcpListener implements OnCountryChangeListener {
        public void onCountrySelected() {
            codePicker.setCountryPreference(codePicker.getSelectedCountryNameCode());
            preference.edit().putString("last_locale", codePicker.getSelectedCountryCode()).apply();
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDirectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(getString(R.string.direct_chat));
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> ChatDirectActivity.super.onBackPressed());


        codePicker = findViewById(R.id.ccp);
        binding.rlSend.setOnClickListener(new btnSendMessageListener());
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        codePicker.setCountryForNameCode(Helper.getCurrentLocale(this));
        codePicker.setOnCountryChangeListener(new btnCcpListener());
        if (getIntent().getStringExtra("number") != null) {
            binding.etNumber.setText(getIntent().getStringExtra("number"));
        }
        if (AdmobAdsManager.isAdmob) {
            adView = AdmobAdsManager.banner(this, binding.llAds);
        }

    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    public static class Helper {

        public static String getCurrentLocale(Context context) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getNetworkCountryIso();
            }
            return null;
        }
    }
}
