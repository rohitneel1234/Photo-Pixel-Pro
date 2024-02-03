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
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.adapters.ColoredAdapter;
import com.rohitneel.photopixelpro.photocollage.assets.ColoredCodeAsset;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoColoredView;

public class ColoredFragment extends DialogFragment implements ColoredAdapter.ColoredChangeListener {
    private static final String TAG = "ColoredFragment";
    private RelativeLayout relative_layout_loading;
    public Bitmap adjustBitmap;
    private ImageView backgroundView;
    public Bitmap bitmap;
    public ColoredListener coloredListener;
    private SeekBar seekbarColored;
    public PhotoColoredView quShotColoredView;
    private RecyclerView recyclerViewColored;

    public interface ColoredListener {
        void onSaveMosaic(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static ColoredFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, ColoredListener coloredListener) {
        ColoredFragment coloredFragment = new ColoredFragment();
        coloredFragment.setBitmap(bitmap2);
        coloredFragment.setAdjustBitmap(bitmap3);
        coloredFragment.setColoredListener((ColoredListener) coloredListener);
        coloredFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return coloredFragment;
    }

    public void setColoredListener(ColoredListener coloredListener) {
        this.coloredListener = coloredListener;
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
        View inflate = layoutInflater.inflate(R.layout.fragment_colored, viewGroup, false);
        this.quShotColoredView = inflate.findViewById(R.id.coloredView);
        this.quShotColoredView.setImageBitmap(this.bitmap);
        this.quShotColoredView.setColoredItems(new ColoredAdapter.ColoredItems(R.drawable.colored_1, 0, ColoredAdapter.COLORED.COLOR_1));
        this.backgroundView = inflate.findViewById(R.id.image_view_background);
        this.adjustBitmap = ColoredCodeAsset.getColoredBitmap1(this.bitmap);
        this.backgroundView.setImageBitmap(this.adjustBitmap);
        this.relative_layout_loading = inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading.setVisibility(View.GONE);
        this.seekbarColored = inflate.findViewById(R.id.seekbarColored);
        this.recyclerViewColored = inflate.findViewById(R.id.recyclerViewColored);
        this.recyclerViewColored.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recyclerViewColored.setHasFixedSize(true);
        this.recyclerViewColored.setAdapter(new ColoredAdapter(getContext(), this));
        inflate.findViewById(R.id.imageViewSaveColored).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new SaveMosaicView().execute(new Void[0]);
            }
        });
        inflate.findViewById(R.id.imageViewCloseColored).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ColoredFragment.this.dismiss();
            }
        });
        this.seekbarColored.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                ColoredFragment.this.quShotColoredView.setBrushBitmapSize(i + 25);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                ColoredFragment.this.quShotColoredView.updateBrush();
            }
        });
        inflate.findViewById(R.id.imageViewUndo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ColoredFragment.this.quShotColoredView.undo();
            }
        });
        inflate.findViewById(R.id.imageViewRedo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ColoredFragment.this.quShotColoredView.redo();
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

    public void onSelected(ColoredAdapter.ColoredItems coloredItems) {
        if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_1) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap1(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_2) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap2(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_3) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap3(this.bitmap, -1.0f);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_4) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap4(this.bitmap, 1.0f);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_5) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap5(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_6) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap6(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_7) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap7(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_8) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap8(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_9) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap9(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_10) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap10(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_11) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap11(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_12) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap12(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_13) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap13(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_14) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap14(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_15) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap15(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_16) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap16(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_17) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap17(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_18) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap18(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        } else if (coloredItems.mode == ColoredAdapter.COLORED.COLOR_19) {
            this.adjustBitmap = ColoredCodeAsset.getColoredBitmap19(this.bitmap);
            this.backgroundView.setImageBitmap(this.adjustBitmap);
        }
        this.quShotColoredView.setColoredItems(coloredItems);
    }

    class SaveMosaicView extends AsyncTask<Void, Bitmap, Bitmap> {
        SaveMosaicView() {
        }
        public void onPreExecute() {
            ColoredFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return ColoredFragment.this.quShotColoredView.getBitmap(ColoredFragment.this.bitmap, ColoredFragment.this.adjustBitmap);
        }

        public void onPostExecute(Bitmap bitmap) {
            ColoredFragment.this.mLoading(false);
            ColoredFragment.this.coloredListener.onSaveMosaic(bitmap);
            ColoredFragment.this.dismiss();
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
