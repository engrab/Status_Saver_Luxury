package com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.InputDeviceCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.Classes.CaptionClass;
import com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.Classes.Font;
import com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.adapters.FontAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityStylishFontsBinding;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Objects;

public class StylishFontsActivity extends AppCompatActivity {

    ArrayList<Font> fontList;
    String fontText;

    AdView adView;
    private ActivityStylishFontsBinding binding;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityStylishFontsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(InputDeviceCompat.SOURCE_TOUCHSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        fontList = new ArrayList<>();
        binding.etText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                makeStylishOf(charSequence);
            }
        });

        if (AdmobAdsManager.isAdmob) {

            adView = AdmobAdsManager.banner(this, findViewById(R.id.llAds));
        }
    }


    public void makeStylishOf(CharSequence charSequence) {
        char[] charArray = charSequence.toString().toLowerCase().toCharArray();
        String[] strArr = new String[44];
        for (int i = 0; i < 44; i++) {
            strArr[i] = applyStyle(charArray, CaptionClass.strings[i]);
        }
        styleTheFont(strArr);
    }

    private String applyStyle(char[] cArr, String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < cArr.length; i++) {
            if (cArr[i] - 'a' < 0 || cArr[i] - 'a' > 25) {
                stringBuffer.append(cArr[i]);
            } else {
                stringBuffer.append(strArr[cArr[i] - 'a']);
            }
        }
        return stringBuffer.toString();
    }

    private void styleTheFont(String[] strArr) {
        fontList.clear();
        fontText = binding.etText.getText().toString().trim();
        if (!fontText.isEmpty()) {
            for (int i = 0; i < 44; i++) {
                Font font = new Font();
                font.fontText = strArr[i];
                fontList.add(font);
            }
            binding.rvFonts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            binding.rvFonts.setAdapter(new FontAdapter(this, fontList, new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                }
            }));
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
