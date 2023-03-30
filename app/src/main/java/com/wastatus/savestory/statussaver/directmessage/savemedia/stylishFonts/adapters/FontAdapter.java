package com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.adapters;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.Classes.Font;

import java.util.ArrayList;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontHolder> {
    Context context;
    ArrayList<Font> dataSet;
    AdapterView.OnItemClickListener onItemClickListener;
    SharedPreferences preferences;

    public FontAdapter(Context context, ArrayList<Font> arrayList, AdapterView.OnItemClickListener onItemClickListener) {
        dataSet = arrayList;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    @NonNull
    public FontHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        preferences = PreferenceManager.getDefaultSharedPreferences(viewGroup.getContext());
        return new FontHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.font_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final FontHolder fontHolder, int i) {
        fontHolder.tvStylishText.setText(dataSet.get(fontHolder.getAdapterPosition()).fontText);

        fontHolder.ivWhatsapp.setOnClickListener(view -> {
            if (Utils.appInstalledOrNot(context, "com.whatsapp")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", fontHolder.tvStylishText.getText());
                intent.setPackage("com.whatsapp");
                intent.setType("text/plain");
                context.startActivity(intent);
            }else {
                Toast.makeText(context, "Please Install Whatsapp", Toast.LENGTH_SHORT).show();
            }
        });
        fontHolder.ivCopy.setOnClickListener(view -> {
            ((ClipboardManager) fontHolder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("stylish text", fontHolder.tvStylishText.getText()));
            Toast.makeText(fontHolder.itemView.getContext(), "Text Copied", Toast.LENGTH_SHORT).show();
        });
        fontHolder.ivShare.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", fontHolder.tvStylishText.getText());
            context.startActivity(intent);
        });
    }

    public int getItemCount() {
        return dataSet.size();
    }

    public static class FontHolder extends RecyclerView.ViewHolder {
        ImageView ivCopy;
        ImageView ivShare;
        ImageView ivWhatsapp;
        TextView tvStylishText;

        public FontHolder(@NonNull View view) {
            super(view);
            tvStylishText = view.findViewById(R.id.tvCaption);
            ivWhatsapp = view.findViewById(R.id.iv_whatsapp);
            ivCopy = view.findViewById(R.id.iv_copy);
            ivShare = view.findViewById(R.id.iv_share);
        }
    }
}
