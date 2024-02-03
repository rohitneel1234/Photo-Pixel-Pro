package com.rohitneel.photopixelpro.photocollage.assets;

import android.graphics.Bitmap;

import org.wysaid.common.SharedContext;
import org.wysaid.nativePort.CGEImageHandler;

import java.util.ArrayList;
import java.util.List;

public class OverlayFileAsset {

    public static class OverlayCode {
        private String image;

        OverlayCode(String image) {
            this.image = image;
        }

        public String getImage() {
            return this.image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

    public static final OverlayCode[] DODGE_EFFECTS = {
            new OverlayCode(""),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_1.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_2.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_3.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_4.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_5.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_6.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_7.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_8.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_9.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_10.webp 100")
            /*new OverlayCode("#unpack @krblend sr overlay/burn/burn_11.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_12.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_13.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_14.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_15.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_16.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_17.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_18.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_19.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_20.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_21.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_22.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_23.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_24.webp 100"),
            new OverlayCode("#unpack @krblend sr overlay/burn/burn_25.webp 100")*/
    };

    public static final OverlayCode[] OVERLAY_EFFECTS = {
            new OverlayCode(""),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_1.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_2.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_3.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_4.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_5.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_6.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_7.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_8.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_9.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_10.jpg 100")
            /*new OverlayCode("#unpack @krblend sr overlay/blend/blend_11.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_12.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_13.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_14.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_15.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_16.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_17.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_18.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_19.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_20.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_21.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_22.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_23.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_24.jpg 100"),
            new OverlayCode("#unpack @krblend sr overlay/blend/blend_25.jpg 100")*/
    };

    public static final OverlayCode[] COLOR_EFFECTS = {
            new OverlayCode(""),
            new OverlayCode("#unpack @krblend hue overlay/color/color_1.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_2.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_3.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_4.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_5.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_6.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_7.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_8.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_9.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_10.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_11.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_12.webp 100")
            /*new OverlayCode("#unpack @krblend hue overlay/color/color_13.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_14.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_15.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_16.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_17.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_18.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_19.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/color/color_20.webp 100")*/

    };

    public static final OverlayCode[] HARDMIX_EFFECTS = {
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_1.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_2.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_3.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_4.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_5.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_6.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_7.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_8.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_9.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_10.webp 100")
            /*new OverlayCode("#unpack @krblend hm overlay/burn/burn_11.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_12.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_13.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_14.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_15.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_16.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_17.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_18.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_19.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_20.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_21.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_22.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_23.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_24.webp 100"),
            new OverlayCode("#unpack @krblend hm overlay/burn/burn_25.webp 100")*/
    };

    public static final OverlayCode[] HUE_EFFECTS = {
            new OverlayCode(""),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_1.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_2.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_3.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_4.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_5.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_6.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_7.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_8.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_9.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_10.webp 100")
          /*  new OverlayCode("#unpack @krblend hue overlay/hue/light_11.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_12.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_13.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_14.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_15.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_16.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_17.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_18.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_19.webp 100"),
            new OverlayCode("#unpack @krblend hue overlay/hue/light_20.webp 100")*/
    };

    public static final OverlayCode[] BURN_EFFECTS = {
            new OverlayCode(""),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_1.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_2.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_3.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_4.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_5.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_6.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_7.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_8.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_9.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_10.webp 100")
            /*new OverlayCode("#unpack @krblend cl overlay/burn/burn_11.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_12.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_13.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_14.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_15.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_16.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_17.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_18.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_19.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_20.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_21.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_22.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_23.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_24.webp 100"),
            new OverlayCode("#unpack @krblend cl overlay/burn/burn_25.webp 100")*/
    };

    public static List<Bitmap> getListBitmapDodgeEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : DODGE_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }


    public static List<Bitmap> getListBitmapOverlayEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : OVERLAY_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }

    public static List<Bitmap> getListBitmapHardmixEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : HARDMIX_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }
    public static List<Bitmap> getListBitmapHueEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : HUE_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }

    public static List<Bitmap> getListBitmapColorEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : COLOR_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }

    public static List<Bitmap> getListBitmapBurnEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : BURN_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }
}
