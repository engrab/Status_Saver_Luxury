package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;


import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.adapters.PreviewPagerAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels.StatusViewModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.listener.DownloadClickListener;
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityPreviewBinding;
import com.google.android.gms.ads.AdView;
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.StatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PreviewActivity extends AppCompatActivity {

    private ActivityPreviewBinding binding;
    ArrayList<StatusModel> imageList;
    int position;
    AdView adView;
    String pakage;
    StatusViewModel viewModel;
    int pos;


    PreviewPagerAdapter previewPagerAdapter;
    String path;



    String folderPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        viewModel = new ViewModelProvider(this).get(StatusViewModel.class);


        binding.toolbar.setTitle(getString(R.string.info_status));
        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewActivity.super.onBackPressed();
            }
        });




        imageList = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        path = getIntent().getStringExtra("statusdownload");
        folderPath = getIntent().getStringExtra("folderpath");
        pakage = getIntent().getStringExtra("pakage");

        if (path.equals("download")) {

            binding.llSave.setVisibility(View.GONE);
            binding.llRepost.setVisibility(View.GONE);
            binding.llDelete.setVisibility(View.VISIBLE);
        } else {

            binding.llSave.setVisibility(View.VISIBLE);
        }

        previewPagerAdapter = new PreviewPagerAdapter(PreviewActivity.this, imageList);
        binding.viewPager.setAdapter(previewPagerAdapter);
        binding.viewPager.setCurrentItem(position);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                pos = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.llSave.setOnClickListener(clickListener);
        binding.llShare.setOnClickListener(clickListener);
        binding.llRepost.setOnClickListener(clickListener);
        binding.llDelete.setOnClickListener(clickListener);

        if (AdmobAdsManager.isAdmob) {

            adView = AdmobAdsManager.banner(this, binding.llAds);
        }

    }
    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.llSave:
                    if (imageList.size() > 0) {


                        if (AdmobAdsManager.isAdmob) {

                            AdmobAdsManager.showInterAd(PreviewActivity.this, null);
                        }


                        try {
                            Utils.copyFileInSavedDir(
                                    PreviewActivity.this,
                                    imageList.get(binding.viewPager.getCurrentItem()).getPath(),
                                    imageList.get(binding.viewPager.getCurrentItem()).getName()
                            );
//                            Utils.download(PreviewActivity.this, imageList.get(binding.viewPager.getCurrentItem()).getName(), imageList.get(binding.viewPager.getCurrentItem()).getPath());
                            Toast.makeText(PreviewActivity.this, "Status saved successfully", Toast.LENGTH_SHORT).show();

                            viewModel.getSavedMedia();
                        } catch (Exception e) {
                            Toast.makeText(PreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.llRepost:


                    if (imageList.size() > 0) {
                        Utils.repostOnWhatsapp(PreviewActivity.this, Utils.isVideoFile(PreviewActivity.this, imageList.get(binding.viewPager.getCurrentItem()).getPath()), imageList.get(binding.viewPager.getCurrentItem()).getPath(), pakage);
                    } else {
                        finish();
                    }

                    break;

                case R.id.llShare:
                    if (imageList.size() > 0) {
                        Utils.shareFile(PreviewActivity.this, Utils.isVideoFile(PreviewActivity.this, imageList.get(binding.viewPager.getCurrentItem()).getPath()), imageList.get(binding.viewPager.getCurrentItem()).getPath());
                    } else {
                        finish();
                    }
                    break;

                case R.id.llDelete:
                    if (imageList.size() > 0) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreviewActivity.this, R.style.AlertDialogTheme);
                        alertDialog.setTitle("Delete");
                        alertDialog.setMessage("Sure to Delete this Image?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int currentItem = 0;

                                if (path.equals("download")) {
                                    File file = new File(imageList.get(binding.viewPager.getCurrentItem()).getPath());
                                    if (file.exists()) {
                                        boolean del = file.delete();
                                        delete(currentItem);
                                    }
                                } else {
                                    DocumentFile fromTreeUri = DocumentFile.fromSingleUri(PreviewActivity.this, Uri.parse(imageList.get(binding.viewPager.getCurrentItem()).getPath()));
                                    if (fromTreeUri.exists()) {
                                        boolean del = fromTreeUri.delete();
                                        delete(currentItem);
                                    }
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();


                        if (AdmobAdsManager.isAdmob) {


                            AdmobAdsManager.showInterAd(PreviewActivity.this, null);
                        }

                    } else {
                        finish();
                    }
                default:
                    break;
            }
        }
    };
    void delete(int currentItem) {
        if (imageList.size() > 0 && binding.viewPager.getCurrentItem() < imageList.size()) {
            currentItem = binding.viewPager.getCurrentItem();
        }
        imageList.remove(binding.viewPager.getCurrentItem());
        previewPagerAdapter = new PreviewPagerAdapter(PreviewActivity.this, imageList);
        binding.viewPager.setAdapter(previewPagerAdapter);

        Intent intent = new Intent();
        setResult(10, intent);

        if (imageList.size() > 0) {
            binding.viewPager.setCurrentItem(currentItem);
        } else {
            finish();
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
