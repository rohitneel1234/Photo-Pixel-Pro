package com.rohitneel.photopixelpro.photocollage.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.interfac.OnListAlbum;
import com.rohitneel.photopixelpro.photocollage.model.ImageModel;

import java.util.ArrayList;

public class ListAlbumAdapter extends ArrayAdapter<ImageModel> {
    Context context;
    ArrayList<ImageModel> data;
    int layoutResourceId;
    OnListAlbum onListAlbum;
    int pHeightItem = 0;

    static class RecordHolder {
        ImageView click;
        ImageView imageItem;
        RelativeLayout layoutRoot;

        RecordHolder() {
        }
    }

    public ListAlbumAdapter(Context context2, int i, ArrayList<ImageModel> arrayList) {
        super(context2, i, arrayList);
        this.layoutResourceId = i;
        this.context = context2;
        this.data = arrayList;
        this.pHeightItem = getDisplayInfo((Activity) context2).widthPixels / 3;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordHolder recordHolder;
        if (view == null) {
            view = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, viewGroup, false);
            recordHolder = new RecordHolder();
            recordHolder.imageItem = view.findViewById(R.id.imageItem);
            recordHolder.click = view.findViewById(R.id.click);
            recordHolder.layoutRoot = view.findViewById(R.id.layoutRoot);
            recordHolder.layoutRoot.getLayoutParams().height = this.pHeightItem;
            recordHolder.imageItem.getLayoutParams().width = this.pHeightItem;
            recordHolder.imageItem.getLayoutParams().height = this.pHeightItem;
            recordHolder.click.getLayoutParams().width = this.pHeightItem;
            recordHolder.click.getLayoutParams().height = this.pHeightItem;
            view.setTag(recordHolder);
        } else {
            recordHolder = (RecordHolder) view.getTag();
        }
        final ImageModel imageModel = this.data.get(i);
        ((RequestBuilder) Glide.with(this.context).load(imageModel.getPathFile()).placeholder((int) R.drawable.image_show)).into(recordHolder.imageItem);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ListAlbumAdapter.this.onListAlbum != null) {
                    ListAlbumAdapter.this.onListAlbum.OnItemListAlbumClick(imageModel);
                }
            }
        });
        return view;
    }


    public void setOnListAlbum(OnListAlbum onListAlbum2) {
        this.onListAlbum = onListAlbum2;
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
}
