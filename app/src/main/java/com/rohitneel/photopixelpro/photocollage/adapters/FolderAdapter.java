package com.rohitneel.photopixelpro.photocollage.adapters;

import static com.rohitneel.photopixelpro.constant.SelectorSettings.getUriByResId;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.listener.OnFolderListener;
import com.rohitneel.photopixelpro.photocollage.model.FolderItem;
import com.rohitneel.photopixelpro.photocollage.model.FolderListContent;


import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    Context ctx;
    public final OnFolderListener mListener;
    private final List<FolderItem> mValues;

    public FolderAdapter(Context context, List<FolderItem> items, OnFolderListener listener) {
        this.mValues = items;
        this.mListener = listener;
        this.ctx = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Uri newURI;
        FolderItem folderItem = this.mValues.get(position);
        holder.mItem = folderItem;
        holder.tvName.setText(folderItem.name);
        holder.tvSize.setText(folderItem.getNumOfImages());
        if (position == FolderListContent.selectedFolderIndex) {
            holder.iLine.setVisibility(View.VISIBLE);
        } else {
            holder.iLine.setVisibility(View.GONE);
        }
        File imageFile = new File(folderItem.coverPath);
        if (imageFile.exists()) {
            newURI = Uri.fromFile(imageFile);
        } else {
            newURI = getUriByResId(R.drawable.iv_loading);
        }
        Glide.with(this.ctx).load(newURI).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().centerCrop()).error((int) R.drawable.image_show)).thumbnail(Glide.with(this.ctx).load(Integer.valueOf(R.drawable.iv_loading))).into((ImageView) holder.iCover);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int previousSelectedIndex = FolderListContent.selectedFolderIndex;
                FolderListContent.setSelectedFolder(holder.mItem, position);
                notifyItemChanged(previousSelectedIndex);
                notifyItemChanged(position);
                if (mListener != null) {
                    mListener.onFolderItem(holder.mItem);
                }
            }
        });
    }

    public int getItemCount() {
        return this.mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iCover;
        ImageView iLine;
        TextView tvName, tvSize;
        FolderItem mItem;
        View mView;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.iCover =  view.findViewById(R.id.iCover);
            this.tvName =  view.findViewById(R.id.tvName);
            this.tvSize =  view.findViewById(R.id.tvSize);
            this.iLine =  view.findViewById(R.id.iLine);
        }
    }
}
