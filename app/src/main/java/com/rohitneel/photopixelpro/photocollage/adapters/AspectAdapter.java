package com.rohitneel.photopixelpro.photocollage.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.model.RatioModel;
import com.steelkiwi.cropiwa.AspectRatio;

import java.util.Arrays;
import java.util.List;

public class AspectAdapter extends RecyclerView.Adapter<AspectAdapter.ViewHolder> {
    public int lastSelectedView;
    public OnNewSelectedListener listener;
    public List<RatioModel> ratios;
    public RatioModel selectedRatio;

    public interface OnNewSelectedListener {
        void onNewAspectRatioSelected(AspectRatio aspectRatio);
    }

    public AspectAdapter() {
        this.ratios = Arrays.asList(
                new RatioModel(10, 10, R.drawable.ic_crop_free, R.drawable.ic_crop_free_click, "Free"),
                new RatioModel(5, 4, R.drawable.ic_crop_5_4, R.drawable.ic_crop_5_4_click,"5:4"),
                new RatioModel(1, 1, R.drawable.ic_instagram_1_1, R.drawable.ic_instagram_1_1_click,"1:1"),
                new RatioModel(4, 3, R.drawable.ic_crop_4_3, R.drawable.ic_crop_4_3_click, "4:3"),
                new RatioModel(4, 5, R.drawable.ic_instagram_4_5, R.drawable.ic_instagram_4_5_click,"4:5"),
                new RatioModel(1, 2, R.drawable.ic_crop_5_4, R.drawable.ic_crop_5_4_click, "1:2"),
                new RatioModel(9, 16, R.drawable.ic_instagram_story, R.drawable.ic_instagram_story_click,"Story"),
                new RatioModel(16, 7, R.drawable.ic_movie, R.drawable.ic_movie_click,"Movie"),
                new RatioModel(2, 3, R.drawable.ic_crop_2_3, R.drawable.ic_crop_2_3_click, "2:3"),
                new RatioModel(4, 3, R.drawable.ic_fb_post, R.drawable.ic_fb_post_click, "Post"),
                new RatioModel(16, 6, R.drawable.ic_fb_cover, R.drawable.ic_fb_cover_click, "Cover"),
                new RatioModel(16, 9, R.drawable.ic_crop_16_9, R.drawable.ic_crop_16_9_click, "16:9"),
                new RatioModel(3, 2, R.drawable.ic_crop_3_2, R.drawable.ic_crop_3_2_click, "3:2"),
                new RatioModel(2, 3, R.drawable.ic_pinterest, R.drawable.ic_pinterest_click, "Post"),
                new RatioModel(16, 9, R.drawable.ic_crop_youtube, R.drawable.ic_crop_youtube_click, "Cover"),
                new RatioModel(9, 16, R.drawable.ic_crop_9_16, R.drawable.ic_crop_9_16_click, "9:16"),
                new RatioModel(3, 4, R.drawable.ic_crop_3_4, R.drawable.ic_crop_3_4_click,"3:4"),
                new RatioModel(16, 8, R.drawable.ic_crop_post_twitter, R.drawable.ic_crop_post_twitter_click, "Post"),
                new RatioModel(16, 5, R.drawable.ic_crop_header, R.drawable.ic_crop_header_click, "Header"),
                new RatioModel(10, 16, R.drawable.ic_crop_a4, R.drawable.ic_crop_a4_click, "A4"),
                new RatioModel(10, 16, R.drawable.ic_crop_a5, R.drawable.ic_crop_a5_click, "A5")
        );
        this.selectedRatio = this.ratios.get(0);
    }

    public AspectAdapter(boolean z) {
        this.ratios = Arrays.asList(
                new RatioModel(5, 4, R.drawable.ic_crop_5_4, R.drawable.ic_crop_5_4_click,"5:4"),
                new RatioModel(1, 1, R.drawable.ic_instagram_1_1, R.drawable.ic_instagram_1_1_click,"1:1"),
                new RatioModel(4, 3, R.drawable.ic_crop_4_3, R.drawable.ic_crop_4_3_click, "4:3"),
                new RatioModel(4, 5, R.drawable.ic_instagram_4_5, R.drawable.ic_instagram_4_5_click,"4:5"),
                new RatioModel(1, 2, R.drawable.ic_crop_5_4, R.drawable.ic_crop_5_4_click, "1:2"),
                new RatioModel(9, 16, R.drawable.ic_instagram_story, R.drawable.ic_instagram_story_click,"Story"),
                new RatioModel(16, 7, R.drawable.ic_movie, R.drawable.ic_movie_click,"Movie"),
                new RatioModel(2, 3, R.drawable.ic_crop_2_3, R.drawable.ic_crop_2_3_click, "2:3"),
                new RatioModel(4, 3, R.drawable.ic_fb_post, R.drawable.ic_fb_post_click, "Post"),
                new RatioModel(16, 6, R.drawable.ic_fb_cover, R.drawable.ic_fb_cover_click, "Cover"),
                new RatioModel(16, 9, R.drawable.ic_crop_16_9, R.drawable.ic_crop_16_9_click, "16:9"),
                new RatioModel(3, 2, R.drawable.ic_crop_3_2, R.drawable.ic_crop_3_2_click, "3:2"),
                new RatioModel(2, 3, R.drawable.ic_pinterest, R.drawable.ic_pinterest_click, "Post"),
                new RatioModel(16, 9, R.drawable.ic_crop_youtube, R.drawable.ic_crop_youtube_click, "Cover"),
                new RatioModel(9, 16, R.drawable.ic_crop_9_16, R.drawable.ic_crop_9_16_click, "9:16"),
                new RatioModel(3, 4, R.drawable.ic_crop_3_4, R.drawable.ic_crop_3_4_click,"3:4"),
                new RatioModel(16, 8, R.drawable.ic_crop_post_twitter, R.drawable.ic_crop_post_twitter_click, "Post"),
                new RatioModel(16, 5, R.drawable.ic_crop_header, R.drawable.ic_crop_header_click, "Header"),
                new RatioModel(10, 16, R.drawable.ic_crop_a4, R.drawable.ic_crop_a4_click, "A4"),
                new RatioModel(10, 16, R.drawable.ic_crop_a5, R.drawable.ic_crop_a5_click, "A5")
        );
        this.selectedRatio = this.ratios.get(0);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_crop, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RatioModel aspectRatioCustom = this.ratios.get(i);
        if (i == this.lastSelectedView) {
            viewHolder.ratioView.setImageResource(aspectRatioCustom.getSelectedIem());
            viewHolder.text_view_filter_name.setText(aspectRatioCustom.getName());
            viewHolder.text_view_filter_name.setTextColor(Color.parseColor("#FF4F19"));
        } else {
            viewHolder.ratioView.setImageResource(aspectRatioCustom.getUnselectItem());
            viewHolder.text_view_filter_name.setText(aspectRatioCustom.getName());
            viewHolder.text_view_filter_name.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void setLastSelectedView(int i) {
        this.lastSelectedView = i;
    }

    public int getItemCount() {
        return this.ratios.size();
    }

    public void setListener(OnNewSelectedListener onNewSelectedListener) {
        this.listener = onNewSelectedListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ratioView;
        public RelativeLayout relativeLayoutCropper;
        TextView text_view_filter_name;
        public ViewHolder(View view) {
            super(view);
            this.ratioView = view.findViewById(R.id.image_view_aspect_ratio);
            this.text_view_filter_name = view.findViewById(R.id.text_view_filter_name);
            this.relativeLayoutCropper = view.findViewById(R.id.relativeLayoutCropper);
            this.ratioView.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (AspectAdapter.this.lastSelectedView != getAdapterPosition()) {
                AspectAdapter.this.selectedRatio = AspectAdapter.this.ratios.get(getAdapterPosition());
                AspectAdapter.this.lastSelectedView = getAdapterPosition();
                if (AspectAdapter.this.listener != null) {
                    AspectAdapter.this.listener.onNewAspectRatioSelected(AspectAdapter.this.selectedRatio);
                }
                AspectAdapter.this.notifyDataSetChanged();
            }
        }
    }
}
