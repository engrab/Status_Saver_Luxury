package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.activities.PreviewActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;

import java.io.File;
import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    ArrayList<DataModel> mData;
    private final Activity activity;
    private File file;

    public DownloadAdapter(Activity paramActivity, ArrayList<DataModel> paramArrayList) {
        this.mData = paramArrayList;
        this.activity = paramActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final DataModel datamodel = this.mData.get(holder.getAdapterPosition());
        this.file = new File(datamodel.getFilepath());
        if (!this.file.isDirectory()) {
            if (!Utils.getBack(datamodel.getFilepath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(1))).into(holder.image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.imagePlayer.setVisibility(View.VISIBLE);
            } else if (!Utils.getBack(datamodel.getFilepath(), "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
                holder.imagePlayer.setVisibility(View.GONE);
            } else if (!Utils.getBack(datamodel.getFilepath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                holder.imagePlayer.setVisibility(View.GONE);
                Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(1))).into(holder.image);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final RelativeLayout cardView;
        private final ImageView imagePlayer;
        private final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.imageView);
            this.imagePlayer = itemView.findViewById(R.id.ivPlay);
            this.cardView = itemView.findViewById(R.id.card_view);
            this.cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, PreviewActivity.class);
            intent.putParcelableArrayListExtra("images", mData);
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("statusdownload", "download");

            if (AdmobAdsManager.isAdmob) {

                AdmobAdsManager.showInterAd(activity, intent);
            }else {
                activity.startActivity(intent);
            }


        }
    }
}
