package com.rohitneel.photopixelpro.photoframe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photoframe.activities.ActivityCreatePhoto;
import com.rohitneel.photopixelpro.photoframe.model.ModelStickers;


public class AdapterStickersList extends RecyclerView.Adapter<AdapterStickersList.ViewHolder> {

    private Context context;
    private ModelStickers[] stickerlist;
    LayoutInflater layoutInflater;
    String type;

    public AdapterStickersList(Context context, int framelist_raw_item, ModelStickers[] stickers, String popup) {
        this.context = context;
        this.stickerlist = stickers;
        this.layoutInflater = LayoutInflater.from(context);
        this.type=popup;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.stickerlist_raw_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(context).load(stickerlist[position].getImgId()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCreatePhoto.getInstance().setEmoji(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickerlist.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_framlist_raw);

        }
    }
}
