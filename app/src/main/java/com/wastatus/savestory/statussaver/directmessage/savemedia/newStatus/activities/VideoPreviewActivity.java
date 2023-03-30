package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityVideoPreviewBinding;


public class VideoPreviewActivity extends AppCompatActivity {

    private ActivityVideoPreviewBinding binding;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPreviewActivity.super.onBackPressed();
            }
        });

        videoPath = getIntent().getStringExtra("videoPath");

        binding.videoView.setVideoPath(videoPath);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(binding.videoView);

        binding.videoView.setMediaController(mediaController);

        binding.videoView.start();

    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.videoView.setVideoPath(videoPath);
        binding.videoView.start();
    }


}
