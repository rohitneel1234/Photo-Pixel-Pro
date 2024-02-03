package com.rohitneel.photopixelpro.photocollage.assets;

import android.graphics.Bitmap;

import org.wysaid.common.SharedContext;
import org.wysaid.nativePort.CGEImageHandler;

import java.text.MessageFormat;

public class ColoredCodeAsset {

    public static Bitmap getColoredBitmap1(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust hue 2.0");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap2(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust hue -2.0");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap3(Bitmap bitmap, float value) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig(MessageFormat.format("@adjust hue {0}", new Object[]{(value / 2.0f) + ""}));
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap4(Bitmap bitmap, float value) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig(MessageFormat.format("@adjust hue {0}", new Object[]{(value / 2.0f) + ""}));
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap5(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust saturation 1.5");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap6(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust saturation 0");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap7(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust colorbalance 0.65 0.41 -0.49");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap8(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust level 0.23 0.56 1.96");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap9(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust hsl -0.66 0.34 0.15");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap10(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust hsv -1 -0.5 0.2 0.5 1 0.1");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap11(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust shadowhighlight -99 200");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap12(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust whitebalance -0.44 2.52");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap13(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust monochrome 2 0.5 -0.1 3 -2 -0.5");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap14(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@curve R(4, 35)(65, 82)(117, 148)(153, 208)(206, 255)G(13, 5)(74, 78)(109, 144)(156, 201)(250, 250)B(6, 37)(93, 104)(163, 184)(238, 222)(255, 237) @adjust hsv -0.2 -0.2 -0.44 -0.2 -0.2 -0.2");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap15(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@curve R(0, 0) (50, 25) (255, 255) G(0, 0) (100, 150) (255, 255) RGB(0, 0) (200, 150) (255, 255)");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap16(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@curve R(0, 0)(63, 101)(200, 84)(255, 255)G(0, 0)(86, 49)(180, 183)(255, 255)B(0, 0)(19, 17)(66, 41)(97, 92)(137, 156)(194, 211)(255, 255)RGB(0, 0)(82, 36)(160, 183)(255, 255)");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap17(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@adjust saturation 0 @curve R(0, 68)(10, 72)(42, 135)(72, 177)(98, 201)(220, 255)G(0, 29)(12, 30)(57, 127)(119, 203)(212, 255)(254, 239)B(0, 36)(54, 118)(66, 141)(119, 197)(155, 215)(255, 254)");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap18(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@beautify face 0.5 {0} 660");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

    public static Bitmap getColoredBitmap19(Bitmap bitmap) {
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        cgeImageHandler.setFilterWithConfig("@beautify face 1 {0} 660");
        cgeImageHandler.processFilters();
        Bitmap bitmap1 = cgeImageHandler.getResultBitmap();
        sharedContext.release();
        return bitmap1;
    }

}
