package com.wastatus.savestory.statussaver.directmessage.savemedia.textRepeater.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityTextRepeaterBinding;
import com.google.android.gms.ads.AdView;

import java.util.Objects;

public class TextRepeaterActivity extends AppCompatActivity {
    private final String inputTxt = "inputTxt";
    private final String inputEmoji = "inputEmoji";
    private final String inputConvert = "inputConvert";
    String strMain;
    int noOfRepeat;
    String strRepeat;
    boolean isNewLine = false;
    String no;
    ProgressDialog progressDialog;
    AdView adView;
    private ActivityTextRepeaterBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextRepeaterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        progressDialog = new ProgressDialog(this);
        if (isNewLine) {
            binding.tvNewLine.setText(R.string.new_line_on);
            binding.ivNewLine.setImageResource(R.drawable.ons);
        } else {
            binding.tvNewLine.setText(R.string.new_line_off);
            binding.ivNewLine.setImageResource(R.drawable.offs);
        }
        binding.ivNewLine.setOnClickListener(new newLineClick());
        binding.btnConvert.setOnClickListener(new btnConvertListener());
        binding.ivDelete.setOnClickListener(new btnClearTextListener());
        binding.ivCopy.setOnClickListener(new btnCopyListener());
        binding.ivShare.setOnClickListener(new btnShareListener());

        if (savedInstanceState != null) {
            binding.inputText.setText(savedInstanceState.getString(inputTxt));
            binding.etNumber.setText(savedInstanceState.getString(inputEmoji));
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
        outState.putString(inputEmoji, binding.etNumber.getText().toString());
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

    private class btnConvertListener implements OnClickListener {
        public void onClick(View view) {
            binding.tvConverted.setText("");
            strRepeat = binding.inputText.getText().toString();
            no = binding.etNumber.getText().toString();
            try {
                noOfRepeat = Integer.parseInt(no);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            if (binding.inputText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Repeat Text", Toast.LENGTH_SHORT).show();
            } else if (binding.etNumber.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Number of Repeat Text", Toast.LENGTH_SHORT).show();
            } else if (noOfRepeat <= 10000) {
                new CreateRepeatText().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Number of Repeater Text Limited Please Enter Limited Number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class btnClearTextListener implements OnClickListener {
        public void onClick(View view) {
            binding.tvConverted.setText("");
        }
    }

    private class btnCopyListener implements OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (binding.tvConverted.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Convert text before copy", Toast.LENGTH_SHORT).show();
                return;
            }
            ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(binding.inputText.getText().toString(), binding.tvConverted.getText().toString()));
            Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private class btnShareListener implements OnClickListener {
        public void onClick(View view) {
            if (binding.tvConverted.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Convert text to share", Toast.LENGTH_LONG).show();
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

    private class CreateRepeatText extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressStyle(0);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        public String doInBackground(String... strings) {
            int i;
            if (isNewLine) {
                for (i = 1; i <= noOfRepeat; i++) {
                    if (i == 1) {
                        strMain = strRepeat;
                    } else {
                        strMain += "\n" + strRepeat;
                    }
                }
            } else {
                for (i = 1; i <= noOfRepeat; i++) {
                    if (i == 1) {
                        strMain = strRepeat;
                    } else {
                        strMain += "\t" + strRepeat;
                    }
                }
            }
            return null;
        }

        @SuppressLint({"LongLogTag"})
        public void onPostExecute(String result) {
            progressDialog.dismiss();
            binding.tvConverted.setText(strMain);
        }
    }

    private class newLineClick implements OnClickListener {

        public void onClick(View v) {
            if (isNewLine) {
                isNewLine = false;
                binding.tvNewLine.setText(getString(R.string.new_line_off));
                binding.ivNewLine.setImageResource(R.drawable.offs);
                return;
            }
            isNewLine = true;
            binding.tvNewLine.setText(getString(R.string.new_line_on));
            binding.ivNewLine.setImageResource(R.drawable.ons);
        }
    }
}
