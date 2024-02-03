package com.rohitneel.photopixelpro.photocollage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;

import java.util.ArrayList;
import java.util.List;

public class Mirror2DAdapter extends RecyclerView.Adapter<Mirror2DAdapter.ViewHolder> {
    private Context context;
    public Mirror2Listener frameListener;
    public int selectedSquareIndex;
    public List<SquareView> squareViewList;

    public interface Mirror2Listener {
        void onBackgroundSelected(SquareView squareView);
    }

    public Mirror2DAdapter(Context context2, Mirror2Listener frameListener2) {
        ArrayList arrayList = new ArrayList();
        this.squareViewList = arrayList;
        this.context = context2;
        this.frameListener = frameListener2;
        /*arrayList.add(new SquareView(R.drawable.mirror_2d_1_icon, "2D-1"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d4, "2D-2"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d5, "2D-3"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d9, "2D-4"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d14, "2D-5"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d14, "2D-6"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d14, "2D-7"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d14, "2D-8"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d14, "2D-9"));
        this.squareViewList.add(new SquareView(R.drawable.mirror_2d14, "2D-10"));

         */
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mirror, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.squareView.setBackgroundResource(this.squareViewList.get(i).drawableId);
        if (this.selectedSquareIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewSelected.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.squareViewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView squareView;
        View viewSelected;
        public ConstraintLayout wrapSquareView;

        public ViewHolder(View view) {
            super(view);
            this.squareView = view.findViewById(R.id.square_view);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.wrapSquareView = (ConstraintLayout) view.findViewById(R.id.constraint_layout_wrapper_square_view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            Mirror2DAdapter.this.selectedSquareIndex = getAdapterPosition();
            Mirror2DAdapter.this.frameListener.onBackgroundSelected(Mirror2DAdapter.this.squareViewList.get(Mirror2DAdapter.this.selectedSquareIndex));
            Mirror2DAdapter.this.notifyDataSetChanged();
        }
    }

    public class SquareView {
        public int drawableId;
        public String text;

        SquareView(int i, String string) {
            this.drawableId = i;
            this.text = string;
        }

        SquareView(int i, String string, boolean z) {
            this.drawableId = i;
            this.text = string;
        }
    }
}
