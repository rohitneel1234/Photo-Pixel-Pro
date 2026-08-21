package com.rohitneel.photopixelpro.photocollage.crop;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.segmentation.Segmentation;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

public class DeeplabMobile {

    public int getInputSize() {
        return 513;
    }

    public Bitmap segment(Bitmap bitmap) {
        if (bitmap == null) return null;

        SelfieSegmenterOptions options =
                new SelfieSegmenterOptions.Builder()
                        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
                        .build();

        Segmenter segmenter = Segmentation.getClient(options);
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        try {
            com.google.mlkit.vision.segmentation.SegmentationMask mask = Tasks.await(segmenter.process(image));
            return generateMaskBitmap(mask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return createFallbackMask(bitmap.getWidth(), bitmap.getHeight());
        } finally {
            segmenter.close();
        }
    }

    private Bitmap generateMaskBitmap(com.google.mlkit.vision.segmentation.SegmentationMask mask) {
        int width = mask.getWidth();
        int height = mask.getHeight();
        ByteBuffer buffer = mask.getBuffer();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        boolean detected = false;

        for (int y = 0; width > 0 && y < height; y++) {
            for (int x = 0; x < width; x++) {
                float confidence = buffer.getFloat();
                if (confidence > 0.4) { // Lower threshold for better recall
                    bitmap.setPixel(x, y, Color.BLACK);
                    detected = true;
                } else {
                    bitmap.setPixel(x, y, Color.TRANSPARENT);
                }
            }
        }
        buffer.rewind();
        if (!detected) {
            return createFallbackMask(width, height);
        }
        return bitmap;
    }

    private Bitmap createFallbackMask(int width, int height) {
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        // Create an oval mask that covers the center area where a person is usually located
        RectF rectF = new RectF(width * 0.15f, height * 0.05f, width * 0.85f, height * 0.95f);
        canvas.drawOval(rectF, paint);

        return createBitmap;
    }
}

