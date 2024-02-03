package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.layout.PixLabLayout;
import com.rohitneel.photopixelpro.photocollage.listener.LayoutItemListener;

import java.util.ArrayList;

public class PixLabAdapters extends RecyclerView.Adapter<PixLabAdapters.ViewHolder> {
    public LayoutItemListener clickListener;
    public int selectedPos = 0;
    private ArrayList<String> pixLabItemList = new ArrayList<>();
    Context mContext ;

    public PixLabAdapters(Context context) {
        mContext = context;
    }

    public void addData(ArrayList<String> arrayList) {
        this.pixLabItemList.clear();
        this.pixLabItemList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_drip, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mSelectedBorder.setVisibility(position == selectedPos ? View.VISIBLE : View.GONE);
        String sb2 = "file:///android_asset/lab/" + pixLabItemList.get(position) +".webp";
        Glide.with(mContext)
                .load(sb2)
                .fitCenter()
                .into(holder.mIvImage);
    }

    public int getItemCount() {
        return this.pixLabItemList.size();
    }

    public void setClickListener(PixLabLayout clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<String> getItemList() {
        return pixLabItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        View mSelectedBorder;
        ImageView mIvImage;

        ViewHolder(View view) {
            super(view);
            mIvImage = view.findViewById(R.id.imageViewItem);
            mSelectedBorder = view.findViewById(R.id.selectedBorder);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int position = selectedPos;
            selectedPos = getAdapterPosition();
            notifyItemChanged(position);
            notifyItemChanged(selectedPos);
            clickListener.onLayoutListClick(view, getAdapterPosition());
        }
    }
}
