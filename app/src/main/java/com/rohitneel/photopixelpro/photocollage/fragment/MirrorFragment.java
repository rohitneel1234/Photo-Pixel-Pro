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
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.adapters.Mirror2DAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.Mirror3DAdapter;
import com.rohitneel.photopixelpro.photocollage.mirror.Mirror2D_2Layer;
import com.rohitneel.photopixelpro.photocollage.mirror.Mirror2D_3L;
import com.rohitneel.photopixelpro.photocollage.mirror.Mirror2D_3Layer;
import com.rohitneel.photopixelpro.photocollage.mirror.Mirror3D_2Layer;
import com.rohitneel.photopixelpro.photocollage.mirror.Mirror3D_4Layer;
import com.rohitneel.photopixelpro.photocollage.utils.FilterUtils;
import com.rohitneel.photopixelpro.photocollage.widget.SquareLayout;
import com.rohitneel.photopixelpro.photocollage.widget.SquareLayout;

public class MirrorFragment extends DialogFragment implements Mirror3DAdapter.Mirror3Listener, Mirror2DAdapter.Mirror2Listener {
    private static final String TAG = "MirrorFragment";
    private RelativeLayout relative_layout_loading;
    private Bitmap bitmap;
    private Bitmap blurBitmap;

    public FrameLayout frame_layout_wrapper, frame;
    public FrameFragment.RatioSaveListener ratioSaveListener;
    private RelativeLayout relativeLayout2D, relativeLayout3D;
    private View view_2D, view_3D;
    private SquareLayout squareLayout3D_1;
    private SquareLayout squareLayout3D_3;
    //2D
    private SquareLayout squareLayout2D_1;
    Mirror3D_2Layer dragLayout2D_1, dragLayout2D_2;
    ImageView imageView2D_1, imageView2D_2;

    private SquareLayout squareLayout2D_3;
    Mirror2D_2Layer dragLayout2D_3, dragLayout2D_4;
    ImageView imageView2D_3, imageView2D_4;

    private SquareLayout squareLayout2D_5;
    Mirror3D_2Layer dragLayout2D_5, dragLayout2D_6;
    ImageView imageView2D_5, imageView2D_6;

    private SquareLayout squareLayout2D_7;
    Mirror2D_3Layer dragLayout2D_7, dragLayout2D_8;
    ImageView imageView2D_7, imageView2D_8;

    private SquareLayout squareLayout2D_9;
    Mirror2D_3Layer dragLayout2D_9, dragLayout2D_10;
    ImageView imageView2D_9, imageView2D_10;

    private SquareLayout squareLayout2D_11;
    Mirror2D_3L dragLayout2D_11, dragLayout2D_12, dragLayout2D_13;
    ImageView imageView2D_11, imageView2D_12, imageView2D_13;
    //3D
    Mirror3D_2Layer dragLayout3D_1, dragLayout3D_2;
    Mirror3D_4Layer dragLayout3D_3, dragLayout3D_4, dragLayout3D_5, dragLayout3D_6;
    ImageView imageView3D_1, imageView3D_2;
    ImageView imageView3D_3, imageView3D_4, imageView3D_5, imageView3D_6;
    public RecyclerView recycler_view_background;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static MirrorFragment show(@NonNull AppCompatActivity appCompatActivity, FrameFragment.RatioSaveListener ratioSaveListener, Bitmap mBitmap, Bitmap iBitmap) {
        MirrorFragment ratioFragment = new MirrorFragment();
        ratioFragment.setBitmap(mBitmap);
        ratioFragment.setBlurBitmap(iBitmap);
        ratioFragment.setRatioSaveListener(ratioSaveListener);
        ratioFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return ratioFragment;
    }
    public void setRatioSaveListener(FrameFragment.RatioSaveListener iRatioSaveListener) {
        this.ratioSaveListener = iRatioSaveListener;
    }
    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_mirror, viewGroup, false);
        initViews(inflate);
        return inflate;
    }

    private void initViews(View inflate) {
        this.relative_layout_loading = inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading.setVisibility(View.GONE);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.frame_layout_wrapper = inflate.findViewById(R.id.frameLayoutWrapper);
        this.frame = inflate.findViewById(R.id.frameLayout3D_1);
        inflate.findViewById(R.id.imageViewCloseMirror).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MirrorFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewSaveMirror).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new SaveRatioView().execute(getBitmapFromView(MirrorFragment.this.frame_layout_wrapper));
            }
        });

        this.recycler_view_background = inflate.findViewById(R.id.recyclerViewMirror);
        this.recycler_view_background.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_background.setAdapter(new Mirror3DAdapter(getContext(), this));
        this.recycler_view_background.setVisibility(View.VISIBLE);

        view_2D = inflate.findViewById(R.id.view_2D);
        view_3D = inflate.findViewById(R.id.view_3D);
        relativeLayout2D = inflate.findViewById(R.id._2d_layout);
        relativeLayout3D = inflate.findViewById(R.id._3d_layout);
        relativeLayout2D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycler_view_background.setAdapter(new Mirror2DAdapter(getContext(), MirrorFragment.this));
                view_2D.setVisibility(View.VISIBLE);
                view_3D.setVisibility(View.INVISIBLE);

            }
        });
        relativeLayout3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycler_view_background.setAdapter(new Mirror3DAdapter(getContext(), MirrorFragment.this));
                view_3D.setVisibility(View.VISIBLE);
                view_2D.setVisibility(View.INVISIBLE);
            }
        });

        this.squareLayout2D_1 = inflate.findViewById(R.id.squareLayout2d_1);
        this.squareLayout2D_1.setVisibility(View.GONE);
        this.squareLayout2D_3 = inflate.findViewById(R.id.squareLayout2d_3);
        this.squareLayout2D_3.setVisibility(View.GONE);
        this.squareLayout2D_5 = inflate.findViewById(R.id.squareLayout2d_5);
        this.squareLayout2D_5.setVisibility(View.GONE);
        this.squareLayout2D_7 = inflate.findViewById(R.id.squareLayout2d_7);
        this.squareLayout2D_7.setVisibility(View.GONE);
        this.squareLayout2D_9 = inflate.findViewById(R.id.squareLayout2d_9);
        this.squareLayout2D_9.setVisibility(View.GONE);
        this.squareLayout2D_11 = inflate.findViewById(R.id.squareLayout2d_11);
        this.squareLayout2D_11.setVisibility(View.GONE);
        this.squareLayout3D_1 = inflate.findViewById(R.id.squareLayout3d_1);
        this.squareLayout3D_1.setVisibility(View.VISIBLE);
        this.squareLayout3D_3 = inflate.findViewById(R.id.squareLayout3d_3);
        this.squareLayout3D_3.setVisibility(View.GONE);
        this.frame.setVisibility(View.VISIBLE);
        //2D
        this.dragLayout2D_1 = inflate.findViewById(R.id.drag2D_1);
        this.dragLayout2D_2 = inflate.findViewById(R.id.drag2D_2);
        dragLayout2D_1.init(getContext(),dragLayout2D_2);
        dragLayout2D_2.init(getContext(),dragLayout2D_1);
        dragLayout2D_1.applyScaleAndTranslation();
        dragLayout2D_2.applyScaleAndTranslation();
        this.imageView2D_1 = inflate.findViewById(R.id.imageView2D_1);
        this.imageView2D_1.setImageBitmap(this.bitmap);
        this.imageView2D_1.setAdjustViewBounds(true);
        this.imageView2D_2 = inflate.findViewById(R.id.imageView2D_2);
        this.imageView2D_2.setImageBitmap(this.bitmap);
        this.imageView2D_2.setAdjustViewBounds(true);

        this.dragLayout2D_3 = inflate.findViewById(R.id.drag2D_3);
        this.dragLayout2D_4 = inflate.findViewById(R.id.drag2D_4);
        dragLayout2D_3.init(getContext(),dragLayout2D_4);
        dragLayout2D_4.init(getContext(),dragLayout2D_3);
        dragLayout2D_3.applyScaleAndTranslation();
        dragLayout2D_4.applyScaleAndTranslation();
        this.imageView2D_3 = inflate.findViewById(R.id.imageView2D_3);
        this.imageView2D_3.setImageBitmap(this.bitmap);
        this.imageView2D_3.setAdjustViewBounds(true);
        this.imageView2D_4 = inflate.findViewById(R.id.imageView2D_4);
        this.imageView2D_4.setImageBitmap(this.bitmap);
        this.imageView2D_4.setAdjustViewBounds(true);

        this.dragLayout2D_5 = inflate.findViewById(R.id.drag2D_5);
        this.dragLayout2D_6 = inflate.findViewById(R.id.drag2D_6);
        dragLayout2D_5.init(getContext(),dragLayout2D_6);
        dragLayout2D_6.init(getContext(),dragLayout2D_5);
        dragLayout2D_5.applyScaleAndTranslation();
        dragLayout2D_6.applyScaleAndTranslation();
        this.imageView2D_5 = inflate.findViewById(R.id.imageView2D_5);
        this.imageView2D_5.setImageBitmap(this.bitmap);
        this.imageView2D_5.setAdjustViewBounds(true);
        this.imageView2D_6 = inflate.findViewById(R.id.imageView2D_6);
        this.imageView2D_6.setImageBitmap(this.bitmap);
        this.imageView2D_6.setAdjustViewBounds(true);

        this.dragLayout2D_7 = inflate.findViewById(R.id.drag2D_7);
        this.dragLayout2D_8 = inflate.findViewById(R.id.drag2D_8);
        dragLayout2D_7.init(getContext(),dragLayout2D_8,true);
        dragLayout2D_8.init(getContext(),dragLayout2D_7,true);
        dragLayout2D_7.applyScaleAndTranslation();
        dragLayout2D_8.applyScaleAndTranslation();
        this.imageView2D_7 = inflate.findViewById(R.id.imageView2D_7);
        this.imageView2D_7.setImageBitmap(this.bitmap);
        this.imageView2D_7.setAdjustViewBounds(true);
        this.imageView2D_8 = inflate.findViewById(R.id.imageView2D_8);
        this.imageView2D_8.setImageBitmap(this.bitmap);
        this.imageView2D_8.setAdjustViewBounds(true);

        this.dragLayout2D_9 = inflate.findViewById(R.id.drag2D_9);
        this.dragLayout2D_10 = inflate.findViewById(R.id.drag2D_10);
        dragLayout2D_9.init(getContext(),dragLayout2D_10,true);
        dragLayout2D_10.init(getContext(),dragLayout2D_9,true);
        dragLayout2D_9.applyScaleAndTranslation();
        dragLayout2D_10.applyScaleAndTranslation();
        this.imageView2D_9 = inflate.findViewById(R.id.imageView2D_9);
        this.imageView2D_9.setImageBitmap(this.bitmap);
        this.imageView2D_9.setAdjustViewBounds(true);
        this.imageView2D_10 = inflate.findViewById(R.id.imageView2D_10);
        this.imageView2D_10.setImageBitmap(this.bitmap);
        this.imageView2D_10.setAdjustViewBounds(true);

        this.dragLayout2D_11 = inflate.findViewById(R.id.drag2D_11);
        this.dragLayout2D_12 = inflate.findViewById(R.id.drag2D_12);
        this.dragLayout2D_13 = inflate.findViewById(R.id.drag2D_13);
        dragLayout2D_11.init(getContext(), dragLayout2D_12, dragLayout2D_13);
        dragLayout2D_12.init(getContext(), dragLayout2D_13, dragLayout2D_11);
        dragLayout2D_13.init(getContext(), dragLayout2D_11, dragLayout2D_12);
        dragLayout2D_11.applyScaleAndTranslation();
        dragLayout2D_12.applyScaleAndTranslation();
        dragLayout2D_13.applyScaleAndTranslation();
        this.imageView2D_11 = inflate.findViewById(R.id.imageView2D_11);
        this.imageView2D_11.setImageBitmap(this.bitmap);
        this.imageView2D_11.setAdjustViewBounds(true);
        this.imageView2D_12 = inflate.findViewById(R.id.imageView2D_12);
        this.imageView2D_12.setImageBitmap(this.bitmap);
        this.imageView2D_12.setAdjustViewBounds(true);
        this.imageView2D_13 = inflate.findViewById(R.id.imageView2D_13);
        this.imageView2D_13.setImageBitmap(this.bitmap);
        this.imageView2D_13.setAdjustViewBounds(true);

        //3D
        this.dragLayout3D_1 = inflate.findViewById(R.id.drag3D_1);
        this.dragLayout3D_2 = inflate.findViewById(R.id.drag3D_2);
        dragLayout3D_1.init(getContext(),dragLayout3D_2);
        dragLayout3D_2.init(getContext(),dragLayout3D_1);
        dragLayout3D_1.applyScaleAndTranslation();
        dragLayout3D_2.applyScaleAndTranslation();
        this.imageView3D_1 = inflate.findViewById(R.id.imageView3D_1);
        this.imageView3D_1.setImageBitmap(this.bitmap);
        this.imageView3D_1.setAdjustViewBounds(true);
        this.imageView3D_2 = inflate.findViewById(R.id.imageView3D_2);
        this.imageView3D_2.setImageBitmap(this.bitmap);
        this.imageView3D_2.setAdjustViewBounds(true);

        this.dragLayout3D_3 = inflate.findViewById(R.id.drag3D_3);
        this.dragLayout3D_4 = inflate.findViewById(R.id.drag3D_4);
        this.dragLayout3D_5 = inflate.findViewById(R.id.drag3D_5);
        this.dragLayout3D_6 = inflate.findViewById(R.id.drag3D_6);
        dragLayout3D_3.init(getContext(),dragLayout3D_4, dragLayout3D_5, dragLayout3D_6);
        dragLayout3D_4.init(getContext(),dragLayout3D_5, dragLayout3D_6, dragLayout3D_3);
        dragLayout3D_5.init(getContext(),dragLayout3D_6, dragLayout3D_3, dragLayout3D_4);
        dragLayout3D_6.init(getContext(),dragLayout3D_3, dragLayout3D_4, dragLayout3D_5);
        dragLayout3D_3.applyScaleAndTranslation();
        dragLayout3D_4.applyScaleAndTranslation();
        dragLayout3D_5.applyScaleAndTranslation();
        dragLayout3D_6.applyScaleAndTranslation();
        this.imageView3D_3 = inflate.findViewById(R.id.imageView3D_3);
        this.imageView3D_3.setImageBitmap(this.bitmap);
        this.imageView3D_3.setAdjustViewBounds(true);
        this.imageView3D_4 = inflate.findViewById(R.id.imageView3D_4);
        this.imageView3D_4.setImageBitmap(this.bitmap);
        this.imageView3D_4.setAdjustViewBounds(true);
        this.imageView3D_5 = inflate.findViewById(R.id.imageView3D_5);
        this.imageView3D_5.setImageBitmap(this.bitmap);
        this.imageView3D_5.setAdjustViewBounds(true);
        this.imageView3D_6 = inflate.findViewById(R.id.imageView3D_6);
        this.imageView3D_6.setImageBitmap(this.bitmap);
        this.imageView3D_6.setAdjustViewBounds(true);

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

    @Override
    public void onMirrorSelected(Mirror3DAdapter.SquareView squareView) {
        this.frame.setBackgroundResource(squareView.mirror);
        this.frame.setVisibility(View.VISIBLE);
        this.squareLayout2D_1.setVisibility(View.GONE);
        this.squareLayout2D_3.setVisibility(View.GONE);
        this.squareLayout2D_5.setVisibility(View.GONE);
        if(squareView.text.equals("3D-1") || squareView.text.equals("3D-2") || squareView.text.equals("3D-3")
        || squareView.text.equals("3D-4") || squareView.text.equals("3D-5") || squareView.text.equals("3D-6")
        || squareView.text.equals("3D-7") || squareView.text.equals("3D-8") || squareView.text.equals("3D-9")
        || squareView.text.equals("3D-10") || squareView.text.equals("3D-11") || squareView.text.equals("3D-12")){

            squareLayout3D_1.setVisibility(View.VISIBLE);
            squareLayout3D_3.setVisibility(View.GONE);

        }  else if(squareView.text.equals("3D-13") || squareView.text.equals("3D-14") || squareView.text.equals("3D-15")){
            squareLayout3D_1.setVisibility(View.GONE);
            squareLayout3D_3.setVisibility(View.VISIBLE);
        }
        this.frame_layout_wrapper.invalidate();
    }

    @Override
    public void onBackgroundSelected(Mirror2DAdapter.SquareView squareView) {
        this.frame.setVisibility(View.GONE);
        squareLayout3D_1.setVisibility(View.GONE);
        squareLayout3D_3.setVisibility(View.GONE);
        if(squareView.text.equals("2D-1")){

            this.squareLayout2D_1.setVisibility(View.VISIBLE);
            this.squareLayout2D_3.setVisibility(View.GONE);
            this.squareLayout2D_5.setVisibility(View.GONE);
            this.squareLayout2D_7.setVisibility(View.GONE);
            this.squareLayout2D_9.setVisibility(View.GONE);
            this.squareLayout2D_11.setVisibility(View.GONE);

        } else if(squareView.text.equals("2D-2")){

            this.squareLayout2D_1.setVisibility(View.GONE);
            this.squareLayout2D_3.setVisibility(View.VISIBLE);
            this.squareLayout2D_5.setVisibility(View.GONE);
            this.squareLayout2D_7.setVisibility(View.GONE);
            this.squareLayout2D_9.setVisibility(View.GONE);
            this.squareLayout2D_11.setVisibility(View.GONE);

        } else if(squareView.text.equals("2D-3")){

            this.squareLayout2D_1.setVisibility(View.GONE);
            this.squareLayout2D_3.setVisibility(View.GONE);
            this.squareLayout2D_5.setVisibility(View.VISIBLE);
            this.squareLayout2D_7.setVisibility(View.GONE);
            this.squareLayout2D_9.setVisibility(View.GONE);
            this.squareLayout2D_11.setVisibility(View.GONE);

        } else if(squareView.text.equals("2D-4")){

            this.squareLayout2D_1.setVisibility(View.GONE);
            this.squareLayout2D_3.setVisibility(View.GONE);
            this.squareLayout2D_5.setVisibility(View.GONE);
            this.squareLayout2D_7.setVisibility(View.VISIBLE);
            this.squareLayout2D_9.setVisibility(View.GONE);
            this.squareLayout2D_11.setVisibility(View.GONE);

        } else if(squareView.text.equals("2D-5")){

            this.squareLayout2D_1.setVisibility(View.GONE);
            this.squareLayout2D_3.setVisibility(View.GONE);
            this.squareLayout2D_5.setVisibility(View.GONE);
            this.squareLayout2D_7.setVisibility(View.GONE);
            this.squareLayout2D_9.setVisibility(View.VISIBLE);
            this.squareLayout2D_11.setVisibility(View.GONE);

        } else if(squareView.text.equals("2D-6")){

            this.squareLayout2D_1.setVisibility(View.GONE);
            this.squareLayout2D_3.setVisibility(View.GONE);
            this.squareLayout2D_5.setVisibility(View.GONE);
            this.squareLayout2D_7.setVisibility(View.GONE);
            this.squareLayout2D_9.setVisibility(View.GONE);
            this.squareLayout2D_11.setVisibility(View.VISIBLE);

        }
        this.frame_layout_wrapper.invalidate();
    }

    class SaveRatioView extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        SaveRatioView() {
        }

        public void onPreExecute() {
            MirrorFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            Bitmap cloneBitmap = FilterUtils.cloneBitmap(getContext(), bitmapArr[0]);
            bitmapArr[0].recycle();
            bitmapArr[0] = null;
            return cloneBitmap;
        }

        public void onPostExecute(Bitmap bitmap) {
            MirrorFragment.this.mLoading(false);
            MirrorFragment. this.ratioSaveListener.ratioSavedBitmap(bitmap);
//              imageViewMirror3d2.setImageBitmap(bitmap);
            MirrorFragment.this.dismiss();
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
