package com.rohitneel.photopixelpro.photocollage.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.entity.PhotoDirectory;

import java.util.List;

public class DirectoryListAdapter extends BaseAdapter {
    private List<PhotoDirectory> directories;
    public RequestManager glide;

    public DirectoryListAdapter(RequestManager requestManager, List<PhotoDirectory> list) {
        this.directories = list;
        this.glide = requestManager;
    }

    public int getCount() {
        return this.directories.size();
    }

    public PhotoDirectory getItem(int i) {
        return this.directories.get(i);
    }

    public long getItemId(int i) {
        return this.directories.get(i).hashCode();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_directory, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.bindData(this.directories.get(i));
        return view;
    }

    private class ViewHolder {
        public ImageView image_view_cover_album;
        public TextView text_view_count;
        public TextView text_view_name;

        public ViewHolder(View view) {
            this.image_view_cover_album = view.findViewById(R.id.image_view_cover_album);
            this.text_view_name = view.findViewById(R.id.text_view_name);
            this.text_view_count = view.findViewById(R.id.text_view_count);
        }

        public void bindData(PhotoDirectory photoDirectory) {
            RequestOptions requestOptions = new RequestOptions();
            ((requestOptions.dontAnimate()).dontTransform()).override(800, 800);
            DirectoryListAdapter.this.glide.setDefaultRequestOptions(requestOptions).load(photoDirectory.getCoverPath()).thumbnail(0.1f).into(this.image_view_cover_album);
            this.text_view_name.setText(photoDirectory.getName());
            TextView textView = this.text_view_count;
            textView.setText(photoDirectory.getPhotos().size() + "");
        }
    }
}
