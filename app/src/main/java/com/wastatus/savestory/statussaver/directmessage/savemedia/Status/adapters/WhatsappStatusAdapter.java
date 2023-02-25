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


public class WhatsappStatusAdapter extends RecyclerView.Adapter<WhatsappStatusAdapter.ViewHolder> {
    private final Context context;
    ArrayList<DataModel> dataList;
    String path;
    boolean isWApp;

    public WhatsappStatusAdapter(Context context, ArrayList<DataModel> dataList, boolean isWApp) {
        this.dataList = dataList;
        this.context = context;
        this.isWApp = isWApp;
        path = Utils.downloadWhatsAppDir.getAbsolutePath();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataModel dataModel = this.dataList.get(position);


        if (!Utils.getBack(dataModel.getFilepath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            holder.imagePlayer.setVisibility(View.VISIBLE);
        } else {
            holder.imagePlayer.setVisibility(View.GONE);
        }

        if (dataList.get(position).getSaved()){
            holder.downloadIV.setImageResource(R.drawable.ic_baseline_check_24);
        }else {
            holder.downloadIV.setImageResource(R.drawable.ic_baseline_download_24);
        }


        Glide.with(this.context).load(dataModel.getFilepath()).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(holder.imageView);


        holder.downloadIV.setOnClickListener(v -> {
            Utils.copyFileInSavedDir(context, dataModel.getFilepath());
            Toast.makeText(context, "Saved successfully!", Toast.LENGTH_LONG).show();
            if (AdmobAdsManager.isAdmob) {

                AdmobAdsManager.showInterAd((Activity) context, null);
            }
            holder.downloadIV.setImageResource(R.drawable.ic_baseline_check_24);

        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imagePlayer;
        private final ImageView imageView;
        private final ImageView downloadIV;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.imagePlayer = itemView.findViewById(R.id.ivPlay);
            CardView cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
            this.downloadIV = itemView.findViewById(R.id.downloadIV);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PreviewActivity.class);
            intent.putParcelableArrayListExtra("images", dataList);
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("statusdownload", "status");
            intent.putExtra("folderpath", path);
            if (isWApp){
                intent.putExtra("pakage", "com.whatsapp");
            }else {
                intent.putExtra("pakage", "com.whatsapp.w4b");
            }
            context.startActivity(intent);
        }
    }


}
