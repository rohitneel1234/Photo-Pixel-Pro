package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.listener.LayoutItemListener;

import java.util.ArrayList;

public class WingsAdapter extends Adapter<WingsAdapter.ViewHolder> {

    public LayoutItemListener menuItemClickLister;
    public int selectedItem = 0;
    Context context;
    private ArrayList<String> wingsIcons = new ArrayList<>();

    public WingsAdapter(Context mContext) {
        this.context = mContext;
    }

    public void addData(ArrayList<String> arrayList) {
        this.wingsIcons.clear();
        this.wingsIcons.addAll(arrayList);
        notifyDataSetChanged();
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_neon, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSelectedBorder.setVisibility(position == selectedItem ? View.VISIBLE : View.GONE);

        String file_1 = "file:///android_asset/wings/" + wingsIcons.get(position) + ".webp";
        Glide.with(context)
                .load(file_1)
                .fitCenter()
                .into(holder.imageViewItem1);
    }

    public int getItemCount() {
        return wingsIcons.size();
    }

    public ArrayList<String> getItemList() {
        return wingsIcons;
    }

    public void setMenuItemClickLister(LayoutItemListener menuItemClickLister) {
        this.menuItemClickLister = menuItemClickLister;
    }

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements OnClickListener {

        View mSelectedBorder;
        ImageView imageViewItem1;

        ViewHolder(View view) {
            super(view);
            imageViewItem1 = view.findViewById(R.id.imageViewItem1);
            mSelectedBorder = view.findViewById(R.id.selectedBorder);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int p = selectedItem;
            selectedItem = getAdapterPosition();
            notifyItemChanged(p);
            notifyItemChanged(selectedItem);
            menuItemClickLister.onLayoutListClick(view, getAdapterPosition());
        }
    }
}
