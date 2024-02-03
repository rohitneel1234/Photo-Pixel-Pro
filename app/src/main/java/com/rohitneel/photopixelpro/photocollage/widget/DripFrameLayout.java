package com.rohitneel.photopixelpro.photocollage.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import kotlin.jvm.internal.Intrinsics;

public final class DripFrameLayout extends FrameLayout {

    public DripFrameLayout(Context context) {
        super(context);
        Intrinsics.checkParameterIsNotNull(context, "context");
    }

    public DripFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(attributeSet, "attributeSet");
    }

    public final Bitmap createBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        draw(new Canvas(createBitmap));
        Intrinsics.checkExpressionValueIsNotNull(createBitmap, "bitmap");
        return createBitmap;
    }
}
