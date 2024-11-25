package com.rohitneel.photopixelpro.photocollage.model;

import java.util.ArrayList;

public class FolderItem {
    public String name;
    public String path;
    public String coverPath;
    public ArrayList<ImageItem> mImages = new ArrayList<>();

    public FolderItem(String nm, String pt, String cp) {
        name = nm;
        path = pt;
        coverPath = cp;
    }

    public void addImageItem(ImageItem imageItem) {
        mImages.add(imageItem);
    }

    public String getNumOfImages() {
        return String.format("%d", mImages.size());
    }

    @Override
    public String toString() {
        return "FolderItem{" +
                "coverImagePath='" + coverPath + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", numOfImages=" + mImages.size() +
                '}';
    }
}
