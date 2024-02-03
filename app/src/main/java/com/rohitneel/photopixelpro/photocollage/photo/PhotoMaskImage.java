package com.rohitneel.photopixelpro.photocollage.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.rohitneel.photopixelpro.R;

@SuppressLint("AppCompatCustomView")
public class PhotoMaskImage extends ImageView {
    int mBackgroundSource;
    RuntimeException mException;
    int mImageSource;
    int mMaskSource;

    @SuppressLint("ResourceType")
    public PhotoMaskImage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        final int[] MaskImage = {R.attr.frame, R.attr.image, R.attr.mask};

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, MaskImage, 0, 0);
        this.mImageSource = obtainStyledAttributes.getResourceId(1, 0);
        this.mMaskSource = obtainStyledAttributes.getResourceId(2, 0);
        this.mBackgroundSource = obtainStyledAttributes.getResourceId(0, 0);
        if (this.mImageSource == 0 || this.mMaskSource == 0 || this.mBackgroundSource == 0) {
            this.mException = new IllegalArgumentException(obtainStyledAttributes.getPositionDescription() + ": The content attribute is required and must refer to a valid image.");
        }
        if (this.mException == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), this.mImageSource);
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), this.mMaskSource);
            Bitmap createBitmap = Bitmap.createBitmap(decodeResource.getWidth(), decodeResource.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint(1);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas.drawBitmap(decodeResource, (Rect) null, new RectF(200.0f, 200.0f, 800.0f, 800.0f), paint);
            paint.setXfermode(null);
            setImageBitmap(createBitmap);
            setScaleType(ImageView.ScaleType.FIT_XY);
            setBackgroundResource(this.mBackgroundSource);
            obtainStyledAttributes.recycle();
            return;
        }
        throw this.mException;
    }
}
