package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.assets.BrushColorAsset;

import java.util.ArrayList;
import java.util.List;

public class CollageBackgroundAdapter extends RecyclerView.Adapter<CollageBackgroundAdapter.ViewHolder> {
    public BackgroundGridListener backgroundListener;
    private Context context;
    public int selectedIndex;
    public List<SquareView> squareViewList = new ArrayList();

    public interface BackgroundGridListener {
        void onBackgroundSelected(int i,SquareView squareView);
    }

    public CollageBackgroundAdapter(Context context2, BackgroundGridListener backgroundChangeListener2, boolean z) {
        this.context = context2;
        this.backgroundListener = backgroundChangeListener2;
        this.squareViewList.add(new SquareView(R.drawable.gradient_1, "Gradient_1"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_2, "Gradient_2"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_3, "Gradient_3"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_4, "Gradient_4"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_5, "Gradient_5"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_6, "Gradient_6"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_7, "Gradient_7"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_8, "Gradient_8"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_9, "Gradient_9"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_10, "Gradient_10"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_11, "Gradient_11"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_12, "Gradient_12"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_13, "Gradient_13"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_14, "Gradient_14"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_15, "Gradient_15"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_16, "Gradient_16"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_17, "Gradient_17"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_18, "Gradient_18"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_19, "Gradient_19"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_20, "Gradient_20"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_21, "Gradient_21"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_22, "Gradient_22"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_23, "Gradient_23"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_24, "Gradient_24"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_25, "Gradient_25"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_26, "Gradient_26"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_27, "Gradient_27"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_28, "Gradient_28"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_29, "Gradient_29"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_30, "Gradient_30"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_31, "Gradient_31"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_32, "Gradient_32"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_33, "Gradient_33"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_34, "Gradient_34"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_35, "Gradient_35"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_36, "Gradient_36"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_37, "Gradient_37"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_38, "Gradient_38"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_39, "Gradient_39"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_40, "Gradient_40"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_41, "Gradient_41"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_42, "Gradient_42"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_43, "Gradient_43"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_44, "Gradient_44"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_45, "Gradient_45"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_46, "Gradient_46"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_47, "Gradient_47"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_48, "Gradient_48"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_49, "Gradient_49"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_50, "Gradient_50"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_51, "Gradient_51"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_52, "Gradient_52"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_53, "Gradient_53"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_54, "Gradient_54"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_55, "Gradient_55"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_56, "Gradient_56"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_57, "Gradient_57"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_58, "Gradient_58"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_59, "Gradient_59"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_60, "Gradient_60"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_61, "Gradient_61"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_62, "Gradient_62"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_63, "Gradient_63"));
        this.squareViewList.add(new SquareView(R.drawable.gradient_64, "Gradient_64"));
    }

    public CollageBackgroundAdapter(Context context2, BackgroundGridListener backgroundChangeListener2, List<Drawable> list) {
        this.context = context2;
        this.backgroundListener = backgroundChangeListener2;
        for (Drawable squareView : list) {
            this.squareViewList.add(new SquareView(1, "", false, true, squareView));
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_background, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
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

        public ImageView imageViewSquare;
        public View squareView,lockPro;
        View viewSelected;
        public ConstraintLayout constraint_layout_wrapper_square_view;

        public ViewHolder(View view) {
            super(view);
            this.squareView = view.findViewById(R.id.square_view);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.constraint_layout_wrapper_square_view = view.findViewById(R.id.constraint_layout_wrapper_square_view);
            this.imageViewSquare = view.findViewById(R.id.image_view_square);
            this.imageViewSquare.setVisibility(View.GONE);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            CollageBackgroundAdapter.this.selectedIndex = getAdapterPosition();
            CollageBackgroundAdapter.this.backgroundListener.onBackgroundSelected(selectedIndex,(SquareView) CollageBackgroundAdapter.this.squareViewList.get(CollageBackgroundAdapter.this.selectedIndex));
            CollageBackgroundAdapter.this.notifyDataSetChanged();
        }
    }

    public static class SquareView {
        public Drawable drawable;
        public int drawableId;
        public boolean isBitmap;
        public boolean isColor;
        public String text;

        SquareView(int i, String str) {
            this.drawableId = i;
            this.text = str;
        }

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
