package com.rohitneel.photopixelpro.photocollage.activities;

import static com.rohitneel.photopixelpro.fragments.HomeFragment.KEY_DATA_RESULT;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hold1.keyboardheightprovider.KeyboardHeightProvider;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.constant.CommonKeys;
import com.rohitneel.photopixelpro.photocollage.adapters.AspectAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.CollageBackgroundAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.CollageColorAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.FilterAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.GridAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.GridItemToolsAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.GridToolsAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.RecyclerTabLayout;
import com.rohitneel.photopixelpro.photocollage.adapters.StickerAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.StickersTabAdapter;
import com.rohitneel.photopixelpro.photocollage.assets.FilterFileAsset;
import com.rohitneel.photopixelpro.photocollage.assets.StickerFileAsset;
import com.rohitneel.photopixelpro.photocollage.event.AlignHorizontallyEvent;
import com.rohitneel.photopixelpro.photocollage.event.DeleteIconEvent;
import com.rohitneel.photopixelpro.photocollage.event.EditTextIconEvent;
import com.rohitneel.photopixelpro.photocollage.event.FlipHorizontallyEvent;
import com.rohitneel.photopixelpro.photocollage.event.ZoomIconEvent;
import com.rohitneel.photopixelpro.photocollage.fragment.CropFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.FilterFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.TextFragment;
import com.rohitneel.photopixelpro.photocollage.listener.FilterListener;
import com.rohitneel.photopixelpro.photocollage.module.Module;
import com.rohitneel.photopixelpro.photocollage.picker.PermissionsUtils;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoGridView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoPickerView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoStickerIcons;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoStickerView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoText;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoTextView;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoGrid;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayoutParser;
import com.rohitneel.photopixelpro.photocollage.preference.Preference;
import com.rohitneel.photopixelpro.photocollage.sticker.DrawableSticker;
import com.rohitneel.photopixelpro.photocollage.sticker.Sticker;
import com.rohitneel.photopixelpro.photocollage.utils.CollageUtils;
import com.rohitneel.photopixelpro.photocollage.utils.FilterUtils;
import com.rohitneel.photopixelpro.photocollage.utils.SaveFileUtils;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;
import com.rohitneel.photopixelpro.photoframe.utils.PathUtills;
import com.squareup.picasso.Target;
import com.yalantis.ucrop.model.AspectRatio;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class PhotoCollageActivity extends PhotoBaseActivity implements GridToolsAdapter.OnItemSelected,
        AspectAdapter.OnNewSelectedListener, StickerAdapter.OnClickSplashListener,
        CollageBackgroundAdapter.BackgroundGridListener, FilterListener,
        CropFragment.OnCropPhoto, CollageColorAdapter.BackgroundColorListener,
        FilterFragment.OnFilterSavePhoto, GridItemToolsAdapter.OnPieceFuncItemSelected,
        GridAdapter.OnItemClickListener {
    private static PhotoCollageActivity QueShotGridActivityInstance;
    public static PhotoCollageActivity QueShotGridActivityCollage;
    public PhotoLayout queShotLayout;
    public PhotoGridView queShotGridView;
    public AspectRatio aspectRatio;
    public CollageBackgroundAdapter.SquareView currentBackgroundState;
    private RelativeLayout relativeLayoutLoading;
    public GridToolsAdapter gridToolsAdapter = new GridToolsAdapter(this, true);
    private GridItemToolsAdapter gridItemToolsAdapter = new GridItemToolsAdapter(this);
    public LinearLayout linear_layout_wrapper_sticker_list;
    public Module moduleToolsId;
    public ImageView imageViewAddSticker;
    public float BorderRadius;
    public float Padding;
    private int deviceHeight = 0;
    public int deviceWidth = 0;
    // Guideline
    private Guideline guidelineTools;
    private Guideline guideline;
    public TextFragment.TextEditor textEditor;
    public TextFragment addTextFragment;
    private KeyboardHeightProvider keyboardHeightProvider;
    // ConstraintLayout
    public ConstraintLayout constraint_layout_change_background;
    public ConstraintLayout constrant_layout_change_Layout;
    public ConstraintLayout constraint_layout_filter_layout;
    private ConstraintLayout constraint_layout_collage_layout;
    private ConstraintLayout constraint_save_control;
    private ConstraintLayout constraint_layout_wrapper_collage_view;
    public ConstraintLayout constraint_layout_sticker;
    private ConstraintLayout constraintLayoutSaveText;
    private ConstraintLayout constraintLayoutSaveSticker;
    private ConstraintLayout constraintLayoutAddText;
    // RecyclerView
    public RecyclerView recyclerViewTools;
    public RecyclerView recyclerViewFilter;
    private RecyclerView recycler_view_collage;
    private RecyclerView recycler_view_ratio;
    private RecyclerView recycler_view_blur;
    private RecyclerView recycler_view_color;
    private RecyclerView recycler_view_gradient;
    public RecyclerView recyclerViewToolsCollage;
    // ArrayList
    public ArrayList listFilterAll = new ArrayList<>();
    public List<Drawable> drawableList = new ArrayList<>();
    public List<String> stringList;
    public List<Target> targets = new ArrayList();
    // TextView
    private LinearLayout linearLayoutBorder;
    private RelativeLayout relativeLayoutAddText;
    private TextView text_view_save;
    private TextView textViewSeekBarPadding;
    private TextView textViewSeekBarRadius;
    public TextView textViewCancel;
    public TextView textViewDiscard;
    // SeekBar
    private SeekBar seekBarRadius;
    private SeekBar seekBarPadding;
    public SeekBar seekbarSticker;
    // Ads
    private LinearLayout bannerContainer;

    private Animation slideUpAnimation, slideDownAnimation;

    private LinearLayout linearLayoutLayer;
    private LinearLayout linearLayoutRatio;
    private LinearLayout linearLayoutBorde;
    private TextView textViewListLayer;
    private TextView textViewListRatio;
    private TextView textViewListBorder;
    private View viewCollage;
    private View viewBorder;
    private View viewRatio;

    private LinearLayout linearLayoutColor;
    private LinearLayout linearLayoutGradient;
    private LinearLayout linearLayoutBlur;
    private TextView textViewListColor;
    private TextView textViewListGradient;
    private TextView textViewListBlur;
    private View viewColor;
    private View viewGradient;
    private View viewBlur;


    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.login_sign_up_background));
        } else {
            setFullScreen();
        }
        setContentView(R.layout.activity_photo_collage);
        if (Build.VERSION.SDK_INT < 30) {
            getWindow().setSoftInputMode(72);
        }
        this.deviceWidth = getResources().getDisplayMetrics().widthPixels;
        this.deviceHeight = getResources().getDisplayMetrics().heightPixels;

        findViewById(R.id.image_view_exit).setOnClickListener(view -> PhotoCollageActivity.this.onBackPressed());
        this.queShotGridView = findViewById(R.id.collage_view);
        this.bannerContainer = findViewById(R.id.bannerContainer);
        this.constraintLayoutSaveText = findViewById(R.id.constraint_layout_confirm_save_text);
        this.constraintLayoutSaveSticker = findViewById(R.id.constraint_layout_confirm_save_sticker);
        this.constraint_layout_wrapper_collage_view = findViewById(R.id.constraint_layout_wrapper_collage_view);
        this.constraint_layout_filter_layout = findViewById(R.id.constraint_layout_filter_layout);
        this.recyclerViewTools = findViewById(R.id.recycler_view_tools);
        this.recyclerViewTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewTools.setAdapter(this.gridToolsAdapter);
        this.recyclerViewToolsCollage = findViewById(R.id.recycler_view_tools_collage);
        this.recyclerViewToolsCollage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewToolsCollage.setAdapter(this.gridItemToolsAdapter);
        this.seekBarPadding = findViewById(R.id.seekbar_border);
        this.seekBarPadding.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        this.seekBarRadius = findViewById(R.id.seekbar_radius);
        this.seekBarRadius.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        this.stringList = getIntent().getStringArrayListExtra(KEY_DATA_RESULT);
        this.relativeLayoutLoading = findViewById(R.id.relative_layout_loading);
        this.recyclerViewFilter = findViewById(R.id.recycler_view_filter);
        this.linearLayoutBorder = findViewById(R.id.linearLayoutPadding);
        this.guidelineTools = findViewById(R.id.guidelineTools);
        this.guideline = findViewById(R.id.guideline);
        this.relativeLayoutAddText = findViewById(R.id.relative_layout_add_text);
        this.relativeLayoutAddText.setVisibility(View.GONE);
        this.constraintLayoutAddText = findViewById(R.id.constraint_layout_confirm_text);
        this.queShotLayout = CollageUtils.getCollageLayouts(this.stringList.size()).get(0);
        this.queShotGridView.setQueShotLayout(this.queShotLayout);
        this.queShotGridView.setTouchEnable(true);
        this.queShotGridView.setNeedDrawLine(false);
        this.queShotGridView.setNeedDrawOuterLine(false);
        this.queShotGridView.setLineSize(4);
        this.queShotGridView.setCollagePadding(6.0f);
        this.queShotGridView.setCollageRadian(15.0f);
        this.queShotGridView.setLineColor(ContextCompat.getColor(this, R.color.white));
        this.queShotGridView.setSelectedLineColor(ContextCompat.getColor(this, R.color.mainColor));
        this.queShotGridView.setHandleBarColor(ContextCompat.getColor(this, R.color.mainColor));
        this.queShotGridView.setAnimateDuration(300);
        this.queShotGridView.setOnQueShotSelectedListener((collage, i) -> {
            PhotoCollageActivity.this.recyclerViewTools.setVisibility(View.GONE);
            PhotoCollageActivity.this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
            PhotoCollageActivity.this.slideUp(PhotoCollageActivity.this.recyclerViewToolsCollage);
            PhotoCollageActivity.this.setGoneSave();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) PhotoCollageActivity.this.recyclerViewToolsCollage.getLayoutParams();
            layoutParams.bottomMargin = SystemUtil.dpToPx(PhotoCollageActivity.this.getApplicationContext(), 10);
            PhotoCollageActivity.this.recyclerViewToolsCollage.setLayoutParams(layoutParams);
            PhotoCollageActivity.this.moduleToolsId = Module.COLLAGE;
        });
        this.queShotGridView.setOnQueShotUnSelectedListener(() -> {
            PhotoCollageActivity.this.recyclerViewToolsCollage.setVisibility(View.GONE);
            PhotoCollageActivity.this.recyclerViewTools.setVisibility(View.VISIBLE);
            setVisibleSave();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerViewToolsCollage.getLayoutParams();
            layoutParams.bottomMargin = 0;
            recyclerViewToolsCollage.setLayoutParams(layoutParams);
            moduleToolsId = Module.NONE;
        });

        this.constraint_save_control = findViewById(R.id.constraint_save_control);
        this.queShotGridView.post(() -> PhotoCollageActivity.this.loadPhoto());
        findViewById(R.id.imageViewSaveLayer).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseLayer).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSaveText).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseText).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewClosebackground).setOnClickListener(this.onClickListener);
        findViewById(R.id.image_view_close_sticker).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSaveFilter).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSavebackground).setOnClickListener(this.onClickListener);
        findViewById(R.id.image_view_save_sticker).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseFilter).setOnClickListener(this.onClickListener);
        this.linearLayoutLayer = findViewById(R.id.linearLayoutCollage);
        this.linearLayoutBorde = findViewById(R.id.linearLayoutBorder);
        this.linearLayoutRatio = findViewById(R.id.linearLayoutRatio);
        this.textViewListLayer = findViewById(R.id.text_view_collage);
        this.textViewListBorder = findViewById(R.id.text_view_border);
        this.textViewListRatio = findViewById(R.id.text_view_ratio);
        this.viewCollage = findViewById(R.id.view_collage);
        this.viewBorder = findViewById(R.id.view_border);
        this.viewRatio = findViewById(R.id.view_ratio);
        this.linearLayoutLayer.setOnClickListener(view -> PhotoCollageActivity.this.setLayer());
        this.linearLayoutBorde.setOnClickListener(view -> PhotoCollageActivity.this.setBorder());
        this.linearLayoutRatio.setOnClickListener(view -> PhotoCollageActivity.this.setRatio());

        this.linearLayoutColor = findViewById(R.id.linearLayoutColor);
        this.linearLayoutGradient = findViewById(R.id.linearLayoutGradient);
        this.linearLayoutBlur = findViewById(R.id.linearLayoutBlur);
        this.textViewListColor = findViewById(R.id.text_view_color);
        this.textViewListGradient = findViewById(R.id.text_view_gradient);
        this.textViewListBlur = findViewById(R.id.text_view_blur);
        this.viewGradient = findViewById(R.id.view_gradient);
        this.viewBlur = findViewById(R.id.view_blur);
        this.viewColor = findViewById(R.id.view_color);
        this.linearLayoutColor.setOnClickListener(view -> PhotoCollageActivity.this.setBackgroundColor());
        this.linearLayoutGradient.setOnClickListener(view -> PhotoCollageActivity.this.setBackgroundGradient());
        this.linearLayoutBlur.setOnClickListener(view -> PhotoCollageActivity.this.selectBackgroundBlur());

        this.constrant_layout_change_Layout = findViewById(R.id.constrant_layout_change_Layout);
        this.textViewSeekBarPadding = findViewById(R.id.seekbarPadding);
        this.textViewSeekBarRadius = findViewById(R.id.seekbarRadius);
        GridAdapter collageAdapter = new GridAdapter();
        this.recycler_view_collage = findViewById(R.id.recycler_view_collage);
        this.recycler_view_collage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_collage.setAdapter(collageAdapter);
        collageAdapter.refreshData(CollageUtils.getCollageLayouts(this.stringList.size()), null);
        collageAdapter.setOnItemClickListener(this);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(true);
        aspectRatioPreviewAdapter.setListener(this);
        this.recycler_view_ratio = findViewById(R.id.recycler_view_ratio);
        this.recycler_view_ratio.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        this.linear_layout_wrapper_sticker_list = findViewById(R.id.linear_layout_wrapper_sticker_list);
        ViewPager stickerViewPager = findViewById(R.id.stickerViewpaper);
        this.constraint_layout_sticker = findViewById(R.id.constraint_layout_sticker);
        this.seekbarSticker = findViewById(R.id.seekbarStickerAlpha);
        this.seekbarSticker.setVisibility(View.GONE);
        this.seekbarSticker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = PhotoCollageActivity.this.queShotGridView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });

        this.relativeLayoutAddText.setOnClickListener(view -> {
            PhotoCollageActivity.this.queShotGridView.setHandlingSticker(null);
            PhotoCollageActivity.this.openTextFragment();
        });

        this.text_view_save = findViewById(R.id.text_view_save);
        this.text_view_save.setOnClickListener(view -> {
            SaveView();
        });
        this.imageViewAddSticker = findViewById(R.id.imageViewAddSticker);
        this.imageViewAddSticker.setVisibility(View.GONE);
        this.imageViewAddSticker.setOnClickListener(view -> {
            imageViewAddSticker.setVisibility(View.GONE);
            linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
        });

        PhotoStickerIcons quShotStickerIconClose = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, PhotoStickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        PhotoStickerIcons quShotStickerIconScale = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, PhotoStickerIcons.SCALE);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        PhotoStickerIcons quShotStickerIconFlip = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, PhotoStickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        PhotoStickerIcons quShotStickerIconCenter = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, PhotoStickerIcons.ALIGN);
        quShotStickerIconCenter.setIconEvent(new AlignHorizontallyEvent());
        PhotoStickerIcons quShotStickerIconRotate = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, PhotoStickerIcons.ROTATE);
        quShotStickerIconRotate.setIconEvent(new ZoomIconEvent());
        PhotoStickerIcons quShotStickerIconEdit = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, PhotoStickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new EditTextIconEvent());
        this.queShotGridView.setIcons(Arrays.asList(new PhotoStickerIcons[]{quShotStickerIconClose, quShotStickerIconScale, quShotStickerIconFlip,quShotStickerIconEdit, quShotStickerIconRotate, quShotStickerIconCenter}));
        this.queShotGridView.setConstrained(true);
        this.queShotGridView.setOnStickerOperationListener(this.onStickerOperationListener);
        stickerViewPager.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 11;
            }

            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view.equals(obj);
            }

            @Override
            public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
                (container).removeView((View) object);

            }

            @NonNull
            public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
                View inflate = LayoutInflater.from(PhotoCollageActivity.this.getBaseContext()).inflate(R.layout.list_sticker, null, false);
                RecyclerView recycler_view_sticker = inflate.findViewById(R.id.recyclerViewSticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(PhotoCollageActivity.this.getApplicationContext(), 7));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.bubbleList(), i, PhotoCollageActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.rainbowList(), i, PhotoCollageActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.flowerList(), i, PhotoCollageActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.amojiList(), i, PhotoCollageActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.deliciousList(), i, PhotoCollageActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.popularList(), i, PhotoCollageActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.valentineList(), i, PhotoCollageActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.emojList(), i, PhotoCollageActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.christmasList(), i, PhotoCollageActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.unicornList(), i, PhotoCollageActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoCollageActivity.this.getApplicationContext(), StickerFileAsset.stickerList(), i, PhotoCollageActivity.this));
                        break;
                }
                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickersTabAdapter(stickerViewPager, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));

        Preference.setKeyboard(getApplicationContext(), 0);
        this.keyboardHeightProvider = new KeyboardHeightProvider(this);
        this.keyboardHeightProvider.addKeyboardListener(i -> {
            if (i <= 0) {
                Preference.setHeightOfNotch(getApplicationContext(), -i);
            } else if (addTextFragment != null) {
                addTextFragment.updateAddTextBottomToolbarHeight(Preference.getHeightOfNotch(getApplicationContext()) + i);
                Preference.setKeyboard(getApplicationContext(), i + Preference.getHeightOfNotch(getApplicationContext()));
            }
        });

        setLoading(false);
        this.constraint_layout_change_background = findViewById(R.id.constrant_layout_change_background);
        this.constraint_layout_collage_layout = findViewById(R.id.constraint_layout_collage_layout);
        this.currentBackgroundState = new CollageBackgroundAdapter.SquareView(Color.parseColor("#ffffff"), "", true);
        this.recycler_view_color = findViewById(R.id.recycler_view_color);
        this.recycler_view_color.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_color.setHasFixedSize(true);
        this.recycler_view_color.setAdapter(new CollageColorAdapter(getApplicationContext(), this));
        this.recycler_view_gradient = findViewById(R.id.recycler_view_gradient);
        this.recycler_view_gradient.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_gradient.setHasFixedSize(true);
        this.recycler_view_gradient.setAdapter(new CollageBackgroundAdapter(getApplicationContext(), (CollageBackgroundAdapter.BackgroundGridListener) this, true));
        this.recycler_view_blur = findViewById(R.id.recycler_view_blur);
        this.recycler_view_blur.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_blur.setHasFixedSize(true);
        this.recycler_view_blur.setAdapter(new CollageBackgroundAdapter(getApplicationContext(), (CollageBackgroundAdapter.BackgroundGridListener) this, true));
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.queShotGridView.getLayoutParams();
        layoutParams.height = point.x;
        layoutParams.width = point.x;
        this.queShotGridView.setLayoutParams(layoutParams);
        this.aspectRatio = new AspectRatio("",1, 1);
        this.queShotGridView.setAspectRatio(new AspectRatio("", 1, 1));
        QueShotGridActivityCollage = this;
        this.moduleToolsId = Module.NONE;
        QueShotGridActivityInstance = this;

        this.recyclerViewToolsCollage.setAlpha(0.0f);
        this.constraint_layout_collage_layout.post(() -> {
            slideDown(recyclerViewToolsCollage);
        });
        new Handler().postDelayed(() -> {
            recyclerViewToolsCollage.setAlpha(1.0f);
        }, 1000);

    }

   /* ActivityResultLauncher<Intent> paymentResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    *//*if(recyclerViewFilter!=null)recyclerViewFilter.getAdapter().notifyDataSetChanged();
                    if(recycler_view_color!=null)recycler_view_color.getAdapter().notifyDataSetChanged();
                    if(recycler_view_collage!=null)recycler_view_collage.getAdapter().notifyDataSetChanged();*//*

                    recyclerViewTools.setVisibility(View.VISIBLE);
                }
            });*/

    private void SaveView() {
        if (PermissionsUtils.checkWriteStoragePermission(PhotoCollageActivity.this)) {
            Bitmap createBitmap = SaveFileUtils.createBitmap(PhotoCollageActivity.this.queShotGridView, 1920);
            Bitmap createBitmap2 = PhotoCollageActivity.this.queShotGridView.createBitmap();
            new SaveCollageAsFile().execute(new Bitmap[]{createBitmap, createBitmap2});
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            this.queShotGridView.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        super.onPause();
        this.keyboardHeightProvider.onPause();
    }

    public void onResume() {
        super.onResume();
        this.keyboardHeightProvider.onResume();
    }

    public void slideDown(View view) {
        ObjectAnimator.ofFloat(view, "translationY", 0.0f, (float) view.getHeight()).start();
    }

    public void slideUp(View view) {
        ObjectAnimator.ofFloat(view, "translationY", new float[]{(float) view.getHeight(), 0.0f}).start();
    }

    private void openTextFragment() {
        this.addTextFragment = TextFragment.show(this);
        this.textEditor = new TextFragment.TextEditor() {
            public void onDone(PhotoText addTextProperties) {
                PhotoCollageActivity.this.queShotGridView.addSticker(new PhotoTextView(PhotoCollageActivity.this.getApplicationContext(), addTextProperties));
            }

            public void onBackButton() {
                if (PhotoCollageActivity.this.queShotGridView.getStickers().isEmpty()) {
                    PhotoCollageActivity.this.onBackPressed();
                }
            }
        };
        this.addTextFragment.setOnTextEditorListener(this.textEditor);
    }


    @SuppressLint("NonConstantResourceId")
    public View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.imageViewClosebackground:
            case R.id.imageViewCloseFilter:
            case R.id.imageViewCloseLayer:
            case R.id.image_view_close_sticker:
            case R.id.imageViewCloseText:
                PhotoCollageActivity.this.setVisibleSave();
                PhotoCollageActivity.this.onBackPressed();
                return;
            case R.id.imageViewSavebackground:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_change_background.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                PhotoCollageActivity.this.setVisibleSave();
                PhotoCollageActivity.this.queShotGridView.setLocked(true);
                PhotoCollageActivity.this.queShotGridView.setTouchEnable(true);
                if (PhotoCollageActivity.this.queShotGridView.getBackgroundResourceMode() == 0) {
                    PhotoCollageActivity.this.currentBackgroundState.isColor = true;
                    PhotoCollageActivity.this.currentBackgroundState.isBitmap = false;
                    PhotoCollageActivity.this.currentBackgroundState.drawableId = ((ColorDrawable) PhotoCollageActivity.this.queShotGridView.getBackground()).getColor();
                    PhotoCollageActivity.this.currentBackgroundState.drawable = null;
                } else if (PhotoCollageActivity.this.queShotGridView.getBackgroundResourceMode() == 1) {
                    PhotoCollageActivity.this.currentBackgroundState.isColor = false;
                    PhotoCollageActivity.this.currentBackgroundState.isBitmap = false;
                    PhotoCollageActivity.this.currentBackgroundState.drawable = PhotoCollageActivity.this.queShotGridView.getBackground();
                } else {
                    PhotoCollageActivity.this.currentBackgroundState.isColor = false;
                    PhotoCollageActivity.this.currentBackgroundState.isBitmap = true;
                    PhotoCollageActivity.this.currentBackgroundState.drawable = PhotoCollageActivity.this.queShotGridView.getBackground();
                }
                PhotoCollageActivity.this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveFilter:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_filter_layout.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                PhotoCollageActivity.this.moduleToolsId = Module.NONE;
                setVisibleSave();
                return;
            case R.id.imageViewSaveText:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraintLayoutAddText.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.GONE);
                this.queShotGridView.setHandlingSticker(null);
                this.queShotGridView.setLocked(true);
                this.relativeLayoutAddText.setVisibility(View.GONE);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveLayer:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constrant_layout_change_Layout.setVisibility(View.GONE);
                PhotoCollageActivity.this.setVisibleSave();
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                PhotoCollageActivity.this.queShotLayout = PhotoCollageActivity.this.queShotGridView.getQueShotLayout();
                PhotoCollageActivity.this.BorderRadius = PhotoCollageActivity.this.queShotGridView.getCollageRadian();
                PhotoCollageActivity.this.Padding = PhotoCollageActivity.this.queShotGridView.getCollagePadding();
                PhotoCollageActivity.this.queShotGridView.setLocked(true);
                PhotoCollageActivity.this.queShotGridView.setTouchEnable(true);
                PhotoCollageActivity.this.aspectRatio = PhotoCollageActivity.this.queShotGridView.getAspectRatio();
                PhotoCollageActivity.this.moduleToolsId = Module.NONE;
                return;
            case R.id.image_view_save_sticker:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_sticker.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                PhotoCollageActivity.this.queShotGridView.setHandlingSticker(null);
                PhotoCollageActivity.this.seekbarSticker.setVisibility(View.GONE);
                PhotoCollageActivity.this.imageViewAddSticker.setVisibility(View.GONE);
                PhotoCollageActivity.this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                PhotoCollageActivity.this.setVisibleSave();
                PhotoCollageActivity.this.queShotGridView.setLocked(true);
                PhotoCollageActivity.this.queShotGridView.setTouchEnable(true);
                PhotoCollageActivity.this.moduleToolsId = Module.NONE;

                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                setVisibleSave();
                return;
            default:
        }
    };



    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            switch (seekBar.getId()) {
                case R.id.seekbar_border:
                    PhotoCollageActivity.this.queShotGridView.setCollagePadding((float) i);
                    String valuePadding = String.valueOf(i);
                    textViewSeekBarPadding.setText(valuePadding);
                    break;
                case R.id.seekbar_radius:
                    PhotoCollageActivity.this.queShotGridView.setCollageRadian((float) i);
                    String valueRadius = String.valueOf(i);
                    textViewSeekBarRadius.setText(valueRadius);
                    break;
            }
            PhotoCollageActivity.this.queShotGridView.invalidate();
        }
    };
    PhotoStickerView.OnStickerOperationListener onStickerOperationListener = new PhotoStickerView.OnStickerOperationListener() {
        public void onStickerDrag(@NonNull Sticker sticker) {
        }

        public void onStickerFlip(@NonNull Sticker sticker) {
        }

        public void onStickerTouchedDown(@NonNull Sticker sticker) {
        }

        public void onStickerZoom(@NonNull Sticker sticker) {
        }

        public void onTouchDownBeauty(float f, float f2) {
        }

        public void onTouchDragBeauty(float f, float f2) {
        }

        public void onTouchUpBeauty(float f, float f2) {
        }

        public void onAddSticker(@NonNull Sticker sticker) {
            PhotoCollageActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
            PhotoCollageActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
        }

        public void onStickerSelected(@NonNull Sticker sticker) {
            PhotoCollageActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
            PhotoCollageActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
        }

        public void onStickerDeleted(@NonNull Sticker sticker) {
            PhotoCollageActivity.this.seekbarSticker.setVisibility(View.GONE);
        }

        public void onStickerTouchOutside() {
            PhotoCollageActivity.this.seekbarSticker.setVisibility(View.GONE);
        }

        public void onStickerDoubleTap(@NonNull Sticker sticker) {
            if (sticker instanceof PhotoTextView) {
                sticker.setShow(false);
                PhotoCollageActivity.this.queShotGridView.setHandlingSticker(null);
                PhotoCollageActivity.this.addTextFragment = TextFragment.show(PhotoCollageActivity.this, ((PhotoTextView) sticker).getPhotoText());
                PhotoCollageActivity.this.textEditor = new TextFragment.TextEditor() {
                    public void onDone(PhotoText addTextProperties) {
                        PhotoCollageActivity.this.queShotGridView.getStickers().remove(PhotoCollageActivity.this.queShotGridView.getLastHandlingSticker());
                        PhotoCollageActivity.this.queShotGridView.addSticker(new PhotoTextView(PhotoCollageActivity.this, addTextProperties));
                    }

                    public void onBackButton() {
                        PhotoCollageActivity.this.queShotGridView.showLastHandlingSticker();
                    }
                };
                PhotoCollageActivity.this.addTextFragment.setOnTextEditorListener(PhotoCollageActivity.this.textEditor);
            }
        }
    };


    public static PhotoCollageActivity getQueShotGridActivityInstance() {
        return QueShotGridActivityInstance;
    }

    public void isPermissionGranted(boolean z, String str) {
        if (z) {
            Bitmap createBitmap = SaveFileUtils.createBitmap(this.queShotGridView, 1920);
            Bitmap createBitmap2 = this.queShotGridView.createBitmap();
            new SaveCollageAsFile().execute(createBitmap, createBitmap2);
        }
    }

    public void setBackgroundColor() {
        this.recycler_view_color.scrollToPosition(0);
        ((CollageColorAdapter) this.recycler_view_color.getAdapter()).setSelectedIndex(-1);
        this.recycler_view_color.getAdapter().notifyDataSetChanged();
        this.recycler_view_color.setVisibility(View.VISIBLE);
        this.recycler_view_gradient.setVisibility(View.GONE);
        this.recycler_view_blur.setVisibility(View.GONE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.viewColor.setVisibility(View.VISIBLE);
        this.viewGradient.setVisibility(View.INVISIBLE);
        this.viewBlur.setVisibility(View.INVISIBLE);
    }

    public void setBackgroundGradient() {
        this.recycler_view_gradient.scrollToPosition(0);
        ((CollageBackgroundAdapter) this.recycler_view_gradient.getAdapter()).setSelectedIndex(-1);
        this.recycler_view_gradient.getAdapter().notifyDataSetChanged();
        this.recycler_view_color.setVisibility(View.GONE);
        this.recycler_view_gradient.setVisibility(View.VISIBLE);
        this.recycler_view_blur.setVisibility(View.GONE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.viewColor.setVisibility(View.INVISIBLE);
        this.viewGradient.setVisibility(View.VISIBLE);
        this.viewBlur.setVisibility(View.INVISIBLE);
    }

    public void selectBackgroundBlur() {
        ArrayList arrayList = new ArrayList();
        for (PhotoGrid drawable : this.queShotGridView.getQueShotGrids()) {
            arrayList.add(drawable.getDrawable());
        }
        CollageBackgroundAdapter backgroundGridAdapter = new CollageBackgroundAdapter(getApplicationContext(), this, (List<Drawable>) arrayList);
        backgroundGridAdapter.setSelectedIndex(-1);
        this.recycler_view_blur.setAdapter(backgroundGridAdapter);
        this.recycler_view_color.setVisibility(View.GONE);
        this.recycler_view_gradient.setVisibility(View.GONE);
        this.recycler_view_blur.setVisibility(View.VISIBLE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.viewColor.setVisibility(View.INVISIBLE);
        this.viewGradient.setVisibility(View.INVISIBLE);
        this.viewBlur.setVisibility(View.VISIBLE);
    }

    public void setLayer() {
        this.recycler_view_collage.setVisibility(View.VISIBLE);
        this.recycler_view_ratio.setVisibility(View.GONE);
        this.linearLayoutBorder.setVisibility(View.GONE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.viewCollage.setVisibility(View.VISIBLE);
        this.viewBorder.setVisibility(View.INVISIBLE);
        this.viewRatio.setVisibility(View.INVISIBLE);
    }

    public void setBorder() {
        this.recycler_view_collage.setVisibility(View.GONE);
        this.recycler_view_ratio.setVisibility(View.GONE);
        this.linearLayoutBorder.setVisibility(View.VISIBLE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.viewCollage.setVisibility(View.INVISIBLE);
        this.viewBorder.setVisibility(View.VISIBLE);
        this.viewRatio.setVisibility(View.INVISIBLE);
        this.seekBarPadding.setProgress((int) this.queShotGridView.getCollagePadding());
        this.seekBarRadius.setProgress((int) this.queShotGridView.getCollageRadian());

    }

    public void setRatio() {
        this.recycler_view_collage.setVisibility(View.GONE);
        this.recycler_view_ratio.setVisibility(View.VISIBLE);
        this.linearLayoutBorder.setVisibility(View.GONE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.viewCollage.setVisibility(View.INVISIBLE);
        this.viewBorder.setVisibility(View.INVISIBLE);
        this.viewRatio.setVisibility(View.VISIBLE);

    }

    public void onToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case LAYER:
                setLayer();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case PADDING:
                setBorder();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case RATIO:
                setRatio();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case FILTER:
                if (this.drawableList.isEmpty()) {
                    for (PhotoGrid drawable : this.queShotGridView.getQueShotGrids()) {
                        this.drawableList.add(drawable.getDrawable());
                    }
                }
                new allFilters().execute();
                setGoneSave();
                return;
            case TEXT:
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                setGuideLine();
                this.queShotGridView.setLocked(false);
                openTextFragment();
                this.constraintLayoutAddText.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.VISIBLE);
                this.relativeLayoutAddText.setVisibility(View.VISIBLE);
                return;
            case STICKER:
                setGuideLine();
                this.constraint_layout_sticker.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.VISIBLE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotGridView.updateLayout(this.queShotLayout);
                this.queShotGridView.setCollagePadding(this.Padding);
                this.queShotGridView.setCollageRadian(this.BorderRadius);
                getWindowManager().getDefaultDisplay().getSize(new Point());
                onNewAspectRatioSelected(this.aspectRatio);
                this.queShotGridView.setAspectRatio(this.aspectRatio);
                for (int i = 0; i < this.drawableList.size(); i++) {
                    this.queShotGridView.getQueShotGrids().get(i).setDrawable(this.drawableList.get(i));
                }
                this.queShotGridView.invalidate();
                if (this.currentBackgroundState.isColor) {
                    this.queShotGridView.setBackgroundResourceMode(0);
                    this.queShotGridView.setBackgroundColor(this.currentBackgroundState.drawableId);
                }  else {
                    this.queShotGridView.setBackgroundResourceMode(1);
                    if (this.currentBackgroundState.drawable != null) {
                        this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                    } else {
                        this.queShotGridView.setBackgroundResource(this.currentBackgroundState.drawableId);
                    }
                }
                setGoneSave();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);

                return;
            case GRADIENT:
                setGuideLine();
                this.constraint_layout_change_background.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                setBackgroundColor();
                if (this.queShotGridView.getBackgroundResourceMode() == 0) {
                    this.currentBackgroundState.isColor = true;
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.drawableId = ((ColorDrawable) this.queShotGridView.getBackground()).getColor();
                    return;
                } else if (this.queShotGridView.getBackgroundResourceMode() == 2 || (this.queShotGridView.getBackground() instanceof ColorDrawable)) {
                    this.currentBackgroundState.isBitmap = true;
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.drawable = this.queShotGridView.getBackground();
                    return;
                } else if (this.queShotGridView.getBackground() instanceof GradientDrawable) {
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.drawable = this.queShotGridView.getBackground();
                    return;
                } else {
                    return;
                }
            default:
        }
    }

    public void loadPhoto() {
        final int i;
        ArrayList<Bitmap> arrayList = new ArrayList<>();
        i = Math.min(stringList.size(), queShotLayout.getAreaCount());
        for (int i2 = 0; i2 < i; i2++) {
            Glide.with(this)
                    .asBitmap()
                    .load("file:///" + stringList.get(i2))
                    .into(new CustomTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(@NonNull Bitmap btm, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                Bitmap bitmap = resizeBitmapIfNeeded(btm);
                                arrayList.add(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (arrayList.size() != i) {
                                return;
                            }
                            if (stringList.size() < queShotLayout.getAreaCount()) {
                                for (int i = 0; i < queShotLayout.getAreaCount(); i++) {
                                    queShotGridView.addQuShotCollage(arrayList.get(0));
                                }
                                return;
                            }
                            queShotGridView.addPieces(arrayList);
                            setLoading(false);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    private Bitmap resizeBitmapIfNeeded(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                float width = bitmap.getWidth();
                float height = bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);

                if (max > 1.0f) {
                    return Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                } else {
                    return bitmap;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setOnBackPressDialog() {
        final Dialog dialogOnBackPressed = new Dialog(PhotoCollageActivity.this, R.style.UploadDialog);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.show();
        this.textViewCancel = dialogOnBackPressed.findViewById(R.id.textViewCancel);
        this.textViewDiscard = dialogOnBackPressed.findViewById(R.id.textViewDiscard);

        this.textViewDiscard.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
            PhotoCollageActivity.this.moduleToolsId = null;
            PhotoCollageActivity.this.finish();
            finish();
        });

        this.textViewCancel.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
        });
    }

    public void setGuideLineTools(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_collage_layout);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 1, this.constraint_layout_collage_layout.getId(), 1, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 4, this.guidelineTools.getId(), 3, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 2, this.constraint_layout_collage_layout.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_collage_layout);
    }

    public void setGuideLine(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_collage_layout);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 1, this.constraint_layout_collage_layout.getId(), 1, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 4, this.guideline.getId(), 3, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 2, this.constraint_layout_collage_layout.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_collage_layout);
    }

    public void setGoneSave() {
        this.constraint_save_control.setVisibility(View.GONE);
    }

    public void setVisibleSave() {
        this.constraint_save_control.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (this.moduleToolsId == null) {
            super.onBackPressed();
            return;
        }
        try {
            switch (this.moduleToolsId) {
                case PADDING:
                case RATIO:
                case LAYER:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constrant_layout_change_Layout.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    setVisibleSave();
                    this.queShotGridView.updateLayout(this.queShotLayout);
                    this.queShotGridView.setCollagePadding(this.Padding);
                    this.queShotGridView.setCollageRadian(this.BorderRadius);
                    this.moduleToolsId = Module.NONE;
                    getWindowManager().getDefaultDisplay().getSize(new Point());
                    onNewAspectRatioSelected(this.aspectRatio);
                    this.queShotGridView.setAspectRatio(this.aspectRatio);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    return;
                case TEXT:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraintLayoutAddText.setVisibility(View.GONE);
                    this.constraintLayoutSaveText.setVisibility(View.GONE);
                    if (!this.queShotGridView.getStickers().isEmpty()) {
                        this.queShotGridView.getStickers().clear();
                        this.queShotGridView.setHandlingSticker(null);
                    }
                    this.moduleToolsId = Module.NONE;
                    this.relativeLayoutAddText.setVisibility(View.GONE);
                    this.queShotGridView.setHandlingSticker(null);
                    setVisibleSave();
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    return;
                case FILTER:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_filter_layout.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    for (int i = 0; i < this.drawableList.size(); i++) {
                        this.queShotGridView.getQueShotGrids().get(i).setDrawable(this.drawableList.get(i));
                    }
                    this.queShotGridView.invalidate();
                    setVisibleSave();
                    this.moduleToolsId = Module.NONE;
                    return;
                case STICKER:
                    setGuideLineTools();
                    this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                    if (this.queShotGridView.getStickers().size() <= 0) {
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.imageViewAddSticker.setVisibility(View.GONE);
                        this.queShotGridView.setHandlingSticker((Sticker) null);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                        this.constraint_layout_sticker.setVisibility(View.GONE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.queShotGridView.setLocked(true);
                        this.moduleToolsId = Module.NONE;
                    } else if (this.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                        this.queShotGridView.getStickers().clear();
                        this.imageViewAddSticker.setVisibility(View.GONE);
                        this.queShotGridView.setHandlingSticker(null);
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                        this.constraint_layout_sticker.setVisibility(View.GONE);
                        this.queShotGridView.setLocked(true);
                        this.queShotGridView.setTouchEnable(true);
                        this.moduleToolsId = Module.NONE;
                    } else {
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
                        this.imageViewAddSticker.setVisibility(View.VISIBLE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                    }
                    this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_sticker.setVisibility(View.GONE);
                    setVisibleSave();
                    return;
                case GRADIENT:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_change_background.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    if (this.currentBackgroundState.isColor) {
                        this.queShotGridView.setBackgroundResourceMode(0);
                        this.queShotGridView.setBackgroundColor(this.currentBackgroundState.drawableId);
                    } else if (this.currentBackgroundState.isBitmap) {
                        this.queShotGridView.setBackgroundResourceMode(2);
                        this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                    } else {
                        this.queShotGridView.setBackgroundResourceMode(1);
                        if (this.currentBackgroundState.drawable != null) {
                            this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                        } else {
                            this.queShotGridView.setBackgroundResource(this.currentBackgroundState.drawableId);
                        }
                    }
                    setVisibleSave();
                    this.moduleToolsId = Module.NONE;
                    return;
                case COLLAGE:
                    setVisibleSave();
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.recyclerViewToolsCollage.setVisibility(View.GONE);
                    this.moduleToolsId = Module.NONE;
                    this.queShotGridView.setQueShotGrid(null);
                    this.queShotGridView.setPrevHandlingQueShotGrid(null);
                    this.queShotGridView.invalidate();
                    this.moduleToolsId = Module.NONE;
                    return;
                case NONE:
                    setOnBackPressDialog();
                    return;
                default:
                    super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onItemClick(PhotoLayout puzzleLayout2, int i) {
        PhotoLayout parse = PhotoLayoutParser.parse(puzzleLayout2.generateInfo());
        puzzleLayout2.setRadian(this.queShotGridView.getCollageRadian());
        puzzleLayout2.setPadding(this.queShotGridView.getCollagePadding());
        this.queShotGridView.updateLayout(parse);
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point);
        this.queShotGridView.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_wrapper_collage_view);
        constraintSet.connect(this.queShotGridView.getId(), 3, this.constraint_layout_wrapper_collage_view.getId(), 3, 0);
        constraintSet.connect(this.queShotGridView.getId(), 1, this.constraint_layout_wrapper_collage_view.getId(), 1, 0);
        constraintSet.connect(this.queShotGridView.getId(), 4, this.constraint_layout_wrapper_collage_view.getId(), 4, 0);
        constraintSet.connect(this.queShotGridView.getId(), 2, this.constraint_layout_wrapper_collage_view.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_wrapper_collage_view);
        this.queShotGridView.setAspectRatio(aspectRatio);
    }

    public void replaceCurrentPiece(String str) {
        new OnLoadBitmapFromUri().execute(str);
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio, Point point) {
        int height = this.constraint_layout_wrapper_collage_view.getHeight();
        if (aspectRatio.getAspectRatioY() > aspectRatio.getAspectRatioX()) {
            int ratio = (int) (aspectRatio.getAspectRatioY() * ((float) height));
            if (ratio < point.x) {
                return new int[]{ratio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio.getAspectRatioX())};
        }
        int ratio2 = (int) (((float) point.x) / aspectRatio.getAspectRatioX());
        if (ratio2 > height) {
            return new int[]{(int) (((float) height) * aspectRatio.getAspectRatioY()), height};
        }
        return new int[]{point.x, ratio2};
    }

    public void addSticker(int item, Bitmap bitmap) {
        this.queShotGridView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
        this.imageViewAddSticker.setVisibility(View.VISIBLE);
    }

    public void onBackgroundSelected(int item, final CollageBackgroundAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.queShotGridView.setBackgroundColor(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(0);
        } else if (squareView.drawable != null) {
            this.queShotGridView.setBackgroundResourceMode(2);
            new AsyncTask<Void, Bitmap, Bitmap>() {

                public Bitmap doInBackground(Void... voidArr) {
                    return FilterUtils.getBlurImageFromBitmap(getApplicationContext(), ((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                }


                public void onPostExecute(Bitmap bitmap) {
                    PhotoCollageActivity.this.queShotGridView.setBackground(new BitmapDrawable(PhotoCollageActivity.this.getResources(), bitmap));
                }
            }.execute();
        } else {
            this.queShotGridView.setBackgroundResource(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(1);
        }
    }

    public void onBackgroundColorSelected(int item, CollageColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.queShotGridView.setBackgroundColor(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(0);
        }
    }

    public void onFilterSelected(int item, String str) {

    }

    public void finishCrop(Bitmap bitmap) {
        this.queShotGridView.replace(bitmap, "");
    }

    public void onSaveFilter(Bitmap bitmap) {
        this.queShotGridView.replace(bitmap, "");
    }

    @Override
    public void onPieceFuncSelected(Module toolType) {
        switch (toolType) {
            case REPLACE:
                PhotoPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).setForwardMain(true).start(this);
                return;
            case H_FLIP:
                this.queShotGridView.flipHorizontally();
                return;
            case V_FLIP:
                this.queShotGridView.flipVertically();
                return;
            case ROTATE:
                this.queShotGridView.rotate(90.0f);
                return;
            case CROP:
                CropFragment.show(this, this, ((BitmapDrawable) this.queShotGridView.getQueShotGrid().getDrawable()).getBitmap());
                return;
            case FILTER:
                new LoadFilterBitmapForCurrentPiece().execute();
                return;
        }
    }

    class allFilters extends AsyncTask<Void, Void, Void> {
        allFilters() { }

        public void onPreExecute() {
            PhotoCollageActivity.this.setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            PhotoCollageActivity.this.listFilterAll.clear();
            PhotoCollageActivity.this.listFilterAll.addAll(FilterFileAsset.getListBitmapFilter(getApplicationContext(), ThumbnailUtils.extractThumbnail(((BitmapDrawable) PhotoCollageActivity.this.queShotGridView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            PhotoCollageActivity.this.recyclerViewFilter.setAdapter(new FilterAdapter(PhotoCollageActivity.this.listFilterAll, PhotoCollageActivity.this, PhotoCollageActivity.this.getApplicationContext(), Arrays.asList(FilterFileAsset.FILTERS)));
            PhotoCollageActivity.this.recyclerViewToolsCollage.setVisibility(View.GONE);
            PhotoCollageActivity.this.queShotGridView.setLocked(false);
            PhotoCollageActivity.this.queShotGridView.setTouchEnable(false);
            setGuideLine();
            PhotoCollageActivity.this.constraint_layout_filter_layout.setVisibility(View.VISIBLE);
            PhotoCollageActivity.this.recyclerViewTools.setVisibility(View.GONE);
            PhotoCollageActivity.this.setLoading(false);
        }
    }

    class LoadFilterBitmapForCurrentPiece extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        LoadFilterBitmapForCurrentPiece() {
        }
        public void onPreExecute() {
            PhotoCollageActivity.this.setLoading(true);
        }
        @SuppressLint("WrongThread")
        public List<Bitmap> doInBackground(Void... voidArr) {
            return FilterFileAsset.getListBitmapFilter(getApplicationContext(), ThumbnailUtils.extractThumbnail(((BitmapDrawable) PhotoCollageActivity.this.queShotGridView.getQueShotGrid().getDrawable()).getBitmap(), 100, 100));
        }

        public void onPostExecute(List<Bitmap> list) {
            PhotoCollageActivity.this.setLoading(false);
            if (PhotoCollageActivity.this.queShotGridView.getQueShotGrid() != null) {
                FilterFragment.show(PhotoCollageActivity.this, PhotoCollageActivity.this, ((BitmapDrawable) PhotoCollageActivity.this.queShotGridView.getQueShotGrid().getDrawable()).getBitmap(), list);
            }
        }
    }

    class OnLoadBitmapFromUri extends AsyncTask<String, Bitmap, Bitmap> {
        OnLoadBitmapFromUri() {
        }

        public void onPreExecute() {
            PhotoCollageActivity.this.setLoading(true);
        }

        public Bitmap doInBackground(String... strArr) {
            try {
                Uri fromFile = Uri.fromFile(new File(strArr[0]));

                Bitmap rotateBitmap = SystemUtil.rotateBitmap(MediaStore.Images.Media.getBitmap(PhotoCollageActivity.this.getContentResolver(), fromFile), new ExifInterface(PhotoCollageActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));

                float width = (float) rotateBitmap.getWidth();
                float height = (float) rotateBitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                return max > 1.0f ? Bitmap.createScaledBitmap(rotateBitmap, (int) (width / max), (int) (height / max), false) : rotateBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            PhotoCollageActivity.this.setLoading(false);
            PhotoCollageActivity.this.queShotGridView.replace(bitmap, "");
        }
    }


    class SaveCollageAsFile extends AsyncTask<Bitmap, String, Uri> {
        SaveCollageAsFile() {}

        public void onPreExecute() {
            PhotoCollageActivity.this.setLoading(true);
        }

        public Uri doInBackground(Bitmap... bitmapArr) {
            Bitmap bitmap = bitmapArr[0];
            Bitmap bitmap2 = bitmapArr[1];
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = null;
            canvas.drawBitmap(bitmap, (Rect) null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
            canvas.drawBitmap(bitmap2, (Rect) null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
            bitmap.recycle();
            bitmap2.recycle();
            try {
                String fileName = "COLLAGE_"+ String.format("%d.jpg", System.currentTimeMillis());
                Uri uri = SaveFileUtils.saveBitmapFile(PhotoCollageActivity.this, createBitmap, fileName, getApplicationContext().getString(R.string.app_name));
                CommonKeys.filePath = new File(PathUtills.getPath(PhotoCollageActivity.this, uri));
                createBitmap.recycle();
                return uri;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        public void onPostExecute(Uri uri) {
            PhotoCollageActivity.this.setLoading(false);
            Intent intent = new Intent(PhotoCollageActivity.this, PhotoShareActivity.class);
            intent.putExtra("activity","PhotoCollageActivity");
            PhotoCollageActivity.this.startActivity(intent);
        }
    }




    public void setLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            this.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        this.relativeLayoutLoading.setVisibility(View.GONE);
    }

}
