package com.rohitneel.photopixelpro.photoframe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.constant.CommonKeys;
import com.rohitneel.photopixelpro.photoframe.activities.ActivityCreatePhoto;
import com.rohitneel.photopixelpro.photoframe.model.ModelclassFrameList;

import java.util.ArrayList;

public class AdepterFrameListPopup extends RecyclerView.Adapter<AdepterFrameListPopup.FrameListViewHolder>{

    ArrayList<ModelclassFrameList> LocalFrame;
    Context context;
    public  int resource;
    String type;


    public AdepterFrameListPopup(Context context, int resource, ArrayList<ModelclassFrameList> LocalFrame, String type) {
        this.context = context;
        this.resource = resource;
        this.LocalFrame = LocalFrame;
        this.type=type;
    }

    @NonNull
    @Override
    public FrameListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new FrameListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrameListViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.iv_framlist_raw.setImageResource(LocalFrame.get(position).getFrame());

       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("popup"))
                {
                    CommonKeys.FrameId=position;
                    ActivityCreatePhoto.getInstance().setFrame(position);
                }

                 }
        });
    }

    @Override
    public int getItemCount() {
        return LocalFrame.size();
    }

    public class FrameListViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_framlist_raw;

        public FrameListViewHolder(View itemView) {
            super(itemView);
            iv_framlist_raw = (ImageView) itemView.findViewById(R.id.iv_framlist_raw);
        }
    }



}
