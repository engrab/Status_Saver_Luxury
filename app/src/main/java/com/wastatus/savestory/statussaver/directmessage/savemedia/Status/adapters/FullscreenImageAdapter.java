package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.activities.VideoPreviewActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;

import java.util.ArrayList;

public class FullscreenImageAdapter extends PagerAdapter {
    Activity activity;
    ArrayList<DataModel> imageList;

    public FullscreenImageAdapter(Activity activity, ArrayList<DataModel> imageList) {
        this.activity = activity;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.preview_list_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        ImageView ivPlay = itemView.findViewById(R.id.ivPlay);

        if (!Utils.getBack(imageList.get(position).getFilepath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            ivPlay.setVisibility(View.VISIBLE);
        } else {
            ivPlay.setVisibility(View.GONE);
        }

        Glide.with(this.activity).load(imageList.get(position).getFilepath()).into(imageView);

        imageView.setOnClickListener(view -> {
            if (!Utils.getBack(imageList.get(position).getFilepath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {

                Intent intent = new Intent(activity, VideoPreviewActivity.class);
                intent.putExtra("videoPath", imageList.get(position).getFilepath());


                if (AdmobAdsManager.isAdmob) {

                    AdmobAdsManager.showInterAd(activity, intent);
                }else {
                    activity.startActivity(intent);
                }

            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
