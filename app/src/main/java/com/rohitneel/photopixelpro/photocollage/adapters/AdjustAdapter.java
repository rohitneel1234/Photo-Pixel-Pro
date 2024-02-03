package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.listener.AdjustListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {
    public String ADJUST = "@adjust brightness {0} @adjust contrast {1} " +
            "@adjust saturation {2} @vignette {3} 0.7 @adjust sharpen {4} 1" +
            " @adjust whitebalance {5} 1 @adjust hue {6} 1 @adjust exposure {7} 1";
    public AdjustListener adjustListener;
    private Context context;
    public List<AdjustModel> adjustModelList;
    public int selectedIndex = 0;

    public AdjustAdapter(Context context, AdjustListener adjustListener) {
        this.context = context;
        this.adjustListener = adjustListener;
        init();
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adjust, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.text_view_adjust_name.setText(this.adjustModelList.get(i).name);
        viewHolder.image_view_adjust_icon.setImageDrawable(this.selectedIndex != i ? this.adjustModelList.get(i).icon : this.adjustModelList.get(i).icon);
        if (this.selectedIndex == i) {
            viewHolder.text_view_adjust_name.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
            viewHolder.image_view_adjust_icon.setColorFilter(ContextCompat.getColor(context, R.color.mainColor));

        } else {
            viewHolder.text_view_adjust_name.setTextColor(ContextCompat.getColor(context, R.color.white));
            viewHolder.image_view_adjust_icon.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }
    }

    public int getItemCount() {
        return this.adjustModelList.size();
    }

    public String getFilterConfig() {
        String adjust = this.ADJUST;
        return MessageFormat.format(adjust,
                this.adjustModelList.get(0).originValue + "",
                this.adjustModelList.get(1).originValue + "",
                this.adjustModelList.get(2).originValue + "",
                this.adjustModelList.get(3).originValue + "",
                this.adjustModelList.get(4).originValue + "",
                this.adjustModelList.get(5).originValue + "",
                this.adjustModelList.get(6).originValue + "",
                Float.valueOf(this.adjustModelList.get(7).originValue));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_adjust_icon;
        TextView text_view_adjust_name;

        ViewHolder(View view) {
            super(view);
            this.image_view_adjust_icon = (ImageView) view.findViewById(R.id.image_view_adjust_icon);
            this.text_view_adjust_name = (TextView) view.findViewById(R.id.text_view_adjust_name);
            view.setOnClickListener((View.OnClickListener) view1 -> {
                int unused = AdjustAdapter.this.selectedIndex = ViewHolder.this.getLayoutPosition();
                AdjustAdapter.this.adjustListener.onAdjustSelected((AdjustModel) AdjustAdapter.this.adjustModelList.get(AdjustAdapter.this.selectedIndex));
                AdjustAdapter.this.notifyDataSetChanged();
            });
        }
    }

    public AdjustModel getCurrentAdjustModel() {
        return this.adjustModelList.get(this.selectedIndex);
    }

    private void init() {
        this.adjustModelList = new ArrayList();
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.brightness), "brightness", this.context.getDrawable(R.drawable.ic_brightness), -1.0f, 0.0f, 1.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.contrast), "contrast", this.context.getDrawable(R.drawable.ic_contrast), 0.5f, 1.0f, 1.5f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.saturation), "saturation", this.context.getDrawable(R.drawable.ic_saturation), 0.0f, 1.0f, 2.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.vignette), "vignette", this.context.getDrawable(R.drawable.ic_vignette), 0.0f, 0.6f, 0.6f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.sharpen), "sharpen", this.context.getDrawable(R.drawable.ic_sharpen), 0.0f, 0.0f, 10.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.whitebalance), "whitebalance", this.context.getDrawable(R.drawable.ic_white_balance), -1.0f, 0.0f, 1.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.hue), "hue", this.context.getDrawable(R.drawable.ic_hue), -2.0f, 0.0f, 2.0f));
        this.adjustModelList.add(new AdjustModel(this.context.getString(R.string.exposure), "exposure", this.context.getDrawable(R.drawable.ic_exposure), -2.0f, 0.0f, 2.0f));
    }

    public class AdjustModel {
        String code;
        Drawable icon;
        public float maxValue;
        public float minValue;
        String name;
        public float originValue;

        AdjustModel(String name, String code, Drawable icon, float minValue, float originalValue, float maxValue) {
            this.name = name;
            this.code = code;
            this.icon = icon;
            this.minValue = minValue;
            this.originValue = originalValue;
            this.maxValue = maxValue;
        }
    }
}
