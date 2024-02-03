package com.rohitneel.photopixelpro.photocollage.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.module.Module;

import java.util.ArrayList;
import java.util.List;

public class PhotoSQToolsAdapter extends RecyclerView.Adapter<PhotoSQToolsAdapter.ViewHolder> {

    public OnQuShotSQItemSelected onQuShotSplashItemSelected;
    public List<ModuleModel> toolModelArrayList = new ArrayList<>();

    public interface OnQuShotSQItemSelected {
        void onQuShotSQToolSelected(Module module);
    }

    public PhotoSQToolsAdapter(OnQuShotSQItemSelected onItemSelected) {
        this.onQuShotSplashItemSelected = onItemSelected;
        this.toolModelArrayList.add(new ModuleModel("Splash BG", R.drawable.ic_splash_bg, Module.SPLASH_BG));
        this.toolModelArrayList.add(new ModuleModel("Sketch BG", R.drawable.ic_sketch_bg, Module.SKETCH_BG));
        this.toolModelArrayList.add(new ModuleModel("Blur BG", R.drawable.ic_blur_bg, Module.BLUR_BG));
        this.toolModelArrayList.add(new ModuleModel("Splash SQ", R.drawable.ic_splash_sq, Module.SPLASH_SQ));
        this.toolModelArrayList.add(new ModuleModel("Sketch SQ", R.drawable.ic_sketck_sq, Module.SKETCH_SQ));
    }

    class ModuleModel {
        public int toolIcon;
        public String toolName;
        public Module toolType;

        ModuleModel(String str, int i, Module toolType) {
            this.toolName = str;
            this.toolIcon = i;
            this.toolType = toolType;
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_effet_tool, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ModuleModel toolModel = this.toolModelArrayList.get(i);
        viewHolder.text_view_tool_name.setText(toolModel.toolName);
        viewHolder.image_view_tool_icon.setImageResource(toolModel.toolIcon);
    }

    public int getItemCount() {
        return this.toolModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_tool_icon;
        TextView text_view_tool_name;
        RelativeLayout relative_layout_wrapper_tool;

        ViewHolder(View view) {
            super(view);
            this.image_view_tool_icon = view.findViewById(R.id.image_view_adjust_icon);
            this.text_view_tool_name = view.findViewById(R.id.text_view_adjust_name);
            this.relative_layout_wrapper_tool = view.findViewById(R.id.linear_layout_wrapper_adjust);
            this.relative_layout_wrapper_tool.setOnClickListener(view1 ->
                    PhotoSQToolsAdapter.this.onQuShotSplashItemSelected.onQuShotSQToolSelected((PhotoSQToolsAdapter.this.toolModelArrayList.get(ViewHolder.this.getLayoutPosition())).toolType));
        }
    }
}
