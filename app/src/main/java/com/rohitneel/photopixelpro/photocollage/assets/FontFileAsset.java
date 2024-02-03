package com.rohitneel.photopixelpro.photocollage.assets;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FontFileAsset {

    public static void setFontByName(Context context, TextView textView, String name) {
        AssetManager assetManager = context.getAssets();
        textView.setTypeface(Typeface.createFromAsset(assetManager, "fonts/" + name));
    }

    public static List<String> getListFonts() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("font.ttf");
        arrayList.add("0.ttf");
        arrayList.add("1.ttf");
        arrayList.add("2.otf");
        arrayList.add("3.ttf");
        arrayList.add("4.ttf");
        arrayList.add("5.ttf");
        arrayList.add("6.ttf");
        arrayList.add("7.ttf");
        arrayList.add("8.ttf");
        arrayList.add("9.otf");
        arrayList.add("10.ttf");
        return arrayList;
    }
}
