package com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.model;

import java.io.Serializable;

public class ListModel implements Serializable {
    private int image;
    private String name;

    public ListModel() {
    }

    public ListModel(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
