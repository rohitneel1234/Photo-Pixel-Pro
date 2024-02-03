package com.rohitneel.photopixelpro.photoframe.adapters;

/**
 * Created by deepshikha on 3/3/17.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.constant.CommonKeys;
import com.rohitneel.photopixelpro.photoframe.activities.ActivityCreatePhoto;
import com.rohitneel.photopixelpro.photoframe.model.Model_images;

import java.util.ArrayList;


public class AdapterGalleryImagesList extends ArrayAdapter<Model_images> {

    Context context;
    ViewHolder viewHolder;
    ArrayList<Model_images> al_menu = new ArrayList<>();
    int int_position;

    public AdapterGalleryImagesList(Context context, ArrayList<Model_images> al_menu, int int_position) {
        super(context, R.layout.rv_gallery_content, al_menu);
        this.al_menu = al_menu;
        this.context = context;
        this.int_position = int_position;


    }

    @Override
    public int getCount() {

        Log.e("ADAPTER LIST SIZE", al_menu.get(int_position).getAl_imagepath().size() + "");
        return al_menu.get(int_position).getAl_imagepath().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.get(int_position).getAl_imagepath().size() > 0) {
            return al_menu.get(int_position).getAl_imagepath().size();
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

        viewHolder.tv_foldern.setVisibility(View.GONE);
        viewHolder.tv_foldersize.setVisibility(View.GONE);


        Glide.with(context).load("file://" + al_menu.get(int_position).getAl_imagepath().get(position)).into(viewHolder.iv_image);


        viewHolder.iv_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CommonKeys.imageSet = true;
                Intent i = new Intent(getContext(), ActivityCreatePhoto.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //finishAffinity();
                i.putExtra("images", "file://" + al_menu.get(int_position).getAl_imagepath().get(position));
                context.startActivity(i);
            }
        });

        return convertView;

    }

    private static class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;
    }

}
