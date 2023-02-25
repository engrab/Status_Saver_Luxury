package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.fragments;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters.DownloadAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.FragmentSavedStatusBinding;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SavedStatusFragment extends Fragment {
    File file;
    public static ArrayList<DataModel> downloadImageList = new ArrayList<>();
    RecyclerView.LayoutManager mLayoutManager;
    DownloadAdapter mAdapter;
    private FragmentSavedStatusBinding binding;

    public static File[] dirListByAscendingDate(File folder) {
        if (!folder.isDirectory()) {
            return null;
        }
        File[] sortedByDate = folder.listFiles();
        if (sortedByDate == null || sortedByDate.length <= 1) {
            return sortedByDate;
        }
        Arrays.sort(sortedByDate, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        return sortedByDate;
    }

    public View onCreateView(@NonNull LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        binding = FragmentSavedStatusBinding.inflate(paramLayoutInflater, paramViewGroup, false);
        View view = binding.getRoot();
        binding.rv.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.rv.setLayoutManager(this.mLayoutManager);
        loadMedia();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void loadMedia() {
        file = Utils.downloadWhatsAppDir;

        downloadImageList.clear();
        if (!file.isDirectory()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0) {
                displayFiles(file, binding.rv);
            }
        } else {
            displayFiles(file, binding.rv);
        }
    }

    void displayFiles(File file, final RecyclerView rv) {
        File[] listMediaFiles = dirListByAscendingDate(file);
        if (listMediaFiles.length != 0) {
            binding.isEmptyList.setVisibility(View.GONE);
        } else {
            binding.isEmptyList.setVisibility(View.VISIBLE);
        }
        int i = 0;
        while (i < listMediaFiles.length) {
            downloadImageList.add(new DataModel(listMediaFiles[i].getAbsolutePath(), listMediaFiles[i].getName(), true));
            i++;
        }

        if (downloadImageList.size() > 0) {
            binding.isEmptyList.setVisibility(View.GONE);
        } else {
            binding.isEmptyList.setVisibility(View.VISIBLE);
        }
        mAdapter = new DownloadAdapter(getActivity(), downloadImageList);
        binding.rv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}
