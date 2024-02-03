package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.constants.Constants;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class ColoredAdapter extends RecyclerView.Adapter<ColoredAdapter.ViewHolder> {
    private int borderWidth;
    private Context context;
    public ColoredChangeListener mosaicChangeListener;
    public List<ColoredItems> coloredItems = new ArrayList();

    public int selectedSquareIndex;

    public enum COLORED {
        COLOR_1,
        COLOR_2,
        COLOR_3,
        COLOR_4,
        COLOR_5,
        COLOR_6,
        COLOR_7,
        COLOR_8,
        COLOR_9,
        COLOR_10,
        COLOR_11,
        COLOR_12,
        COLOR_13,
        COLOR_14,
        COLOR_15,
        COLOR_16,
        COLOR_17,
        COLOR_18,
        COLOR_19,
        SHADER
    }

    public interface ColoredChangeListener {
        void onSelected(ColoredItems mosaicItem);
    }

    public ColoredAdapter(Context context2, ColoredChangeListener coloredChangeListener) {
        this.context = context2;
        this.mosaicChangeListener = coloredChangeListener;
        this.borderWidth = SystemUtil.dpToPx(context2, Constants.BORDER_WIDTH);
        this.coloredItems.add(new ColoredItems(R.drawable.colored_1, 0, COLORED.COLOR_1));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_2, 0, COLORED.COLOR_2));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_3, 0, COLORED.COLOR_3));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_4, 0, COLORED.COLOR_4));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_5, 0, COLORED.COLOR_5));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_6, 0, COLORED.COLOR_6));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_7, 0, COLORED.COLOR_7));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_8, 0, COLORED.COLOR_8));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_9, 0, COLORED.COLOR_9));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_10, 0, COLORED.COLOR_10));
       /* this.coloredItems.add(new ColoredItems(R.drawable.colored_11, 0, COLORED.COLOR_11));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_12, 0, COLORED.COLOR_12));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_13, 0, COLORED.COLOR_13));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_14, 0, COLORED.COLOR_14));*/
        /*this.coloredItems.add(new ColoredItems(R.drawable.colored_15, 0, COLORED.COLOR_15));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_15, 0, COLORED.COLOR_16));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_15, 0, COLORED.COLOR_17));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_15, 0, COLORED.COLOR_18));
        this.coloredItems.add(new ColoredItems(R.drawable.colored_15, 0, COLORED.COLOR_19));*/

    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_colored, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(this.context).load(Integer.valueOf(this.coloredItems.get(i).frameId)).into(viewHolder.mosaic);
        if (this.selectedSquareIndex == i) {
            viewHolder.mosaic.setBorderColor(ContextCompat.getColor(context, R.color.mainColor));
            viewHolder.mosaic.setBorderWidth(this.borderWidth);
            return;
        }
        viewHolder.mosaic.setBorderColor(0);
        viewHolder.mosaic.setBorderWidth(this.borderWidth);
    }

    public int getItemCount() {
        return this.coloredItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedImageView mosaic;

        public ViewHolder(View view) {
            super(view);
            this.mosaic = view.findViewById(R.id.round_image_view_mosaic_item);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            ColoredAdapter.this.selectedSquareIndex = getAdapterPosition();
            if (ColoredAdapter.this.selectedSquareIndex < ColoredAdapter.this.coloredItems.size()) {
                ColoredAdapter.this.mosaicChangeListener.onSelected((ColoredItems) ColoredAdapter.this.coloredItems.get(ColoredAdapter.this.selectedSquareIndex));
            }
            ColoredAdapter.this.notifyDataSetChanged();
        }
    }

    public static class ColoredItems {
        int frameId;
        public COLORED mode;
        public int shaderId;

        public ColoredItems(int i, int i2, COLORED mode2) {
            this.frameId = i;
            this.mode = mode2;
            this.shaderId = i2;
        }
    }
}
