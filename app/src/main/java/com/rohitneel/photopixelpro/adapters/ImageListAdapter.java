package com.rohitneel.photopixelpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rohitneel.photopixelpro.R;

import java.util.ArrayList;

/**
 * Created by Rohit Neel on 12/09/2020.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    private ArrayList<String> fileArrayList;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public ImageListAdapter (ArrayList<String> arrayList){
        fileArrayList = arrayList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
                 GlideApp.with(context)
                .load(fileArrayList.get(holder.getAdapterPosition()))
                .centerCrop()
                .into(holder.imageView);

        if(fileArrayList.get(holder.getAdapterPosition()).contains(".mp4"))
            holder.playBtn.setVisibility(View.VISIBLE);
        else
            holder.playBtn.setVisibility(View.GONE);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Image View holder for utilizing the ViewHolder pattern.
     */

    class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ImageView playBtn;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivItemImage);
            playBtn = itemView.findViewById(R.id.itemButton);
        }

    }

    public interface OnItemClickListener {

        public void onClick(int position);
    }
}
