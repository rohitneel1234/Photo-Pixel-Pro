package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.entity.Photo;
import com.rohitneel.photopixelpro.photocollage.entity.PhotoDirectory;
import com.rohitneel.photopixelpro.photocollage.event.OnItemCheckListener;
import com.rohitneel.photopixelpro.photocollage.picker.AndroidLifecycleUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {
    private int columnNumber;
    private RequestManager glide;
    private boolean hasCamera;
    private int imageSize;

    public View.OnClickListener onClickListener;

    public OnItemCheckListener onItemCheckListener;
    private boolean previewEnable;

    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> list) {
        this.onItemCheckListener = null;
        this.onClickListener = null;
        this.hasCamera = true;
        this.previewEnable = true;
        this.columnNumber = 4;
        this.photoDirectories = list;
        this.glide = requestManager;
        setColumnNumber(context, this.columnNumber);
    }

    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> list, ArrayList<String> arrayList, int i) {
        this(context, requestManager, list);
        setColumnNumber(context, i);
        this.selectedPhotos = new ArrayList();
        if (arrayList != null) {
            this.selectedPhotos.addAll(arrayList);
        }
    }

    private void setColumnNumber(Context context, int i) {
        this.columnNumber = i;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        this.imageSize = displayMetrics.widthPixels / i;
    }

    public int getItemViewType(int i) {
        return (!showCamera() || i != 0) ? 101 : 100;
    }

    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_editor_photo, viewGroup, false));
        if (i == 100) {
            photoViewHolder.image_view_picker.setScaleType(ImageView.ScaleType.CENTER);
            photoViewHolder.image_view_picker.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    photoViewHolder.image_view_selected.setVisibility(View.VISIBLE);
                    if (PhotoGridAdapter.this.onClickListener != null) {
                        PhotoGridAdapter.this.onClickListener.onClick(view);
                    }
                }
            });
        }
        return photoViewHolder;
    }

    public void onBindViewHolder(final PhotoViewHolder photoViewHolder, int i) {
        final Photo photo;
        if (getItemViewType(i) == 101) {
            List<Photo> currentPhotos = getCurrentPhotos();
            if (showCamera()) {
                photo = currentPhotos.get(i - 1);
            } else {
                photo = currentPhotos.get(i);
            }
            if (AndroidLifecycleUtils.canLoadImage(photoViewHolder.image_view_picker.getContext())) {
                RequestOptions requestOptions = new RequestOptions();
                ((RequestOptions) ((RequestOptions) ((RequestOptions) requestOptions.centerCrop()).dontAnimate()).override(this.imageSize, this.imageSize)).placeholder((int) R.drawable.image_show);
                this.glide.setDefaultRequestOptions(requestOptions).load(new File(photo.getPath())).thumbnail(0.5f).into(photoViewHolder.image_view_picker);
            }
            boolean isSelected = isSelected(photo);
            photoViewHolder.image_view_selected.setSelected(isSelected);
            photoViewHolder.image_view_picker.setSelected(isSelected);
            photoViewHolder.image_view_picker.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    photoViewHolder.image_view_selected.setVisibility(View.VISIBLE);
                    PhotoGridAdapter.this.onItemCheckListener.onItemCheck(photoViewHolder.getAdapterPosition(), photo, PhotoGridAdapter.this.getSelectedPhotos().size() + (PhotoGridAdapter.this.isSelected(photo) ? -1 : 1));
                }
            });
            return;
        }
        photoViewHolder.image_view_picker.setImageResource(R.drawable.black_border);
    }

    public int getItemCount() {
        int size = this.photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        return showCamera() ? size + 1 : size;
    }

    @Override
    public boolean isSelected(Photo photo) {
        return false;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_view_picker;

        public View image_view_selected;

        public PhotoViewHolder(View view) {
            super(view);
            this.image_view_picker = view.findViewById(R.id.image_view_picker);
            this.image_view_selected = view.findViewById(R.id.image_view_selected);
            this.image_view_selected.setVisibility(View.GONE);
        }
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener2) {
        this.onItemCheckListener = onItemCheckListener2;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> arrayList = new ArrayList<>(getSelectedItemCount());
        for (String add : this.selectedPhotos) {
            arrayList.add(add);
        }
        return arrayList;
    }

    public void setShowCamera(boolean z) {
        this.hasCamera = z;
    }

    public void setPreviewEnable(boolean z) {
        this.previewEnable = z;
    }

    public boolean showCamera() {
        return this.hasCamera && this.currentDirectoryIndex == 0;
    }

    public void onViewRecycled(PhotoViewHolder photoViewHolder) {
        this.glide.clear(photoViewHolder.image_view_picker);
        super.onViewRecycled(photoViewHolder);
    }
}
