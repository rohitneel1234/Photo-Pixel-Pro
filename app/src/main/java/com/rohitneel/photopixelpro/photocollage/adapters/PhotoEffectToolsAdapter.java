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

public class PhotoEffectToolsAdapter extends RecyclerView.Adapter<PhotoEffectToolsAdapter.ViewHolder> {

    public OnQuShotEffectItemSelected onQuShotEffectItemSelected;
    public List<ModuleModel> toolModelArrayList = new ArrayList<>();

    public interface OnQuShotEffectItemSelected {
        void onQuShotEffectToolSelected(Module module);
    }

    public PhotoEffectToolsAdapter(OnQuShotEffectItemSelected onItemSelected) {
        this.onQuShotEffectItemSelected = onItemSelected;
        this.toolModelArrayList.add(new ModuleModel("Overlay", R.drawable.ic_overlay, Module.OVERLAY));
        this.toolModelArrayList.add(new ModuleModel("Neon", R.drawable.ic_neon, Module.NEON));
        this.toolModelArrayList.add(new ModuleModel("Wing", R.drawable.ic_wing, Module.WINGS));
        this.toolModelArrayList.add(new ModuleModel("Drip", R.drawable.ic_drip, Module.DRIP));
        this.toolModelArrayList.add(new ModuleModel("Splash", R.drawable.ic_splash, Module.SPLASH));
        this.toolModelArrayList.add(new ModuleModel("Art", R.drawable.ic_art, Module.ART));
        this.toolModelArrayList.add(new ModuleModel("Motion", R.drawable.ic_motion, Module.MOTION));
        this.toolModelArrayList.add(new ModuleModel("PixLab", R.drawable.ic_pixlab, Module.PIX));
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
                    PhotoEffectToolsAdapter.this.onQuShotEffectItemSelected.onQuShotEffectToolSelected((PhotoEffectToolsAdapter.this.toolModelArrayList.get(ViewHolder.this.getLayoutPosition())).toolType));
        }
    }
}
