package com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.adapters.AsciAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityAscciiBinding;
import com.google.android.gms.ads.AdView;


public class AsciiActivity extends AppCompatActivity {
    String name;
    String[] items;
    int position;
    private ActivityAscciiBinding binding;
    private AdView adView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAscciiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        name = getIntent().getStringExtra("name");
        binding.toolbar.setTitle(name);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        position = getIntent().getIntExtra("position", 0);
        if (position == 0) {
            items = getResources().getStringArray(R.array.as_morning);
        } else if (position == 1) {
            items = getResources().getStringArray(R.array.as_night);
        } else if (position == 2) {
            items = getResources().getStringArray(R.array.as_thanks);
        } else if (position == 3) {
            items = getResources().getStringArray(R.array.as_love);
        } else if (position == 4) {
            items = getResources().getStringArray(R.array.as_happy);
        } else if (position == 5) {
            items = getResources().getStringArray(R.array.as_greetings);
        } else if (position == 6) {
            items = getResources().getStringArray(R.array.as_animals);
        } else if (position == 7) {
            items = getResources().getStringArray(R.array.as_dog);
        } else if (position == 8) {
            items = getResources().getStringArray(R.array.as_cat);
        } else if (position == 9) {
            items = getResources().getStringArray(R.array.as_heart);
        } else if (position == 10) {
            items = getResources().getStringArray(R.array.as_confused);
        } else if (position == 11) {
            items = getResources().getStringArray(R.array.as_cute);
        } else if (position == 12) {
            items = getResources().getStringArray(R.array.as_sad);
        } else if (position == 13) {
            items = getResources().getStringArray(R.array.as_dance);
        } else if (position == 14) {
            items = getResources().getStringArray(R.array.as_eating);
        } else if (position == 15) {
            items = getResources().getStringArray(R.array.as_hug);
        } else if (position == 16) {
            items = getResources().getStringArray(R.array.as_disapproval);
        } else if (position == 17) {
            items = getResources().getStringArray(R.array.as_laughing);
        } else if (position == 18) {
            items = getResources().getStringArray(R.array.as_angry);
        } else if (position == 19) {
            items = getResources().getStringArray(R.array.as_thinking);
        }
        binding.simpleListView.setAdapter(new AsciAdapter(this, items));
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


}
