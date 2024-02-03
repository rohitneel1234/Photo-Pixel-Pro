package com.rohitneel.photopixelpro.photocollage.layer.slant;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class FourSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 6;
    }

    public FourSlantLayout(int i) {
        super(i);
    }

    public FourSlantLayout(PhotoLayout puzzleLayout, boolean z) {
        super((SlantCollageLayout) puzzleLayout, z);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.3f, 0.3f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.7f, 0.7f);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.3f, 0.3f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.7f, 0.7f);
                return;
            case 2:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.6666667f, 0.6666667f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.6666667f, 0.6666667f);
                return;
            case 3:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f, 0.33333334f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.33333334f, 0.33333334f);
                return;
            case 4:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.5f, 0.5f);
                return;
            case 5:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f, 0.5f);
                return;
            case 6:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.7f, 0.3f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.3f, 0.5f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.5f, 0.7f);
                return;
            default:
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new FourSlantLayout(quShotLayout, true);
    }
}
