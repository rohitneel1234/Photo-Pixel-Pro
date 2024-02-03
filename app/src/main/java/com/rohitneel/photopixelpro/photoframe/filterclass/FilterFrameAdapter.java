package com.rohitneel.photopixelpro.photoframe.filterclass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;

import java.util.ArrayList;

public class FilterFrameAdapter extends RecyclerView.Adapter<FilterFrameAdapter.ViewHolder> {
    private String Selection = "0";
    private Activity activity;
    private FilterCallback filterCallback;
    private ArrayList<Filter> list;

    public interface FilterCallback {
        void FilterMethod(int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout border;
        LinearLayout mainView;
        ImageView thumbnail;
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            this.border = itemView.findViewById(R.id.relativeborder);
            this.txtTitle = itemView.findViewById(R.id.tvTitle);
            this.mainView = itemView.findViewById(R.id.ll_mainView_ll);
        }
    }

    public FilterFrameAdapter(Activity activity, ArrayList<Filter> list) {
        this.list = list;
        this.activity = activity;
        this.filterCallback = (FilterCallback) activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity).inflate(R.layout.item_frame_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.thumbnail.setImageResource(this.list.get(position).getThumb());
        holder.txtTitle.setText(this.list.get(position).getTitle());
        if (position == Integer.parseInt(this.Selection)) {
            holder.border.setVisibility(View.VISIBLE);
        } else {
            holder.border.setVisibility(View.INVISIBLE);
        }
        holder.thumbnail.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Selection = String.valueOf(position);
                notifyDataSetChanged();
                filterCallback.FilterMethod(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}