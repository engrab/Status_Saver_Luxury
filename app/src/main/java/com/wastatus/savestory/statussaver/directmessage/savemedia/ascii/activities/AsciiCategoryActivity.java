package com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.adapters.AsciCategoryAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.model.ListModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityCategoryAsciiBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsciiCategoryActivity extends AppCompatActivity {
    List<ListModel> list;

    private ActivityCategoryAsciiBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAsciiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        getList();
        binding.simpleGridView.setAdapter(new AsciCategoryAdapter(this, list));
        binding.simpleGridView.setOnItemClickListener(new simpleGridListener());

    }


    private void getList() {
        list = new ArrayList<>();
        list.add(new ListModel(R.drawable.ic_moring, getString(R.string.good_morning)));
        list.add(new ListModel(R.drawable.ic_night, getString(R.string.good_night)));
        list.add(new ListModel(R.drawable.ic_thank, getString(R.string.thanks_you)));
        list.add(new ListModel(R.drawable.ic_love, getString(R.string.Love)));
        list.add(new ListModel(R.drawable.ic_happy, getString(R.string.happy)));
        list.add(new ListModel(R.drawable.ic_greeting, getString(R.string.greetings)));
        list.add(new ListModel(R.drawable.ic_lion, getString(R.string.animals)));
        list.add(new ListModel(R.drawable.ic_dog, getString(R.string.dog)));
        list.add(new ListModel(R.drawable.ic_cat, getString(R.string.cat)));
        list.add(new ListModel(R.drawable.ic_heart, getString(R.string.heart)));
        list.add(new ListModel(R.drawable.ic_confused, getString(R.string.confused)));
        list.add(new ListModel(R.drawable.ic_cute, getString(R.string.cute)));
        list.add(new ListModel(R.drawable.ic_sad, getString(R.string.sad)));
        list.add(new ListModel(R.drawable.ic_dance, getString(R.string.dance)));
        list.add(new ListModel(R.drawable.ic_eating, getString(R.string.eating)));
        list.add(new ListModel(R.drawable.ic_hug, getString(R.string.hug)));
        list.add(new ListModel(R.drawable.ic_disapproval, getString(R.string.disapporval)));
        list.add(new ListModel(R.drawable.ic_laughing, getString(R.string.laughing)));
        list.add(new ListModel(R.drawable.ic_angry, getString(R.string.angry)));
        list.add(new ListModel(R.drawable.ic_thinking, getString(R.string.thinking)));

    }

    private class simpleGridListener implements OnItemClickListener {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(AsciiCategoryActivity.this, AsciiActivity.class);
            intent.putExtra("name", list.get(position).getName());
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }


}
