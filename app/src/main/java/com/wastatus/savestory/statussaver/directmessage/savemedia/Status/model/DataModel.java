package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class DataModel implements Parcelable {
    public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel in) {
            return new DataModel(in);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };
    private String filename;
    private String filepath;
    private Boolean isSaved;

    public DataModel(String filepath, String filename, Boolean isSaved) {
        this.filepath = filepath;
        this.filename = filename;
        this.isSaved = isSaved;
    }

    public DataModel(String filepath, String filename) {
        this.filepath = filepath;
        this.filename = filename;
        this.isSaved = false;
    }

    protected DataModel(Parcel in) {
        filename = in.readString();
        filepath = in.readString();
        byte tmpIsSaved = in.readByte();
        isSaved = tmpIsSaved == 0 ? null : tmpIsSaved == 1;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Boolean getSaved() {
        return isSaved;
    }

    public void setSaved(Boolean saved) {
        isSaved = saved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(filename);
        dest.writeString(filepath);
        dest.writeByte((byte) (isSaved == null ? 0 : isSaved ? 1 : 2));
    }
}
