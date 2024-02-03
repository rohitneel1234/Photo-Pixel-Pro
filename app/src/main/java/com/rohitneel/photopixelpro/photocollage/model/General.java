package com.rohitneel.photopixelpro.photocollage.model;

public class General
{
    String typeAds;
    boolean connected;

    public General()
    {
    }

    public String getTypeAds() {
        return typeAds;
    }

    public General(String typeAds, boolean connected) {
        this.typeAds = typeAds;
        this.connected = connected;
    }

    public void setTypeAds(String typeAds) {
        this.typeAds = typeAds;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}

