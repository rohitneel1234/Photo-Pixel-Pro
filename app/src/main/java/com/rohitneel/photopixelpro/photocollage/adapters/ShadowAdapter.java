package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoText;

import java.util.List;

public class ShadowAdapter extends RecyclerView.Adapter<ShadowAdapter.ViewHolder> {
    private Context context;
    private List<PhotoText.TextShadow> lstTextShadows;
    public ShadowItemClickListener mClickListener;
    private LayoutInflater mInflater;
    public int selectedItem = 0;

    public interface ShadowItemClickListener {
        void onShadowItemClick(View view, int i);
    }

    public int getItemViewType(int i) {
        return i;
    }

    public ShadowAdapter(Context context2, List<PhotoText.TextShadow> list) {
        this.mInflater = LayoutInflater.from(context2);
        this.context = context2;
        this.lstTextShadows = list;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.item_font, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        int i2;
        PhotoText.TextShadow textShadow = this.lstTextShadows.get(i);
        viewHolder.textShadow.setShadowLayer((float) textShadow.getRadius(), (float) textShadow.getDx(), (float) textShadow.getDy(), textShadow.getColorShadow());
        ConstraintLayout constraintLayout = viewHolder.wrapperFontItems;
        if (this.selectedItem != i) {
            i2 = R.drawable.border_view;
        } else {
            i2 = R.drawable.border_black_view;
        }
        constraintLayout.setBackground(ContextCompat.getDrawable(context, i2));
    }

    public int getItemCount() {
        return this.lstTextShadows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textShadow;
        ConstraintLayout wrapperFontItems;

        ViewHolder(View view) {
            super(view);
            this.textShadow = view.findViewById(R.id.text_view_font_item);
            this.wrapperFontItems = view.findViewById(R.id.constraint_layout_wrapper_font_item);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (ShadowAdapter.this.mClickListener != null) {
                ShadowAdapter.this.mClickListener.onShadowItemClick(view, getAdapterPosition());
            }
            ShadowAdapter.this.selectedItem = getAdapterPosition();
            ShadowAdapter.this.notifyDataSetChanged();
        }
    }

    public void setSelectedItem(int i) {
        this.selectedItem = i;
    }

    public void setClickListener(ShadowItemClickListener shadowItemClickListener) {
        this.mClickListener = shadowItemClickListener;
    }
}
