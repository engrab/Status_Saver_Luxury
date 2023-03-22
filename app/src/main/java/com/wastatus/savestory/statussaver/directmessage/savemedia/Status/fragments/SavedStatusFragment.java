package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters.DownloadAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.listener.SaveListener;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.FragmentSavedBinding;

public class SavedStatusFragment extends Fragment {
    private static final String TAG = "SavedStatusFragment";
    RecyclerView.LayoutManager mLayoutManager;
    DownloadAdapter mAdapter;
    private FragmentSavedBinding binding;


    public View onCreateView(@NonNull LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        binding = FragmentSavedBinding.inflate(paramLayoutInflater, paramViewGroup, false);
        View view = binding.getRoot();
        binding.rv.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.rv.setLayoutManager(this.mLayoutManager);

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
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            binding.progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayFiles();
                }
            },1000);
            binding.progressBar.setVisibility(View.GONE);
        }

    }


    private void displayFiles() {
        Utils.loadMedia();

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
