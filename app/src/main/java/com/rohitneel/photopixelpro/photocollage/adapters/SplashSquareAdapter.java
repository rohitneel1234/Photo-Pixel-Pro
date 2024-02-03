package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.assets.StickerFileAsset;
import com.rohitneel.photopixelpro.photocollage.constants.Constants;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoSplashSticker;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class SplashSquareAdapter extends RecyclerView.Adapter<SplashSquareAdapter.ViewHolder> {
    private int borderWidth;
    private Context context;

    public int selectedSquareIndex;

    public SplashChangeListener splashChangeListener;

    public List<SplashItem> splashList = new ArrayList();

    public interface SplashChangeListener {
        void onSelected(PhotoSplashSticker splashSticker);
    }

    public SplashSquareAdapter(Context context, SplashChangeListener splashChangeListener, boolean z) {
        this.context = context;
        this.splashChangeListener = splashChangeListener;
        this.borderWidth = SystemUtil.dpToPx(context, Constants.BORDER_WIDTH);
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_1.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_1.webp")), R.drawable.icon_splash_1));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_2.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_2.webp")), R.drawable.icon_splash_2));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_3.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_3.webp")), R.drawable.icon_splash_3));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_4.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_4.webp")), R.drawable.icon_splash_4));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_5.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_5.webp")), R.drawable.icon_splash_5));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_6.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_6.webp")), R.drawable.icon_splash_6));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_7.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_7.webp")), R.drawable.icon_splash_7));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_8.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_8.webp")), R.drawable.icon_splash_8));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_9.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_9.webp")), R.drawable.icon_splash_9));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_10.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_10.webp")), R.drawable.icon_splash_10));
       /* this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_11.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_11.webp")), R.drawable.icon_splash_11));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_12.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_12.webp")), R.drawable.icon_splash_12));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_13.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_13.webp")), R.drawable.icon_splash_13));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_14.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_14.webp")), R.drawable.icon_splash_14));
        this.splashList.add(new SplashItem(new PhotoSplashSticker(StickerFileAsset.loadBitmapFromAssets(context, "frame/image_mask_15.webp"), StickerFileAsset.loadBitmapFromAssets(context, "frame/image_frame_15.webp")), R.drawable.icon_splash_15));*/
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_splash, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.splash.setImageResource(this.splashList.get(i).drawableId);
        if (this.selectedSquareIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.viewSelected.setVisibility(View.GONE);
    }

    public int getItemCount() {
        return this.splashList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedImageView splash;
        View viewSelected;
        public ViewHolder(View view) {
            super(view);
            this.splash = view.findViewById(R.id.round_image_view_splash_item);
            this.viewSelected = view.findViewById(R.id.view_selected);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            SplashSquareAdapter.this.selectedSquareIndex = getAdapterPosition();
            if (SplashSquareAdapter.this.selectedSquareIndex < 0) {
                SplashSquareAdapter.this.selectedSquareIndex = 0;
            }
            if (SplashSquareAdapter.this.selectedSquareIndex >= SplashSquareAdapter.this.splashList.size()) {
                SplashSquareAdapter.this.selectedSquareIndex = SplashSquareAdapter.this.splashList.size() - 1;
            }
            SplashSquareAdapter.this.splashChangeListener.onSelected((SplashSquareAdapter.this.splashList.get(SplashSquareAdapter.this.selectedSquareIndex)).splashSticker);
            SplashSquareAdapter.this.notifyDataSetChanged();
        }
    }

    class SplashItem {
        int drawableId;
        PhotoSplashSticker splashSticker;

        SplashItem(PhotoSplashSticker splashSticker2, int i) {
            this.splashSticker = splashSticker2;
            this.drawableId = i;
        }
    }
}
