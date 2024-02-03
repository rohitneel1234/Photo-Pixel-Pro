package com.rohitneel.photopixelpro.photocollage.layer.slant;

import android.util.Log;

public abstract class NumberSlantLayout extends SlantCollageLayout {
    static final String TAG = "NumberSlantLayout";
    protected int theme;

    public abstract int getThemeCount();

    public NumberSlantLayout() {
    }

    public NumberSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public NumberSlantLayout(int i) {
        if (i >= getThemeCount()) {
            StringBuilder sb = new StringBuilder();
            sb.append("NumberSlantLayout: the most theme count is ");
            sb.append(getThemeCount());
            sb.append(" ,you should let theme from 0 to ");
            sb.append(getThemeCount() - 1);
            sb.append(" .");
            Log.e(TAG, sb.toString());
        }
        this.theme = i;
    }

    public int getTheme() {
        return this.theme;
    }
}
