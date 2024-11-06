package com.rohitneel.photopixelpro.photocollage.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.adapters.AspectAdapter;
import com.rohitneel.photopixelpro.photocollage.utils.DegreeSeekBar;
import com.rohitneel.photopixelpro.photocollage.utils.FilterUtils;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;
import com.yalantis.ucrop.model.AspectRatio;

public class RatioFragment extends DialogFragment implements AspectAdapter.OnNewSelectedListener{
    private static final String TAG = "RatioFragment";
    private RelativeLayout relative_layout_loading;
    private Bitmap bitmap;
    private Bitmap blurBitmap;
    private ImageView image_view_blur;
    private AspectRatio aspectRatio;
    private DegreeSeekBar adjustSeekBar;
    public RecyclerView recycler_view_ratio;
    public RatioSaveListener ratioSaveListener;
    public ImageView image_view_ratio;
    private ConstraintLayout constraint_layout_ratio;
    public FrameLayout frame_layout_wrapper;

    public interface RatioSaveListener {
        void ratioSavedBitmap(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static RatioFragment show(@NonNull AppCompatActivity appCompatActivity, RatioSaveListener ratioSaveListener, Bitmap mBitmap, Bitmap iBitmap) {
        RatioFragment ratioFragment = new RatioFragment();
        ratioFragment.setBitmap(mBitmap);
        ratioFragment.setBlurBitmap(iBitmap);
        ratioFragment.setRatioSaveListener(ratioSaveListener);
        ratioFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return ratioFragment;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void setRatioSaveListener(RatioSaveListener iRatioSaveListener) {
        this.ratioSaveListener = iRatioSaveListener;
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_ratio, viewGroup, false);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(true);
        aspectRatioPreviewAdapter.setListener(this);
        this.relative_layout_loading = inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading.setVisibility(View.GONE);
        this.recycler_view_ratio = inflate.findViewById(R.id.recyclerViewRatio);
        this.recycler_view_ratio.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        this.aspectRatio = new AspectRatio("",1, 1);

        this.adjustSeekBar = inflate.findViewById(R.id.seekbarOverlay);
        this.adjustSeekBar.setCenterTextColor(getResources().getColor(R.color.mainColor));
        this.adjustSeekBar.setTextColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setPointColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setDegreeRange(0, 50);
        this.adjustSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
            public void onScrollEnd() {
            }

            public void onScrollStart() {
            }

            public void onScroll(int i) {
                int dpToPx = SystemUtil.dpToPx(RatioFragment.this.getContext(), i);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) RatioFragment.this.image_view_ratio.getLayoutParams();
                layoutParams.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
                RatioFragment.this.image_view_ratio.setLayoutParams(layoutParams);
            }
        });
        this.image_view_ratio = inflate.findViewById(R.id.imageViewRatio);
        this.image_view_ratio.setImageBitmap(this.bitmap);
        this.image_view_ratio.setAdjustViewBounds(true);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.constraint_layout_ratio = inflate.findViewById(R.id.constraintLayoutRatio);
        this.image_view_blur = inflate.findViewById(R.id.imageViewBlur);
        this.image_view_blur.setImageBitmap(this.blurBitmap);
        this.frame_layout_wrapper = inflate.findViewById(R.id.frameLayoutWrapper);
        this.frame_layout_wrapper.setLayoutParams(new ConstraintLayout.LayoutParams(point.x, point.x));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_ratio);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 3, this.constraint_layout_ratio.getId(), 3, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 1, this.constraint_layout_ratio.getId(), 1, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 4, this.constraint_layout_ratio.getId(), 4, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 2, this.constraint_layout_ratio.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_ratio);
        inflate.findViewById(R.id.imageViewCloseRatio).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RatioFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewSaveRatio).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new SaveRatioView().execute(getBitmapFromView(RatioFragment.this.frame_layout_wrapper));
            }
        });
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onStop() {
        super.onStop();
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio, Point point) {
        int height = this.constraint_layout_ratio.getHeight();
        if (aspectRatio.getAspectRatioY() > aspectRatio.getAspectRatioX()) {
            int mRatio = (int) (aspectRatio.getAspectRatioY() * ((float) height));
            if (mRatio < point.x) {
                return new int[]{mRatio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio.getAspectRatioX())};
        }
        int iRatio = (int) (((float) point.x) / aspectRatio.getAspectRatioX());
        if (iRatio > height) {
            return new int[]{(int) (((float) height) * aspectRatio.getAspectRatioY()), height};
        }
        return new int[]{point.x, iRatio};
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.aspectRatio = aspectRatio;
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point);
        this.frame_layout_wrapper.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_ratio);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 3, this.constraint_layout_ratio.getId(), 3, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 1, this.constraint_layout_ratio.getId(), 1, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 4, this.constraint_layout_ratio.getId(), 4, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 2, this.constraint_layout_ratio.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_ratio);
    }

    class SaveRatioView extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        SaveRatioView() {
        }

        public void onPreExecute() {
            RatioFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            Bitmap cloneBitmap = FilterUtils.cloneBitmap(bitmapArr[0]);
            bitmapArr[0].recycle();
            bitmapArr[0] = null;
            return cloneBitmap;
        }

        public void onPostExecute(Bitmap bitmap) {
            RatioFragment.this.mLoading(false);
            RatioFragment.this.ratioSaveListener.ratioSavedBitmap(bitmap);
            RatioFragment.this.dismiss();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.blurBitmap != null) {
            this.blurBitmap.recycle();
            this.blurBitmap = null;
        }
        this.bitmap = null;
    }

    public Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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
