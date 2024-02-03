package com.rohitneel.photopixelpro.constant;

import android.graphics.Bitmap;

import com.rohitneel.photopixelpro.photoframe.model.ModelclassDownloadedImages;
import com.rohitneel.photopixelpro.photoframe.model.ModelclassFrameList;

import java.io.File;
import java.util.ArrayList;

public class CommonKeys {

    public static String mFrameKey = "frame";
    public static String mBirthdayKey = "birthday";
    public static String mFlowersKey = "flowers";
    public static String mLoveAnniversaryKey = "loveAnniversary";
    public static String mWallKey = "wall";
    public static String mBackgroundRemoverFilePath = "/storage/emulated/0/Pictures/Photo Pixel Pro/BGEraser";

    public static String mHomeKey = "HomeFragment";
    public static String mSettingsKey = "SettingsActivity";

    public static String Rateapp = "https://play.google.com/store/apps/details?id=com.rohitneel.photopixelpro";
    public static String FontStyle="0.ttf";
    public static boolean ischangetypeface = false;
    public static boolean imageSet=false,cameraImage = false;
    public static  int  FrameId,FrameId1;
    public static Bitmap Image,bmp;
    public static File filePath ;
    public static boolean AllowToOpenAdvertise = false;
    public static boolean isFirstTimeOpen = true;
    public static  ArrayList<ModelclassFrameList> frameLists;
    public static  ArrayList<ModelclassDownloadedImages> modelclassDownloadedImages;

}
