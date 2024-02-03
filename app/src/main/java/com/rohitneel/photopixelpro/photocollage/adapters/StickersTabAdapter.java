package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.rohitneel.photopixelpro.R;

public class StickersTabAdapter extends RecyclerTabLayout.Adapter<StickersTabAdapter.ViewHolder> {
    private Context context;
    private PagerAdapter mAdapater = this.mViewPager.getAdapter();

    public StickersTabAdapter(ViewPager viewPager, Context context2) {
        super(viewPager);
        this.context = context2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tab_sticker, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.bubble));
                break;
            case 1:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.rainbow));
                break;
            case 2:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.flower));
                break;
            case 3:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.amoji));
                break;
            case 4:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.delicious));
                break;
            case 5:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.popular));
                break;
            case 6:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.valentine));
                break;
            case 7:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.emoj));
                break;
            case 8:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.christmas));
                break;
            case 9:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.unicorn));
                break;
            case 10:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.sticker));
                break;
        }
        viewHolder.imageView.setSelected(i == getCurrentIndicatorPosition());
    }

    public int getItemCount() {
        return this.mAdapater.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.image);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                   StickersTabAdapter.this.getViewPager().setCurrentItem(ViewHolder.this.getAdapterPosition());
                }
            });
        }
    }
}
