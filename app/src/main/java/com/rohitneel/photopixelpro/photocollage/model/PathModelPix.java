package com.rohitneel.photopixelpro.photocollage.model;

import java.io.Serializable;

public class PathModelPix implements Serializable {

    public Integer pathInt;
    public String pathString = "offLine";

    public PathModelPix() {
    }

    public Integer getPathInt() {
        return pathInt;
    }

    public void setPathInt(Integer pathInt) {
        this.pathInt = pathInt;
    }

    public String getPathString() {
        return pathString;
    }

    public void setPathString(String pathString) {
        this.pathString = pathString;
    }
}
