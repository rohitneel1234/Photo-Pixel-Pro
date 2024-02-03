package com.rohitneel.photopixelpro.photocollage.layer.straight;

import android.util.Log;

public abstract class NumberStraightLayout extends StraightCollageLayout {
    static final String TAG = "NumberStraightLayout";
    protected int theme;

    public abstract int getThemeCount();

    public NumberStraightLayout() {
    }

    public NumberStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public NumberStraightLayout(int i) {
        if (i >= getThemeCount()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NumberStraightLayout: the most theme count is ");
            stringBuilder.append(getThemeCount());
            stringBuilder.append(" ,you should let theme from 0 to ");
            stringBuilder.append(getThemeCount() - 1);
            stringBuilder.append(" .");
            Log.e(TAG, stringBuilder.toString());
        }
        this.theme = i;
    }

    public int getTheme() {
        return this.theme;
    }
}
