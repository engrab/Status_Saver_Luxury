package com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.model.ListModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;

import java.util.List;

public class AsciCategoryAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<ListModel> captionList;

    public AsciCategoryAdapter(Context applicationContext, List<ListModel> logos) {
        captionList = logos;
        inflater = LayoutInflater.from(applicationContext);
    }

    public int getCount() {
        return captionList.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.gridview, null);
        ((TextView) view.findViewById(R.id.name)).setText(captionList.get(i).getName());
        ((ImageView) view.findViewById(R.id.image)).setImageResource(captionList.get(i).getImage());
        return view;
    }
}
