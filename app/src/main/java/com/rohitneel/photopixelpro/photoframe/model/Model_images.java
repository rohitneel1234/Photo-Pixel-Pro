package com.rohitneel.photopixelpro.photoframe.model;

import java.util.ArrayList;

/**
 * Created by deepshikha on 3/3/17.
 */

public class Model_images {
    String str_folder;
    ArrayList<String> al_imagepath;

    public String getStr_folder() {
        return str_folder;
    }

    public void setStr_folder(String str_folder) {
        this.str_folder = str_folder;
    }

    public ArrayList<String> getAl_imagepath() {
        return al_imagepath;
    }

    public void setAl_imagepath(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }
}
