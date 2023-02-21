package com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wastatus.savestory.statussaver.directmessage.savemedia.R;

public class AsciAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final String[] items;

    public AsciAdapter(Context applicationContext, String[] items) {
        context = applicationContext;
        this.items = items;
        inflater = LayoutInflater.from(applicationContext);
    }

    public int getCount() {
        return items.length;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listview_item, null);
        ((TextView) view.findViewById(R.id.textView)).setText(items[i]);
        ImageView icon2 = view.findViewById(R.id.share_button);
        view.findViewById(R.id.copy_button).setOnClickListener(view12 -> {
            String str = items[i];
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copied Text", str));
            Toast.makeText(context, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
        });
        icon2.setOnClickListener(view1 -> {
            String str = items[i];
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.putExtra("android.intent.extra.TEXT", str);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        });
        return view;
    }
}
