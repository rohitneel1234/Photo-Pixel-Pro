package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.assets.StickerFileAsset;
//import com.rohitneel.photopixelpro.photocollage.assets.StickerFileAsset;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    public Context context;
    public int screenWidth;
    public OnClickSplashListener onClickSplashListener;

    public List<String> stringList;

    public interface OnClickSplashListener {
        void addSticker(int i,Bitmap bitmap);
    }

    public StickerAdapter(Context context2, List<String> list, int i, OnClickSplashListener onClickSplashListener) {
        this.context = context2;
        this.stringList = list;
        this.screenWidth = i;
        this.onClickSplashListener = onClickSplashListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bitmap bitmap = StickerFileAsset.loadBitmapFromAssets(this.context, this.stringList.get(i));
        Glide.with(context).load(bitmap).into(viewHolder.sticker);
    }

    public int getItemCount() {
        return this.stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView sticker;

        public ViewHolder(View view) {
            super(view);
            this.sticker = view.findViewById(R.id.image_view_item_sticker);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            StickerAdapter.this.onClickSplashListener.addSticker(getAdapterPosition(),StickerFileAsset.loadBitmapFromAssets(StickerAdapter.this.context, (String) StickerAdapter.this.stringList.get(getAdapterPosition())));
        }
    }
}
