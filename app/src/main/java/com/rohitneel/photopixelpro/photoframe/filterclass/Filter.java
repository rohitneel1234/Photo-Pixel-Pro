package com.rohitneel.photopixelpro.photoframe.filterclass;

public class Filter {
    private int thumb;
    private String title;

    Filter(int paramInt, String paramString) {
        this.thumb = paramInt;
        this.title = paramString;
    }

    int getThumb() {
        return this.thumb;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String paramString) {
        this.title = paramString;
    }
}
