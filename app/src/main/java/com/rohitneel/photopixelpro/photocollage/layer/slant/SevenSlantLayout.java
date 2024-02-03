package com.rohitneel.photopixelpro.photocollage.layer.slant;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class SevenSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 2;
    }

    public SevenSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public SevenSlantLayout(int i) {
        super(i);
    }

    public void layout() {
        if (this.theme == 0) {
            addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
            addLine(1, PhotoLine.Direction.VERTICAL, 0.5f);
            addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f, 0.5f);
            addLine(1, PhotoLine.Direction.HORIZONTAL, 0.33f, 0.33f);
            addLine(3, PhotoLine.Direction.HORIZONTAL, 0.5f, 0.5f);
            addLine(2, PhotoLine.Direction.HORIZONTAL, 0.5f, 0.5f);
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new SevenSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
