package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LayManager {

    public static RelativeLayout.LayoutParams setRelParams (Context context, int width, int height){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels * width / 1080,
                context.getResources().getDisplayMetrics().heightPixels * height / 1920);
        return params;
    }

    public static LinearLayout.LayoutParams setLinParams (Context context, int width, int height){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels * width / 1080,
                context.getResources().getDisplayMetrics().heightPixels * height / 1920);
        return params;
    }

}
