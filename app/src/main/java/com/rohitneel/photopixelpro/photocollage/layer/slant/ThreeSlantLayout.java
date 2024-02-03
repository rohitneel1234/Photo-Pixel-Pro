package com.rohitneel.photopixelpro.photocollage.layer.slant;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class ThreeSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 6;
    }

    public ThreeSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
        this.theme = ((NumberSlantLayout) slantPuzzleLayout).getTheme();
    }

    public ThreeSlantLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 2:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 3:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 4:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.44f, 0.56f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 5:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.56f, 0.44f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.44f, 0.56f);
                return;
            default:
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new ThreeSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
