package com.rohitneel.photopixelpro.photoframe.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.photoframe.activities.ActivityCreatePhoto;
import com.rohitneel.photopixelpro.photoframe.model.ModelclassFrameList;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.constant.CommonKeys;

import java.util.ArrayList;

public class AdapterFrameList extends RecyclerView.Adapter<AdapterFrameList.ViewHolder> {

    private Context context;
    private ArrayList<ModelclassFrameList> frameLists;
    LayoutInflater layoutInflater;

    public AdapterFrameList(Context context, ArrayList<ModelclassFrameList> frameLists) {
        this.context = context;
        this.frameLists = frameLists;
        CommonKeys.frameLists = frameLists;

        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rv_frame_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.imageView.setImageResource(frameLists.get(position).getFrame());
        final int frame = frameLists.get(position).getFrame();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CommonKeys.FrameId1 = position;
                Intent intent = new Intent(context, ActivityCreatePhoto.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //   intent.putExtra("frameId", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return frameLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv);
            imageView = itemView.findViewById(R.id.iv1);

        }
    }
}
