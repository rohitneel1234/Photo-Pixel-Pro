package com.rohitneel.photopixelpro.photocollage.adapters;

import static com.rohitneel.photopixelpro.constant.SelectorSettings.getUriByResId;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.constant.SelectorSettings;
import com.rohitneel.photopixelpro.photocollage.listener.OnImageListener;
import com.rohitneel.photopixelpro.photocollage.model.ImageItem;
import com.rohitneel.photopixelpro.photocollage.model.ImageListContent;


import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context ctx;
    public final OnImageListener mListener;
    private final List<ImageItem> mValues;

    public ImageAdapter(Context context, List<ImageItem> items, OnImageListener listener) {
        this.mValues = items;
        this.mListener = listener;
        this.ctx = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Uri newURI;
        final ImageItem imageItem = this.mValues.get(position);
        holder.mItem = imageItem;
        File imageFile = new File(imageItem.path);
        if (imageFile.exists()) {
            newURI = Uri.fromFile(imageFile);
        } else {
            newURI = getUriByResId(R.drawable.image_show);
        }
        Glide.with(this.ctx).load(newURI).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().centerCrop()).error((int) R.drawable.image_show)).thumbnail(Glide.with(this.ctx).load(Integer.valueOf(R.drawable.image_show))).into(holder.ivItem);
        if (ImageListContent.isImageSelected(imageItem.path)) {
            holder.rfItem.setBackgroundColor(ctx.getColor(R.color.mainColor));
        } else {
            holder.rfItem.setBackgroundColor(ctx.getColor(R.color.transparent));
        }
        holder.rfItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ImageListContent.isImageSelected(imageItem.path)) {
                    ImageListContent.toggleImageSelected(imageItem.path);
                    ImageAdapter.this.notifyItemChanged(position);
                } else if (ImageListContent.SELECTED_IMAGES.size() < SelectorSettings.mMaxImageNumber) {
                    ImageListContent.toggleImageSelected(imageItem.path);
                    ImageAdapter.this.notifyItemChanged(position);
                } else {
                    ImageListContent.bReachMaxNumber = true;
                }
                if (ImageAdapter.this.mListener != null) {
                    ImageAdapter.this.mListener.onImageItem(holder.mItem);
                }
            }
        });
    }

    public int getItemCount() {
        return this.mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivItem;
        public ImageItem mItem;
        public final LinearLayout rfItem;

        public ViewHolder(View view) {
            super(view);
            this.rfItem = view.findViewById(R.id.rfItem);
            this.ivItem =  view.findViewById(R.id.ivItem);
        }
    }
}
