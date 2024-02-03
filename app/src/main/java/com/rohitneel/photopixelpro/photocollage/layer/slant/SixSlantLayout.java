package com.rohitneel.photopixelpro.photocollage.layer.slant;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class SixSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 2;
    }

    public SixSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public SixSlantLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.7f, 0.7f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f, 0.5f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.3f, 0.3f);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.3f, 0.3f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.5f, 0.5f);
                addLine(4, PhotoLine.Direction.VERTICAL, 0.7f, 0.7f);
                return;
            default:
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new SixSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
