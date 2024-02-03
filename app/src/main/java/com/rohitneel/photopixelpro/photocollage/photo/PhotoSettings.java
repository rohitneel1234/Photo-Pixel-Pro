package com.rohitneel.photopixelpro.photocollage.photo;

public class PhotoSettings {
    private boolean isClearViewsEnabled;
    private boolean isTransparencyEnabled;

    public boolean isTransparencyEnabled() {
        return this.isTransparencyEnabled;
    }

    public boolean isClearViewsEnabled() {
        return this.isClearViewsEnabled;
    }

    private PhotoSettings(Builder builder) {
        this.isClearViewsEnabled = builder.isClearViewsEnabled;
        this.isTransparencyEnabled = builder.isTransparencyEnabled;
    }

    public static class Builder {

        public boolean isClearViewsEnabled = true;

        public boolean isTransparencyEnabled = true;

        public PhotoSettings build() {
            return new PhotoSettings(this);
        }
    }
}
