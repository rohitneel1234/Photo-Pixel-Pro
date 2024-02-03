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
import com.rohitneel.photopixelpro.photocollage.layout.ArtLayout;
import com.rohitneel.photopixelpro.photocollage.listener.LayoutItemListener;

import java.util.ArrayList;

public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ViewHolder> {
    public LayoutItemListener clickListener;
    public int selectedPos = 0;
    private ArrayList<String> dripItemList = new ArrayList<>();
    Context mContext ;

    public ArtAdapter(Context context) {
        mContext = context;
    }

    public void addData(ArrayList<String> arrayList) {
        this.dripItemList.clear();
        this.dripItemList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_art, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mSelectedBorder.setVisibility(position == selectedPos ? View.VISIBLE : View.GONE);
        String sb2 = "file:///android_asset/art/icon/" + dripItemList.get(position) +".webp";
        Glide.with(mContext)
                .load(sb2)
                .fitCenter()
                .into(holder.imageViewItem);
    }

    public int getItemCount() {
        return this.dripItemList.size();
    }

    public void setClickListener(ArtLayout clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<String> getItemList() {
        return dripItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        View mSelectedBorder;
        ImageView imageViewItem;

        ViewHolder(View view) {
            super(view);
            imageViewItem = view.findViewById(R.id.imageViewItem);
            mSelectedBorder = view.findViewById(R.id.selectedBorder);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int p = selectedPos;
            selectedPos = getAdapterPosition();
            notifyItemChanged(p);
            notifyItemChanged(selectedPos);
            clickListener.onLayoutListClick(view, getAdapterPosition());
        }
    }
}
