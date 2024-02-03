package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.assets.BrushColorAsset;
import com.rohitneel.photopixelpro.photocollage.listener.BrushColorListener;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    public BrushColorListener brushColorListener;
    public List<String> colors = BrushColorAsset.listColorBrush();
    private Context context;
    public int selectedColorIndex;

    public ColorAdapter(Context context2, BrushColorListener brushColorListener2) {
        this.context = context2;
        this.brushColorListener = brushColorListener2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_paint, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.squareView.setBackgroundColor(Color.parseColor(this.colors.get(i)));
        if (this.selectedColorIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewSelected.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.colors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View squareView;
        ImageView viewSelected;

        ViewHolder(View view) {
            super(view);
            this.squareView = view.findViewById(R.id.square_view);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.squareView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ColorAdapter.this.selectedColorIndex = ViewHolder.this.getLayoutPosition();
                    ColorAdapter.this.brushColorListener.onColorChanged(ColorAdapter.this.colors.get(ColorAdapter.this.selectedColorIndex));
                    ColorAdapter.this.notifyDataSetChanged();
                }
            });

        }
    }

    public void setSelectedColorIndex(int i) {
        this.selectedColorIndex = i;
    }
}
