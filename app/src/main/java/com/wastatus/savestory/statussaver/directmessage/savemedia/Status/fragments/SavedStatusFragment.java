package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters.DownloadAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.FragmentSavedStatusBinding;

public class SavedStatusFragment extends Fragment {

    RecyclerView.LayoutManager mLayoutManager;
    DownloadAdapter mAdapter;
    private FragmentSavedStatusBinding binding;


    public View onCreateView(@NonNull LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        binding = FragmentSavedStatusBinding.inflate(paramLayoutInflater, paramViewGroup, false);
        View view = binding.getRoot();
        binding.rv.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.rv.setLayoutManager(this.mLayoutManager);
        displayFiles();
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


    void displayFiles() {

        if (Utils.downloadedList.size() > 0) {
            binding.isEmptyList.setVisibility(View.GONE);
        } else {
            binding.isEmptyList.setVisibility(View.VISIBLE);
            return;
        }
        mAdapter = new DownloadAdapter(getActivity(), Utils.downloadedList);
        binding.rv.setAdapter(mAdapter);

    }

}
