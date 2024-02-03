package com.rohitneel.photopixelpro.photoframe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photoframe.activities.ActivityCreatePhoto;
import com.rohitneel.photopixelpro.photoframe.model.ModelFontDetail;

import java.util.ArrayList;


public class AdapterFontList extends RecyclerView.Adapter<AdapterFontList.FontListForShayriDetailViewHolder> {

    Context context;
    int resource;
    ArrayList<ModelFontDetail> arrayList;

    public AdapterFontList(Context context, int resource, ArrayList<ModelFontDetail> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;

    }

    @Override
    public FontListForShayriDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new FontListForShayriDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FontListForShayriDetailViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        String[] fontlist = arrayList.get(position).getFontName().split("\\.");

        holder.tvFont.setText(fontlist[0].replace("-", " "));
        try {
            holder.tvFont.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + arrayList.get(position).getFontName()));
        } catch (Exception e) {
            Log.e("Error", "Error");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCreatePhoto.getInstance().SetFontToText(arrayList.get(position).getFontName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FontListForShayriDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tvFont;

        public FontListForShayriDetailViewHolder(View itemView) {
            super(itemView);
            tvFont = (TextView) itemView.findViewById(R.id.tvFontName);
        }
    }
}