package com.rohitneel.photopixelpro.photocollage.constants;


import java.util.ArrayList;

public class Constants {

    public static int BORDER_WIDTH = 4;
    public static ArrayList<String> FORMAT_IMAGE = new ImageTypeList();
    public static String INSTAGRAM = "com.instagram.android";
    public static String MESSEGER = "com.facebook.orca";
    public static String TWITTER = "com.twitter.android";
    public static String WHATSAPP = "com.whatsapp";
    public static String FACEBOOK = "com.facebook.katana";
    public static String GMAIL = "com.google.android.gm";

    static class ImageTypeList extends ArrayList<String> {
        ImageTypeList() {
            add(".PNG");
            add(".JPEG");
            add(".jpg");
            add(".png");
            add(".jpeg");
            add(".JPG");
        }
    }
}
