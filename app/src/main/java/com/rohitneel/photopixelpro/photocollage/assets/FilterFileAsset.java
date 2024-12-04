package com.rohitneel.photopixelpro.photocollage.assets;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;

public class FilterFileAsset {

    public static class FiltersCode {
        private String code;
        private String name;

        FiltersCode(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static final FiltersCode[] FILTERS = {
            new FiltersCode("", "none"),
            new FiltersCode("@adjust lut filter/bright_1.webp", "BG-1"),
            new FiltersCode("@adjust lut filter/bright_2.webp", "BG-2"),
            new FiltersCode("@adjust lut filter/bright_3.webp", "BG-3"),
            new FiltersCode("@adjust lut filter/bright_4.webp", "BG-4"),

            new FiltersCode("@adjust lut filter/color_1.webp", "CL-1"),
            new FiltersCode("@adjust lut filter/color_2.webp", "CL-2"),
            new FiltersCode("@adjust lut filter/chill_1.webp", "CL-3"),
            new FiltersCode("@adjust lut filter/code_1.webp", "CL-4"),

            new FiltersCode("@adjust lut filter/cube_1.webp", "CB-1"),
            new FiltersCode("@adjust lut filter/cube_2.webp", "CB-2"),
            new FiltersCode("@adjust lut filter/cube_3.webp", "CB-3"),
            new FiltersCode("@adjust lut filter/cube_4.webp", "CB-4"),
            new FiltersCode("@adjust lut filter/cube_5.webp", "CB-5"),
            new FiltersCode("@adjust lut filter/cube_6.webp", "CB-6"),
            new FiltersCode("@adjust lut filter/cube_7.webp", "CB-7"),
            new FiltersCode("@adjust lut filter/cube_8.webp", "CB-8"),
            new FiltersCode("@adjust lut filter/cube_9.webp", "CB-9"),

            ////
            new FiltersCode("@adjust lut filter/vintage_1.webp", "VT-1"),
            new FiltersCode("@adjust lut filter/vintage_2.webp", "VT-2"),

            ////
            new FiltersCode("@adjust lut filter/tone_1.webp", "TN-1"),
            new FiltersCode("@adjust lut filter/tone_2.webp", "TN-2"),
            new FiltersCode("@adjust lut filter/tone_3.webp", "TN-3"),
            new FiltersCode("@adjust lut filter/tone_4.webp", "TN-4"),
            new FiltersCode("@adjust lut filter/tone_5.webp", "TN-5"),
            new FiltersCode("@adjust lut filter/tone_6.webp", "TN-6"),
            new FiltersCode("@adjust lut filter/tone_7.webp", "TN-7"),
            new FiltersCode("@adjust lut filter/tone_8.webp", "TN-8"),

            ////
            new FiltersCode("@adjust lut filter/light_1.webp", "LM-7"),
            ///

    };

    public static List<Bitmap> getListBitmapFilter(Context context, Bitmap bitmap) {
        List<Bitmap> filteredBitmaps = new ArrayList<>();
        GPUImage gpuImage = new GPUImage(context);

        for (FiltersCode filtersCode : FILTERS) {
            try {
                String filterCode = filtersCode.getCode();
                AssetManager assetManager = context.getAssets();
                String assetPath = "filter/" + filterCode.replace("@adjust lut filter/", "");
                InputStream inputStream = assetManager.open(assetPath);
                Bitmap lutBitmap = BitmapFactory.decodeStream(inputStream);
                gpuImage.setImage(bitmap);

                GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
                lookupFilter.setBitmap(lutBitmap);
                gpuImage.setFilter(lookupFilter);

                Bitmap filteredBitmap = gpuImage.getBitmapWithFilterApplied();
                filteredBitmaps.add(filteredBitmap);

                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filteredBitmaps;
    }
}
