package com.rohitneel.photopixelpro.photoframe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photoframe.model.Model_images;


import java.util.ArrayList;


public class AdapterGalleryFileList extends ArrayAdapter<Model_images> {

    Context context;
    ViewHolder viewHolder;
    ArrayList<Model_images> al_menu = new ArrayList<>();


    public AdapterGalleryFileList(Context context, ArrayList<Model_images> al_menu) {
        super(context, R.layout.rv_gallery_content, al_menu);
        this.al_menu = al_menu;
        this.context = context;


    }

    @Override
    public int getCount() {
        return al_menu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.size() > 0) {
            return al_menu.size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rv_gallery_content, parent, false);
            viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.Tv1);
            viewHolder.tv_foldersize = (TextView) convertView.findViewById(R.id.Tv1);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.Iv1);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_foldern.setText(al_menu.get(position).getStr_folder());

        Glide.with(context).load("file://" + al_menu.get(position).getAl_imagepath().get(0)).into(viewHolder.iv_image);

        return convertView;
    }

    private static class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;
    }

}
