package com.rohitneel.photopixelpro.photocollage.crop;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.view.ViewCompat;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


import java.io.File;


public class DeeplabMobile implements DeeplabInterface {
    private static final String INPUT_NAME = "ImageTensor";
    private static final String MODEL_FILE = "file:///android_asset/eraser.pb";
    private static final String OUTPUT_NAME = "SemanticPredictions";
    private volatile TensorFlowInferenceInterface sTFInterface = null;

    public int getInputSize() {
        return 513;
    }

    public boolean initialize(Context context) {
        new File(MODEL_FILE);
        this.sTFInterface = new TensorFlowInferenceInterface(context.getAssets(), MODEL_FILE);
        return this.sTFInterface != null;
    }

    public boolean isInitialized() {
        return this.sTFInterface != null;
    }

    public Bitmap segment(Bitmap bitmap) {
        if (this.sTFInterface == null || bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > 513 || height > 513) {
            return null;
        }
        int i = width * height;
        int[] iArr = new int[i];
        byte[] bArr = new byte[(i * 3)];
        int[] iArr2 = new int[i];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = iArr[i2];
            int i4 = i2 * 3;
            bArr[i4 + 0] = (byte) ((i3 >> 16) & 255);
            bArr[i4 + 1] = (byte) ((i3 >> 8) & 255);
            bArr[i4 + 2] = (byte) (i3 & 255);
        }
        System.currentTimeMillis();
        this.sTFInterface.feed(INPUT_NAME, bArr, 1, (long) height, (long) width, 3);
        this.sTFInterface.run(new String[]{OUTPUT_NAME}, true);
        this.sTFInterface.fetch(OUTPUT_NAME, iArr2);
        System.currentTimeMillis();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i5 = 0; i5 < height; i5++) {
            for (int i6 = 0; i6 < width; i6++) {
                createBitmap.setPixel(i6, i5, iArr2[(i5 * width) + i6] == 0 ? 0 : ViewCompat.MEASURED_STATE_MASK);
            }
        }
        return createBitmap;
    }


}
