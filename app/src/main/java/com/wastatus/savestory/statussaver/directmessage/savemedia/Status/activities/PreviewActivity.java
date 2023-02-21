package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters.FullscreenImageAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.LayManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityPreviewBinding;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    private ActivityPreviewBinding binding;
    ArrayList<DataModel> imageList;
    int position;
    AdView adView;
    String pakage;


    FullscreenImageAdapter fullscreenImageAdapter;
    String path;



    String folderPath;
    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.llSave:
                    if (imageList.size() > 0) {


                        if (AdmobAdsManager.isAdmob) {
                            AdmobAdsManager.counter++;

                            AdmobAdsManager.showInterAd(PreviewActivity.this, null);
                        }


                        try {
                            Utils.download(PreviewActivity.this, imageList.get(binding.viewPager.getCurrentItem()).getFilePath());
                            Toast.makeText(PreviewActivity.this, "Status saved successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(PreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.llRepost:


                    if (imageList.size() > 0) {
                        Utils.repostOnWhatsapp(PreviewActivity.this, Utils.isVideoFile(PreviewActivity.this, imageList.get(binding.viewPager.getCurrentItem()).getFilePath()), imageList.get(binding.viewPager.getCurrentItem()).getFilePath(), pakage);
                    } else {
                        finish();
                    }

                    break;

                case R.id.llShare:
                    if (imageList.size() > 0) {
                        Utils.shareFile(PreviewActivity.this, Utils.isVideoFile(PreviewActivity.this, imageList.get(binding.viewPager.getCurrentItem()).getFilePath()), imageList.get(binding.viewPager.getCurrentItem()).getFilePath());
                    } else {
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(getString(R.string.info_status));
        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewActivity.super.onBackPressed();
            }
        });



        LinearLayout.LayoutParams btnParam = LayManager.setLinParams(PreviewActivity.this, 300, 100);
        binding.llSave.setLayoutParams(btnParam);
        binding.llShare.setLayoutParams(btnParam);

        imageList = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        path = getIntent().getStringExtra("statusdownload");
        folderPath = getIntent().getStringExtra("folderpath");
        pakage = getIntent().getStringExtra("pakage");

        if (path.equals("download")) {

            binding.llSave.setVisibility(View.GONE);
            binding.llRepost.setVisibility(View.GONE);
        } else {

            binding.llSave.setVisibility(View.VISIBLE);
        }

        fullscreenImageAdapter = new FullscreenImageAdapter(PreviewActivity.this, imageList);
        binding.viewPager.setAdapter(fullscreenImageAdapter);
        binding.viewPager.setCurrentItem(position);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (AdmobAdsManager.isAdmob) {
                    AdmobAdsManager.counter++;

                    AdmobAdsManager.showInterAd(PreviewActivity.this, null);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.llSave.setOnClickListener(clickListener);
        binding.llShare.setOnClickListener(clickListener);
        binding.llRepost.setOnClickListener(clickListener);

        if (AdmobAdsManager.isAdmob) {

            adView = AdmobAdsManager.banner(this, findViewById(R.id.banner_container));
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

    void delete(int currentItem) {
        if (imageList.size() > 0 && binding.viewPager.getCurrentItem() < imageList.size()) {
            currentItem = binding.viewPager.getCurrentItem();
        }
        imageList.remove(binding.viewPager.getCurrentItem());
        fullscreenImageAdapter = new FullscreenImageAdapter(PreviewActivity.this, imageList);
        binding.viewPager.setAdapter(fullscreenImageAdapter);

        Intent intent = new Intent();
        setResult(10, intent);

        if (imageList.size() > 0) {
            binding.viewPager.setCurrentItem(currentItem);
        } else {
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
