package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.constants.Constants;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class MosaicAdapter extends RecyclerView.Adapter<MosaicAdapter.ViewHolder> {
    private int borderWidth;
    private Context context;
    public MosaicChangeListener mosaicChangeListener;
    public List<MosaicItem> mosaicItems = new ArrayList();
    public int selectedSquareIndex;

    public enum BLUR {
        BLUR,
        MOSAIC,
        SHADER
    }

    public interface MosaicChangeListener {
        void onSelected(MosaicItem mosaicItem);
    }

    public MosaicAdapter(Context context2, MosaicChangeListener mosaicChangeListener2) {
        this.context = context2;
        this.mosaicChangeListener = mosaicChangeListener2;
        this.borderWidth = SystemUtil.dpToPx(context2, Constants.BORDER_WIDTH);
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_1, 0, BLUR.BLUR));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_2, 0, BLUR.MOSAIC));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_3, R.drawable.mos_3, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_4, R.drawable.mos_4, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_5, R.drawable.mos_5, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_6, R.drawable.mos_6, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_7, R.drawable.mos_7, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_8, R.drawable.mos_8, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_9, R.drawable.mos_9, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_10, R.drawable.mos_10, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_11, R.drawable.mos_11, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_12, R.drawable.mos_12, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_13, R.drawable.mos_13, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_14, R.drawable.mos_14, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_15, R.drawable.mos_15, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_16, R.drawable.mos_16, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_17, R.drawable.mos_17, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_19, R.drawable.mos_19, BLUR.SHADER));
        this.mosaicItems.add(new MosaicItem(R.drawable.mos_20, R.drawable.mos_20, BLUR.SHADER));

    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mosaic, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(this.context).load(Integer.valueOf(this.mosaicItems.get(i).frameId)).into(viewHolder.mosaic);
        if (this.selectedSquareIndex == i) {
            viewHolder.mosaic.setBorderColor(ContextCompat.getColor(context, R.color.mainColor));
            viewHolder.mosaic.setBorderWidth(this.borderWidth);
            return;
        }
        viewHolder.mosaic.setBorderColor(0);
        viewHolder.mosaic.setBorderWidth(this.borderWidth);
    }

    public int getItemCount() {
        return this.mosaicItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedImageView mosaic;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View view) {
            super(view);
            this.mosaic = view.findViewById(R.id.round_image_view_mosaic_item);
            this.constraintLayout = view.findViewById(R.id.constraintLayoutMosaic);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            MosaicAdapter.this.selectedSquareIndex = getAdapterPosition();
            if (MosaicAdapter.this.selectedSquareIndex < MosaicAdapter.this.mosaicItems.size()) {
                MosaicAdapter.this.mosaicChangeListener.onSelected((MosaicItem) MosaicAdapter.this.mosaicItems.get(MosaicAdapter.this.selectedSquareIndex));
            }
            MosaicAdapter.this.notifyDataSetChanged();
        }
    }

    public static class MosaicItem {
        int frameId;
        public BLUR mode;
        public int shaderId;

        public MosaicItem(int i, int i2, BLUR mode2) {
            this.frameId = i;
            this.mode = mode2;
            this.shaderId = i2;
        }
    }
}
