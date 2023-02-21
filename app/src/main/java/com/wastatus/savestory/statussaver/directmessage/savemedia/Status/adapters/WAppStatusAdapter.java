package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.activities.PreviewActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;

import java.util.ArrayList;


public class WAppStatusAdapter extends RecyclerView.Adapter<WAppStatusAdapter.ViewHolder> {
    private final Context activity;
    ArrayList<DataModel> mData;
    String folderPath;
    boolean isWApp;

    public WAppStatusAdapter(Context activity, ArrayList<DataModel> jData, boolean isWApp) {
        this.mData = jData;
        this.activity = activity;
        this.isWApp = isWApp;
        folderPath = Utils.downloadWhatsAppDir.getAbsolutePath();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataModel dataModel = this.mData.get(position);


        if (!Utils.getBack(dataModel.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            holder.imagePlayer.setVisibility(View.VISIBLE);
        } else {
            holder.imagePlayer.setVisibility(View.GONE);
        }


        Glide.with(this.activity).load(dataModel.getFilePath()).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(holder.imageView);


        holder.downloadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.copyFileInSavedDir(activity, dataModel.getFilePath());
                Toast.makeText(activity, "Saved successfully!", Toast.LENGTH_LONG).show();
                if (AdmobAdsManager.isAdmob) {

                    AdmobAdsManager.showInterAd((Activity) activity, null);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CardView cardView;
        private final ImageView imagePlayer;
        private final ImageView imageView;
        private final ImageView downloadIV;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.imagePlayer = itemView.findViewById(R.id.ivPlay);
            this.cardView = itemView.findViewById(R.id.card_view);
            this.cardView.setOnClickListener(this);
            this.downloadIV = itemView.findViewById(R.id.downloadIV);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, PreviewActivity.class);
            intent.putParcelableArrayListExtra("images", mData);
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("statusdownload", "status");
            intent.putExtra("folderpath", folderPath);
            if (isWApp){

                intent.putExtra("pakage", "com.whatsapp");
            }else {
                intent.putExtra("pakage", "com.whatsapp.w4b");

            }
            activity.startActivity(intent);
        }
    }


}
