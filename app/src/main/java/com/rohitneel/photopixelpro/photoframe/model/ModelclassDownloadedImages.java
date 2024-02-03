package com.rohitneel.photopixelpro.photoframe.model;

import android.graphics.Bitmap;

public class ModelclassDownloadedImages {

    private String name;
    private String imagepath;
    private Bitmap thumbimage;

    public ModelclassDownloadedImages(String name, String imagepath, Bitmap thumbimage) {
        this.name = name;
        this.imagepath = imagepath;
        this.thumbimage = thumbimage;
    }

    public ModelclassDownloadedImages(String name, String imagepath) {
        this.name = name;
        this.imagepath = imagepath;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public Bitmap getThumbimage() {
        return thumbimage;
    }

    public void setThumbimage(Bitmap thumbimage) {
        this.thumbimage = thumbimage;
    }
}
