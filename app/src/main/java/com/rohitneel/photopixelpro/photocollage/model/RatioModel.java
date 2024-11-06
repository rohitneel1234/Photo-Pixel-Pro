package com.rohitneel.photopixelpro.photocollage.model;


import com.yalantis.ucrop.model.AspectRatio;

public class RatioModel extends AspectRatio {
    private int selectedIem;
    private int unselectItem;
    private String name;

    public RatioModel(int i, int i2, int i3, int i4, String name) {
        super("",i, i2);
        this.selectedIem = i4;
        this.unselectItem = i3;
        this.name = name;
    }

    public int getSelectedIem() {
        return this.selectedIem;
    }

    public int getUnselectItem() {
        return this.unselectItem;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
