package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.assets.OverlayFileAsset;
import com.rohitneel.photopixelpro.photocollage.listener.OverlayListener;

import java.util.List;

public class OverlayAdapter extends RecyclerView.Adapter<OverlayAdapter.ViewHolder> {
    private List<Bitmap> bitmaps;
    private Context context;
    public List<OverlayFileAsset.OverlayCode> effectsCodeList;
    public OverlayListener hardmixListener;
    public int selectIndex = 0;

    public OverlayAdapter(List<Bitmap> bitmapList, OverlayListener hardmixListener, Context mContext, List<OverlayFileAsset.OverlayCode> effectsCodeList) {
        this.hardmixListener = hardmixListener;
        this.bitmaps = bitmapList;
        this.context = mContext;
        this.effectsCodeList = effectsCodeList;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_overlay, viewGroup, false));
    }

    public void reset() {
        this.selectIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.round_image_view_filter_item.setImageBitmap(this.bitmaps.get(i));
        if (this.selectIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.viewSelected.setVisibility(View.GONE);

    }

    public int getItemCount() {
        return this.bitmaps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView round_image_view_filter_item;
        View viewSelected , lockPro;

        ViewHolder(View view) {
            super(view);
            this.round_image_view_filter_item = view.findViewById(R.id.round_image_view_filter_item);
            this.viewSelected = view.findViewById(R.id.view_selected);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OverlayAdapter.this.selectIndex = ViewHolder.this.getLayoutPosition();
                    OverlayAdapter.this.hardmixListener.onOverlaySelected(selectIndex,((OverlayFileAsset.OverlayCode) OverlayAdapter.this.effectsCodeList.get(OverlayAdapter.this.selectIndex)).getImage());                    OverlayAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }
}
