package com.rohitneel.photopixelpro.photocollage.photo;

import android.content.Context;
import android.util.AttributeSet;

public class PhotoSquareView extends PhotoGridView {
    public PhotoSquareView(Context context) {
        super(context);
    }

    public PhotoSquareView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PhotoSquareView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth > measuredHeight) {
            measuredWidth = measuredHeight;
        }
        setMeasuredDimension(measuredWidth, measuredWidth);
    }
}
