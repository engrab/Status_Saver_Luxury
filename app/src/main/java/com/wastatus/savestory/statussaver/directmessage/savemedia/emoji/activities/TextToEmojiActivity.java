package com.wastatus.savestory.statussaver.directmessage.savemedia.emoji.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityTextToEmojiBinding;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class TextToEmojiActivity extends AppCompatActivity {

    AdView adView;
    private final String inputTxt ="inputTxt";
    private final String inputEmoji = "inputEmoji";
    private final String inputConvert = "inputConvert";
    private ActivityTextToEmojiBinding binding;


    private class btnConvertListener implements OnClickListener {
        public void onClick(View view) {
            if (binding.inputText.getText().toString().isEmpty()) {
                Toast.makeText(TextToEmojiActivity.this, R.string.enter_text_here, Toast.LENGTH_SHORT).show();
                return;
            }
            char[] charArray = binding.inputText.getText().toString().toCharArray();
            binding.tvConverted.setText(".\n");
            for (char character : charArray) {
                byte[] localObject3;
                InputStream localObject2;
                if (character == '?') {
                    try {
                        InputStream localObject1 = getBaseContext().getAssets().open("ques.txt");
                        localObject3 = new byte[localObject1.available()];
                        localObject1.read(localObject3);
                        localObject1.close();
                        binding.tvConverted.append(new String(localObject3).replaceAll("[*]", binding.etEmoji.getText().toString()) + "\n\n");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } else if (character == ((char) (character & 95)) || Character.isDigit(character)) {
                    try {
                        localObject2 = getBaseContext().getAssets().open(character + ".txt");
                        localObject3 = new byte[localObject2.available()];
                        localObject2.read(localObject3);
                        localObject2.close();
                        binding.tvConverted.append(new String(localObject3).replaceAll("[*]", binding.etEmoji.getText().toString()) + "\n\n");
                    } catch (IOException ioe2) {
                        ioe2.printStackTrace();
                    }
                } else {
                    try {
                        localObject2 = getBaseContext().getAssets().open("low" + character + ".txt");
                        localObject3 = new byte[localObject2.available()];
                        localObject2.read(localObject3);
                        localObject2.close();
                        binding.tvConverted.append(new String(localObject3).replaceAll("[*]", binding.etEmoji.getText().toString()) + "\n\n");
                    } catch (IOException ioe22) {
                        ioe22.printStackTrace();
                    }
                }
            }
        }
    }

    private class btnClearTextListener implements OnClickListener {
        public void onClick(View view) {
            binding.tvConverted.setText("");
        }
    }

    private class btnCopyButtonListener implements OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (binding.tvConverted.getText().toString().isEmpty()) {
                Toast.makeText(TextToEmojiActivity.this, R.string.convert_text, Toast.LENGTH_SHORT).show();
                return;
            }
            ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(binding.inputText.getText().toString(), binding.tvConverted.getText().toString()));
            Toast.makeText(TextToEmojiActivity.this, R.string.copy, Toast.LENGTH_SHORT).show();
        }
    }

    private class btnShareListener implements OnClickListener {
        public void onClick(View view) {
            if (binding.tvConverted.getText().toString().isEmpty()) {
                Toast.makeText(TextToEmojiActivity.this, R.string.convert_share, Toast.LENGTH_LONG).show();
                return;
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.setPackage("com.whatsapp");
            shareIntent.putExtra("android.intent.extra.TEXT", binding.tvConverted.getText().toString());
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "Select an app to share"));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextToEmojiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());


       
        binding.btnConvert.setOnClickListener(new btnConvertListener());
        binding.ivDelete.setOnClickListener(new btnClearTextListener());
        binding.ivCopy.setOnClickListener(new btnCopyButtonListener());
        binding.ivShare.setOnClickListener(new btnShareListener());

        if (savedInstanceState != null){
            binding.inputText.setText(savedInstanceState.getString(inputTxt));
            binding.etEmoji.setText(savedInstanceState.getString(inputEmoji));
            binding.tvConverted.setText(savedInstanceState.getString(inputConvert));

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(inputTxt, binding.inputText.getText().toString());
        outState.putString(inputEmoji, binding.etEmoji.getText().toString());
        outState.putString(inputConvert, binding.tvConverted.getText().toString());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
