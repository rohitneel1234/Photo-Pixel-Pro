package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.assets.BrushColorAsset;

import java.util.ArrayList;
import java.util.List;

public class CollageColorAdapter extends RecyclerView.Adapter<com.rohitneel.photopixelpro.photocollage.adapters.CollageColorAdapter.ViewHolder> {
    public BackgroundColorListener backgroundListener;
    private Context context;
    public int selectedIndex;
    public List<SquareView> squareViewList = new ArrayList();

    public interface BackgroundColorListener {
        void onBackgroundColorSelected(int i, SquareView squareView);
    }

    public CollageColorAdapter(Context context2, BackgroundColorListener backgroundListener2) {
        this.context = context2;
        this.backgroundListener = backgroundListener2;
        List<String> lstColorForBrush = BrushColorAsset.listColorBrush();
        for (int i = 0; i < lstColorForBrush.size() - 2; i++) {
            this.squareViewList.add(new SquareView(Color.parseColor(lstColorForBrush.get(i)), "", true));
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_background, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SquareView squareView = this.squareViewList.get(i);
        if (squareView.isColor) {
            viewHolder.squareView.setBackgroundColor(squareView.drawableId);
        } else if (squareView.drawable != null) {
            viewHolder.squareView.setVisibility(View.GONE);
            viewHolder.imageViewSquare.setVisibility(View.VISIBLE);
            viewHolder.imageViewSquare.setImageDrawable(squareView.drawable);
        } else {
            viewHolder.squareView.setBackgroundResource(squareView.drawableId);
        }
        if (this.selectedIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewSelected.setVisibility(View.GONE);
        }
    }

    public void setSelectedIndex(int i) {
        this.selectedIndex = i;
    }

    public int getItemCount() {
        return this.squareViewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout constraint_layout_wrapper_square_view;
        public ImageView imageViewSquare;
        public View lockPro;
        public View squareView;
        View viewSelected;

        public ViewHolder(View view) {
            super(view);
            this.squareView = view.findViewById(R.id.square_view);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.constraint_layout_wrapper_square_view = (ConstraintLayout) view.findViewById(R.id.constraint_layout_wrapper_square_view);
            this.imageViewSquare = (ImageView) view.findViewById(R.id.image_view_square);
            this.imageViewSquare.setVisibility(View.GONE);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            com.rohitneel.photopixelpro.photocollage.adapters.CollageColorAdapter.this.selectedIndex = getAdapterPosition();
            com.rohitneel.photopixelpro.photocollage.adapters.CollageColorAdapter.this.backgroundListener.onBackgroundColorSelected(com.rohitneel.photopixelpro.photocollage.adapters.CollageColorAdapter.this.selectedIndex, com.rohitneel.photopixelpro.photocollage.adapters.CollageColorAdapter.this.squareViewList.get(com.rohitneel.photopixelpro.photocollage.adapters.CollageColorAdapter.this.selectedIndex));
            com.rohitneel.photopixelpro.photocollage.adapters.CollageColorAdapter.this.notifyDataSetChanged();
        }
    }

    public static class SquareView {
        public Drawable drawable;
        public int drawableId;
        public boolean isBitmap;
        public boolean isColor;
        public String text;

        public SquareView(int i, String str, boolean z) {
            this.drawableId = i;
            this.text = str;
            this.isColor = z;
        }

        public SquareView(int i, String str, boolean z, boolean z2, Drawable drawable2) {
            this.drawableId = i;
            this.text = str;
            this.isColor = z;
            this.isBitmap = z2;
            this.drawable = drawable2;
        }
    }
}
