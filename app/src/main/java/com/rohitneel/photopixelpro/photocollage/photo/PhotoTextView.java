package com.rohitneel.photopixelpro.photocollage.photo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.rohitneel.photopixelpro.photocollage.sticker.Sticker;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;

public class PhotoTextView extends Sticker {

    private PhotoText photoText;

    private int backgroundAlpha;

    private int backgroundBorder;

    private int backgroundColor;

    private BitmapDrawable backgroundDrawable;

    private final Context context;

    private Drawable drawable;

    private boolean isShowBackground;

    private float lineSpacingExtra = 0.0F;

    private float lineSpacingMultiplier = 1.0F;

    private float maxTextSizePixels;

    private float minTextSizePixels;

    private int paddingHeight;

    private int paddingWidth;

    private StaticLayout staticLayout;

    private String text;

    private Layout.Alignment textAlign;

    private int textAlpha;

    private int textColor;

    private int textHeight;

    private final TextPaint textPaint;

    private PhotoText.TextShadow textShadow;

    private int textWidth;


    public PhotoTextView(@NonNull Context paramContext, PhotoText paramAddTextProperties) {
        this.context = paramContext;
        this.photoText = paramAddTextProperties;
        this.textPaint = new TextPaint(1);
        PhotoTextView textSticker = setTextSize(paramAddTextProperties.getTextSize()).setTextWidth(paramAddTextProperties.getTextWidth()).setTextHeight(paramAddTextProperties.getTextHeight()).setText(paramAddTextProperties.getText()).setPaddingWidth(SystemUtil.dpToPx(paramContext, paramAddTextProperties.getPaddingWidth())).setBackgroundBorder(SystemUtil.dpToPx(paramContext, paramAddTextProperties.getBackgroundBorder())).setTextShadow(paramAddTextProperties.getTextShadow()).setTextColor(paramAddTextProperties.getTextColor()).setTextAlpha(paramAddTextProperties.getTextAlpha()).setBackgroundColor(paramAddTextProperties.getBackgroundColor()).setBackgroundAlpha(paramAddTextProperties.getBackgroundAlpha()).setShowBackground(paramAddTextProperties.isShowBackground()).setTextColor(paramAddTextProperties.getTextColor());
        AssetManager assetManager = paramContext.getAssets();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("fonts/");
        stringBuilder.append(paramAddTextProperties.getFontName());
        textSticker.setTypeface(Typeface.createFromAsset(assetManager, stringBuilder.toString())).setTextAlign(paramAddTextProperties.getTextAlign()).setTextShare(paramAddTextProperties.getTextShader()).resizeText();
    }

    private float convertSpToPx(float paramFloat) {
        return paramFloat * (this.context.getResources().getDisplayMetrics()).scaledDensity;
    }

    public void draw(@NonNull Canvas paramCanvas) {
        Matrix matrix = getMatrix();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        if (this.isShowBackground) {
            Paint paint = new Paint();
            if (this.backgroundDrawable != null) {
                paint.setShader(new BitmapShader(this.backgroundDrawable.getBitmap(), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
                paint.setAlpha(this.backgroundAlpha);
            } else {
                paint.setARGB(this.backgroundAlpha, Color.red(this.backgroundColor), Color.green(this.backgroundColor), Color.blue(this.backgroundColor));
            }
            paramCanvas.drawRoundRect(0.0F, 0.0F, this.textWidth, this.textHeight, this.backgroundBorder, this.backgroundBorder, paint);
            paramCanvas.restore();
            paramCanvas.save();
            paramCanvas.concat(matrix);
        }
        paramCanvas.restore();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        int i = this.paddingWidth;
        int j = this.textHeight / 2;
        int k = this.staticLayout.getHeight() / 2;
        paramCanvas.translate(i, (j - k));
        this.staticLayout.draw(paramCanvas);
        paramCanvas.restore();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        paramCanvas.restore();
    }

    public PhotoText getPhotoText() {
        return this.photoText;
    }

    public int getAlpha() {
        return this.textPaint.getAlpha();
    }


    @NonNull
    public Drawable getDrawable() {
        return this.drawable;
    }

    public int getHeight() {
        return this.textHeight;
    }


    @Nullable
    public String getText() {
        return this.text;
    }

    public int getWidth() {
        return this.textWidth;
    }

    public void release() {
        super.release();
        if (this.drawable != null)
            this.drawable = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    public PhotoTextView resizeText() {

        String text2 = getText();
        if (text2 == null || text2.length() <= 0) {
            return this;
        }
        if (this.textShadow != null) {
            this.textPaint.setShadowLayer((float) this.textShadow.getRadius(), (float) this.textShadow.getDx(), (float) this.textShadow.getDy(), this.textShadow.getColorShadow());
        }
        this.textPaint.setTextAlign(Paint.Align.LEFT);
        this.textPaint.setARGB(this.textAlpha, Color.red(this.textColor), Color.green(this.textColor), Color.blue(this.textColor));
        int i = this.textWidth - (this.paddingWidth * 2);
        this.staticLayout = new StaticLayout(this.text, this.textPaint, i <= 0 ? 100 : i, this.textAlign, this.lineSpacingMultiplier, this.lineSpacingExtra, true);
        return this;
    }

    @NonNull
    public PhotoTextView setAlpha(@IntRange(from = 0L, to = 255L) int paramInt) {
        this.textPaint.setAlpha(paramInt);
        return this;
    }

    public PhotoTextView setBackgroundAlpha(int paramInt) {
        this.backgroundAlpha = paramInt;
        return this;
    }

    public PhotoTextView setBackgroundBorder(int paramInt) {
        this.backgroundBorder = paramInt;
        return this;
    }

    public PhotoTextView setBackgroundColor(int paramInt) {
        this.backgroundColor = paramInt;
        return this;
    }


    public PhotoTextView setDrawable(@NonNull Drawable paramDrawable) {
        this.drawable = paramDrawable;
        return this;
    }


    public PhotoTextView setPaddingWidth(int paramInt) {
        this.paddingWidth = paramInt;
        return this;
    }


    public PhotoTextView setShowBackground(boolean paramBoolean) {
        this.isShowBackground = paramBoolean;
        return this;
    }

    @NonNull
    public PhotoTextView setText(@Nullable String paramString) {
        this.text = paramString;
        return this;
    }

    @NonNull
    public PhotoTextView setTextAlign(@NonNull int paramInt) {
        switch (paramInt) {
            case 2:
                this.textAlign = Layout.Alignment.ALIGN_NORMAL;
                break;
            case 3:
                this.textAlign = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case 4:
                this.textAlign = Layout.Alignment.ALIGN_CENTER;
                break;
        }
        return this;
    }

    public PhotoTextView setTextAlpha(int paramInt) {
        this.textAlpha = paramInt;
        return this;
    }

    @NonNull
    public PhotoTextView setTextColor(@ColorInt int paramInt) {
        this.textColor = paramInt;
        return this;
    }

    public PhotoTextView setTextHeight(int paramInt) {
        this.textHeight = paramInt;
        return this;
    }

    public PhotoTextView setTextShadow(PhotoText.TextShadow paramTextShadow) {
        this.textShadow = paramTextShadow;
        return this;
    }

    @NonNull
    public PhotoTextView setTextShare(@Nullable Shader paramShader) {
        this.textPaint.setShader(paramShader);
        return this;
    }

    @NonNull
    public PhotoTextView setTextSize(int paramInt) {
        this.textPaint.setTextSize(convertSpToPx(paramInt));
        return this;
    }

    public PhotoTextView setTextWidth(int paramInt) {
        this.textWidth = paramInt;
        return this;
    }

    @NonNull
    public PhotoTextView setTypeface(@Nullable Typeface paramTypeface) {
        this.textPaint.setTypeface(paramTypeface);
        return this;
    }
}
