package com.rohitneel.photopixelpro.photocollage.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.adapters.FilterAdapter;
import com.rohitneel.photopixelpro.photocollage.assets.FilterFileAsset;
import com.rohitneel.photopixelpro.photocollage.listener.FilterListener;
import com.rohitneel.photopixelpro.photocollage.utils.FilterUtils;

import java.util.Arrays;
import java.util.List;

public class FilterFragment extends DialogFragment implements FilterListener {
    private static final String TAG = "FilterFragment";

    public Bitmap bitmap;
    private List<Bitmap> lstFilterBitmap;

    public OnFilterSavePhoto onFilterSavePhoto;

    public TextView textViewTitle;
    public ImageView image_view_preview;
    private RecyclerView recycler_view_filter;

    public interface OnFilterSavePhoto {
        void onSaveFilter(Bitmap bitmap);
    }

    public void setOnFilterSavePhoto(OnFilterSavePhoto onFilterSavePhoto2) {
        this.onFilterSavePhoto = onFilterSavePhoto2;
    }

    public void setLstFilterBitmap(List<Bitmap> list) {
        this.lstFilterBitmap = list;

    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static FilterFragment show(@NonNull AppCompatActivity appCompatActivity, OnFilterSavePhoto onFilterSavePhoto2, Bitmap bitmap2, List<Bitmap> list) {
        FilterFragment filterDialogFragment = new FilterFragment();
        filterDialogFragment.setBitmap(bitmap2);
        filterDialogFragment.setOnFilterSavePhoto(onFilterSavePhoto2);
        filterDialogFragment.setLstFilterBitmap(list);
        filterDialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return filterDialogFragment;
    }

    public void onDestroy() {
        super.onDestroy();
        this.lstFilterBitmap.clear();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_filter, viewGroup, false);
        this.recycler_view_filter = inflate.findViewById(R.id.recycler_view_filter_all);
        this.recycler_view_filter.setAdapter(new FilterAdapter(this.lstFilterBitmap, this, getContext(), Arrays.asList(FilterFileAsset.FILTERS)));
        this.image_view_preview = inflate.findViewById(R.id.image_view_preview);
        this.image_view_preview.setImageBitmap(this.bitmap);
        this.textViewTitle = inflate.findViewById(R.id.textViewTitle);
        this.textViewTitle.setText("FILTER");
        (inflate.findViewById(R.id.imageViewSaveFilter)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FilterFragment.this.onFilterSavePhoto.onSaveFilter(((BitmapDrawable) FilterFragment.this.image_view_preview.getDrawable()).getBitmap());
                FilterFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewCloseFilter).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FilterFragment.this.dismiss();
            }
        });
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onFilterSelected(int item, String str)
    {
        new LoadBitmapWithFilter().execute(new String[]{str});
    }

    class LoadBitmapWithFilter extends AsyncTask<String, Bitmap, Bitmap> {
        LoadBitmapWithFilter() {}

        public Bitmap doInBackground(String... strArr) {
            return FilterUtils.getBitmapWithFilter(FilterFragment.this.bitmap, strArr[0]);
        }

        public void onPostExecute(Bitmap bitmap) {
            FilterFragment.this.image_view_preview.setImageBitmap(bitmap);
        }
    }

}
