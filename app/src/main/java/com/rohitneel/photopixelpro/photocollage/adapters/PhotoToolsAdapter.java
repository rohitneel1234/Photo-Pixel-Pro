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

public class PhotoToolsAdapter extends RecyclerView.Adapter<PhotoToolsAdapter.ViewHolder> {

    public OnQuShotItemSelected onQuShotItemSelected;
    public List<ModuleModel> toolModelArrayList = new ArrayList<>();

    public interface OnQuShotItemSelected {
        void onQuShotToolSelected(Module module);
    }

    public PhotoToolsAdapter(OnQuShotItemSelected onItemSelected) {
        this.onQuShotItemSelected = onItemSelected;
        this.toolModelArrayList.add(new ModuleModel("Crop", R.drawable.ic_crop, Module.CROP));
        this.toolModelArrayList.add(new ModuleModel("Filter", R.drawable.ic_filter, Module.FILTER));
        this.toolModelArrayList.add(new ModuleModel("Adjust", R.drawable.ic_set, Module.ADJUST));
        this.toolModelArrayList.add(new ModuleModel("HSL", R.drawable.ic_hsl, Module.HSL));
        this.toolModelArrayList.add(new ModuleModel("Effect", R.drawable.ic_effect, Module.EFFECT));
        this.toolModelArrayList.add(new ModuleModel("Ratio", R.drawable.ic_ratio, Module.RATIO));
        this.toolModelArrayList.add(new ModuleModel("Text", R.drawable.ic_text, Module.TEXT));
        this.toolModelArrayList.add(new ModuleModel("Sticker", R.drawable.ic_sticker, Module.STICKER));
        this.toolModelArrayList.add(new ModuleModel("Blur", R.drawable.ic_blur, Module.BLURE));
        this.toolModelArrayList.add(new ModuleModel("Draw", R.drawable.ic_paint, Module.DRAW));
        this.toolModelArrayList.add(new ModuleModel("Mirror", R.drawable.ic_mirror, Module.MIRROR));
        this.toolModelArrayList.add(new ModuleModel("Frame", R.drawable.ic_frame, Module.BACKGROUND));
        this.toolModelArrayList.add(new ModuleModel("SQ/BG", R.drawable.ic_splash_square, Module.SQ_BG));
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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_editing_tool, viewGroup, false));
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
            this.image_view_tool_icon = view.findViewById(R.id.image_view_tool_icon);
            this.text_view_tool_name = view.findViewById(R.id.text_view_tool_name);
            this.relative_layout_wrapper_tool = view.findViewById(R.id.relative_layout_wrapper_tool);
            this.relative_layout_wrapper_tool.setOnClickListener(view1 ->
                    PhotoToolsAdapter.this.onQuShotItemSelected.onQuShotToolSelected((PhotoToolsAdapter.this.toolModelArrayList.get(ViewHolder.this.getLayoutPosition())).toolType));
        }
    }
}
