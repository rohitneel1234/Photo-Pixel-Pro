package com.rohitneel.photopixelpro.photocollage.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.adapters.MosaicAdapter;
import com.rohitneel.photopixelpro.photocollage.assets.MosaicAsset;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoMosaicView;
import com.rohitneel.photopixelpro.photocollage.utils.DegreeSeekBar;
import com.rohitneel.photopixelpro.photocollage.utils.FilterUtils;

public class MosaicFragment extends DialogFragment implements MosaicAdapter.MosaicChangeListener {
    private static final String TAG = "MosaicFragment";
    private RelativeLayout relative_layout_loading;
    public Bitmap adjustBitmap;
    private ImageView backgroundView;
    public Bitmap bitmap;
    public MosaicListener mosaicListener;
    private DegreeSeekBar adjustSeekBar;
    public PhotoMosaicView queShotMosaicView;
    private RecyclerView recyclerViewMosaic;

    public interface MosaicListener {
        void onSaveMosaic(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static MosaicFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, MosaicListener mosaicDialogListener2) {
        MosaicFragment mosaicDialog = new MosaicFragment();
        mosaicDialog.setBitmap(bitmap2);
        mosaicDialog.setAdjustBitmap(bitmap3);
        mosaicDialog.setMosaicListener((MosaicListener) mosaicDialogListener2);
        mosaicDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return mosaicDialog;
    }

    public void setMosaicListener(MosaicListener mosaicListener) {
        this.mosaicListener = mosaicListener;
    }

    public void setAdjustBitmap(Bitmap bitmap2) {
        this.adjustBitmap = bitmap2;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_mosaic, viewGroup, false);
        this.queShotMosaicView = inflate.findViewById(R.id.mosaic_view);
        this.queShotMosaicView.setImageBitmap(this.bitmap);
        this.queShotMosaicView.setMosaicItem(new MosaicAdapter.MosaicItem(R.drawable.background_blur, 0, MosaicAdapter.BLUR.BLUR));
        this.backgroundView = inflate.findViewById(R.id.image_view_background);
        this.adjustBitmap = FilterUtils.getBlurImageFromBitmap(this.bitmap);
        this.backgroundView.setImageBitmap(this.adjustBitmap);
        this.relative_layout_loading = inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading.setVisibility(View.GONE);
        this.recyclerViewMosaic = inflate.findViewById(R.id.recyclerViewMoasic);
        this.recyclerViewMosaic.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recyclerViewMosaic.setHasFixedSize(true);
        this.recyclerViewMosaic.setAdapter(new MosaicAdapter(getContext(), this));
        inflate.findViewById(R.id.imageViewSaveMosaic).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new SaveMosaicView().execute(new Void[0]);
            }
        });
        inflate.findViewById(R.id.imageViewCloseMosaic).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MosaicFragment.this.dismiss();
            }
        });
        this.adjustSeekBar = inflate.findViewById(R.id.seekbarMoasic);
        this.adjustSeekBar.setCenterTextColor(getResources().getColor(R.color.mainColor));
        this.adjustSeekBar.setTextColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setPointColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setDegreeRange(0, 70);
        this.adjustSeekBar.setCurrentDegrees(15);
        this.adjustSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
            public void onScrollEnd() {
            }

            public void onScrollStart() {
            }

            public void onScroll(int i) {
                MosaicFragment.this.queShotMosaicView.setBrushBitmapSize(i + 15);
                MosaicFragment.this.queShotMosaicView.updateBrush();
            }
        });

        inflate.findViewById(R.id.imageViewUndo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MosaicFragment.this.queShotMosaicView.undo();
            }
        });
        inflate.findViewById(R.id.imageViewRedo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MosaicFragment.this.queShotMosaicView.redo();
            }
        });
        return inflate;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.bitmap.recycle();
        this.bitmap = null;
        this.adjustBitmap.recycle();
        this.adjustBitmap = null;
    }

    public void onStop() {
        super.onStop();
    }

    public void onSelected(MosaicAdapter.MosaicItem mosaicItem) {
        if (mosaicItem.mode == MosaicAdapter.BLUR.BLUR) {
            this.adjustBitmap = FilterUtils.getBlurImageFromBitmap(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        } else if (mosaicItem.mode == MosaicAdapter.BLUR.MOSAIC) {
            this.adjustBitmap = MosaicAsset.getMosaic(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }
        this.queShotMosaicView.setMosaicItem(mosaicItem);
    }

    class SaveMosaicView extends AsyncTask<Void, Bitmap, Bitmap> {
        SaveMosaicView() {
        }
        public void onPreExecute() {
            MosaicFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return MosaicFragment.this.queShotMosaicView.getBitmap(MosaicFragment.this.bitmap, MosaicFragment.this.adjustBitmap);
        }

        public void onPostExecute(Bitmap bitmap) {
            MosaicFragment.this.mLoading(false);
            MosaicFragment.this.mosaicListener.onSaveMosaic(bitmap);
            MosaicFragment.this.dismiss();
        }
    }

    public void mLoading(boolean z) {
        if (z) {
            getActivity().getWindow().setFlags(16, 16);
            this.relative_layout_loading.setVisibility(View.VISIBLE);
            return;
        }
        getActivity().getWindow().clearFlags(16);
        this.relative_layout_loading.setVisibility(View.GONE);
    }

}
