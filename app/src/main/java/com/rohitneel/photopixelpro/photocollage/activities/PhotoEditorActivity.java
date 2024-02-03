package com.rohitneel.photopixelpro.photocollage.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hold1.keyboardheightprovider.KeyboardHeightProvider;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.adapters.AdjustAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.ColorAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.FilterAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.MagicBrushAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.OverlayAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.PhotoDrawToolsAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.PhotoEffectToolsAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.PhotoSQToolsAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.PhotoToolsAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.RecyclerTabLayout;
import com.rohitneel.photopixelpro.photocollage.adapters.StickerAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.StickersTabAdapter;
import com.rohitneel.photopixelpro.photocollage.assets.FilterFileAsset;
import com.rohitneel.photopixelpro.photocollage.assets.OverlayFileAsset;
import com.rohitneel.photopixelpro.photocollage.assets.StickerFileAsset;
import com.rohitneel.photopixelpro.photocollage.constants.StoreManager;
import com.rohitneel.photopixelpro.photocollage.draw.DrawModel;
import com.rohitneel.photopixelpro.photocollage.draw.Drawing;
import com.rohitneel.photopixelpro.photocollage.event.AlignHorizontallyEvent;
import com.rohitneel.photopixelpro.photocollage.event.DeleteIconEvent;
import com.rohitneel.photopixelpro.photocollage.event.EditTextIconEvent;
import com.rohitneel.photopixelpro.photocollage.event.FlipHorizontallyEvent;
import com.rohitneel.photopixelpro.photocollage.event.ZoomIconEvent;
import com.rohitneel.photopixelpro.photocollage.fragment.BlurSquareBgFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.ColoredFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.CropFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.FrameFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.HSlFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.MirrorFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.MosaicFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.RatioFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.SaturationSquareBackgroundFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.SaturationSquareFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.SketchSquareBackgroundFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.SketchSquareFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.TextFragment;
import com.rohitneel.photopixelpro.photocollage.layout.ArtLayout;
import com.rohitneel.photopixelpro.photocollage.layout.BlurLayout;
import com.rohitneel.photopixelpro.photocollage.layout.DripLayout;
import com.rohitneel.photopixelpro.photocollage.layout.MotionLayout;
import com.rohitneel.photopixelpro.photocollage.layout.NeonLayout;
import com.rohitneel.photopixelpro.photocollage.layout.PixLabLayout;
import com.rohitneel.photopixelpro.photocollage.layout.SplashLayout;
import com.rohitneel.photopixelpro.photocollage.layout.WingLayout;
import com.rohitneel.photopixelpro.photocollage.listener.AdjustListener;
import com.rohitneel.photopixelpro.photocollage.listener.BrushColorListener;
import com.rohitneel.photopixelpro.photocollage.listener.BrushMagicListener;
import com.rohitneel.photopixelpro.photocollage.listener.FilterListener;
import com.rohitneel.photopixelpro.photocollage.listener.OnPhotoEditorListener;
import com.rohitneel.photopixelpro.photocollage.listener.OverlayListener;
import com.rohitneel.photopixelpro.photocollage.module.Module;
import com.rohitneel.photopixelpro.photocollage.picker.PermissionsUtils;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoEditor;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoPickerView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoStickerIcons;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoStickerView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoText;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoTextView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoView;
import com.rohitneel.photopixelpro.photocollage.preference.Preference;
import com.rohitneel.photopixelpro.photocollage.sticker.DrawableSticker;
import com.rohitneel.photopixelpro.photocollage.sticker.Sticker;
import com.rohitneel.photopixelpro.photocollage.utils.BitmapTransfer;
import com.rohitneel.photopixelpro.photocollage.utils.DegreeSeekBar;
import com.rohitneel.photopixelpro.photocollage.utils.FilterUtils;
import com.rohitneel.photopixelpro.photocollage.utils.SaveFileUtils;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;

import org.jetbrains.annotations.NotNull;
import org.wysaid.myUtils.MsgUtil;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class PhotoEditorActivity extends PhotoBaseActivity implements OnPhotoEditorListener,
        View.OnClickListener, StickerAdapter.OnClickSplashListener, BlurSquareBgFragment.BlurSquareBgListener,
        CropFragment.OnCropPhoto, BrushColorListener, SaturationSquareBackgroundFragment.SplashSaturationBackgrundListener,
        RatioFragment.RatioSaveListener, FrameFragment.RatioSaveListener, SketchSquareBackgroundFragment.SketchBackgroundListener,
        SaturationSquareFragment.SplashSaturationListener,
        MosaicFragment.MosaicListener, ColoredFragment.ColoredListener, SketchSquareFragment.SketchListener,
        PhotoToolsAdapter.OnQuShotItemSelected, PhotoDrawToolsAdapter.OnQuShotDrawItemSelected,
        PhotoEffectToolsAdapter.OnQuShotEffectItemSelected,
        PhotoSQToolsAdapter.OnQuShotSQItemSelected, FilterListener, AdjustListener, OverlayListener,
        BrushMagicListener, HSlFragment.OnFilterSavePhoto {

    private static final String TAG = "PhotoEditorActivity";
    // Tools
    public Module moduleToolsId = Module.NONE;
    // Keyboard
    private KeyboardHeightProvider keyboardProvider;
    private Animation slideUpAnimation, slideDownAnimation;
    // Guideline
    private Guideline guidelinePaint;
    private Guideline guideline;
    // Adapter
    public AdjustAdapter mAdjustAdapter;
    public ColorAdapter colorAdapter;
    private final PhotoToolsAdapter mEditingToolsAdapter = new PhotoToolsAdapter(this);
    private final PhotoDrawToolsAdapter mEditingDrawToolsAdapter = new PhotoDrawToolsAdapter(this);
    private final PhotoEffectToolsAdapter mEditingEffectToolsAdapter = new PhotoEffectToolsAdapter(this);
    private final PhotoSQToolsAdapter mEditingSplashToolsAdapter = new PhotoSQToolsAdapter(this);
    // QuShot
    public PhotoEditor photoEditor;
    public PhotoView photoView;

    // BitmapStickerIcon
    PhotoStickerIcons photoStickerIconClose;
    PhotoStickerIcons photoStickerIconScale;
    PhotoStickerIcons photoStickerIconFlip;
    PhotoStickerIcons photoStickerIconRotate;
    PhotoStickerIcons photoStickerIconEdit;
    PhotoStickerIcons photoStickerIconAlign;
    // Guideline
    private DegreeSeekBar adjustSeekBar;
    private DegreeSeekBar adjustFilter;
    private SeekBar seekBarOverlay;
    // Fragment
    public TextFragment.TextEditor textEditor;
    public TextFragment textFragment;
    // ViewPager
    public ViewPager viewPagerStickers;
    // ConstraintLayout
    private ConstraintLayout constraintLayoutSave;
    private ConstraintLayout constraintLayoutDraw;
    private ConstraintLayout constraintLayoutSplash;
    private ConstraintLayout constraintLayoutEffects;
    private ConstraintLayout constraintLayoutAdjust;
    private ConstraintLayout constraintLayoutOverlay;
    private ConstraintLayout constraintLayoutConfirmCompareOverlay;
    private ConstraintLayout constraintLayoutSaveOverlay;
    private ConstraintLayout constraintLayoutSaveText;
    private ConstraintLayout constraintLayoutSaveSticker;
    private ConstraintLayout constraintLayoutConfirmCompareHardmix;
    private ConstraintLayout constraintLayoutDodge;
    private ConstraintLayout constraintLayoutConfirmCompareDodge;
    private ConstraintLayout constraintLayoutDivide;
    private ConstraintLayout constraintLayoutConfirmCompareDivide;
    private ConstraintLayout constraintLayoutBurn;
    private ConstraintLayout constraintLayoutConfirmCompareBurn;
    private ConstraintLayout constraintLayoutPaint;
    private ConstraintLayout constraintLayoutPaintTool;
    private ConstraintLayout constraintLayoutNeon;
    private ConstraintLayout constraintLayoutNeonTool;
    private ConstraintLayout constraintLayoutMagic;
    private ConstraintLayout constraintLayoutMagicTool;
    public ConstraintLayout constraintLayoutFilter;
    private ConstraintLayout constraintLayoutSticker;
    private ConstraintLayout constraintLayoutAddText;
    private ConstraintLayout constraintLayoutView;
    // RelativeLayout
    private RelativeLayout relativeLayoutAddText;
    private RelativeLayout relativeLayoutWrapper;
    private RelativeLayout relativeLayoutLoading;
    public LinearLayout linear_layout_wrapper_sticker_list;

    // RecyclerView
    public RecyclerView recyclerViewTools;
    public RecyclerView recyclerViewDraw;
    public RecyclerView recyclerViewSpalsh;
    public RecyclerView recyclerViewEffect;
    public RecyclerView recyclerViewFilter;
    private RecyclerView recyclerViewPaintListColor;
    private RecyclerView recyclerViewMagicListColor;
    private RecyclerView recyclerViewNeonListColor;
    private RecyclerView recyclerViewAdjust;
    // ImageView
    private ImageView imageViewCompareAdjust;
    public ImageView imageViewCompareFilter;
    public ImageView imageViewCompareOverlay;
    private ImageView imageViewRedoPaint;
    private ImageView imageViewCleanPaint;
    private ImageView imageViewCleanNeon;
    private ImageView imageViewRedoNeon;
    private ImageView imageViewUndoPaint;
    private ImageView imageViewUndoNeon;
    private ImageView imageViewCleanMagic;
    private ImageView imageViewRedoMagic;
    private ImageView imageViewUndoMagic;
    public ImageView undo;
    public ImageView redo;
    public ImageView imageViewAddSticker;

    // TextView
    public TextView textViewCancel;
    public TextView textViewDiscard;
    public TextView textViewSaveEditing;
    public ImageView image_view_exit;

    // Seekbar
    public SeekBar seekbarSticker;
    // ArrayList & List
    public ArrayList listDodgeEffect = new ArrayList<>();
    public ArrayList listColorEffect = new ArrayList<>();
    public ArrayList listHardMixEffect = new ArrayList<>();
    public ArrayList listHueEffect = new ArrayList<>();
    public ArrayList listOverlayEffect = new ArrayList<>();
    public ArrayList listBurnEffect = new ArrayList<>();
    public ArrayList listFilter = new ArrayList<>();
    // Admob Ads

    private TextView textViewTitleBrush;
    private ImageView imageViewBrushSize;
    private ImageView imageViewBrushOpacity;
    private ImageView imageViewBrushEraser;
    private TextView textViewValueBrush;
    private TextView textViewValueOpacity;
    private TextView textViewValueEraser;
    private SeekBar seekBarBrush;
    private SeekBar seekBarOpacity;
    private SeekBar seekBarEraser;

    private TextView textViewTitleNeon;
    private ImageView imageViewNeonSize;
    private ImageView imageViewNeonEraser;
    private TextView textViewNeonBrush;
    private TextView textViewNeonEraser;
    private SeekBar seekBarNeonBrush;
    private SeekBar seekBarNeonEraser;

    private TextView textViewTitleMagic;
    private ImageView imageViewMagicSize;
    private ImageView imageViewMagicOpacity;
    private ImageView imageViewMagicEraser;
    private TextView textViewMagicBrush;
    private TextView textViewMagicOpacity;
    private TextView textViewMagicEraser;
    private SeekBar seekBarMagicBrush;
    private SeekBar seekBarMagicOpacity;
    private SeekBar seekBarMagicEraser;


    private RelativeLayout relativeLayoutPaint;
    private RelativeLayout relativeLayoutNeon;
    private RelativeLayout relativeLayoutMagic;

    public RecyclerView recycler_view_overlay_effect;
    public RecyclerView recycler_view_color_effect;
    public RecyclerView recycler_view_dodge_effect;
    public RecyclerView recycler_view_hardmix_effet;
    public RecyclerView recycler_view_hue_effect;
    public RecyclerView recycler_view_burn_effect;

    private View view_overlay;
    private View view_hue;
    private View view_color;
    private View view_dodge;
    private View view_hardmix;
    private View view_burn;

    private LinearLayout linearLayoutOverlay;
    private LinearLayout linearLayoutHue;
    private LinearLayout linearLayoutColor;
    private LinearLayout linearLayoutDodge;
    private LinearLayout linearLayoutBurn;
    private LinearLayout linearLayoutHardmix;

    private TextView text_view_overlay;
    private TextView text_view_color;
    private TextView text_view_dodge;
    private TextView text_view_hardmix;
    private TextView text_view_hue;
    private TextView text_view_burn;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.login_sign_up_background));
        } else {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
        }
        setContentView(R.layout.activity_photo_editor);
        CGENativeLibrary.setLoadImageCallback(this.loadImageCallback, null);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }

        initView();
        onClickListener();
        setView();
        setBottomToolbar(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
        this.keyboardProvider.onPause();
    }

    public void onResume() {
        super.onResume();
        this.keyboardProvider.onResume();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        this.relativeLayoutLoading = findViewById(R.id.relative_layout_loading);
        this.relativeLayoutLoading.setVisibility(View.VISIBLE);
        this.photoView = findViewById(R.id.photo_editor_view);
        this.photoView.setVisibility(View.INVISIBLE);
        this.recyclerViewTools = findViewById(R.id.recyclerViewTools);
        this.recyclerViewDraw = findViewById(R.id.recyclerViewDraw);
        this.recyclerViewSpalsh = findViewById(R.id.recyclerViewBlurSqaure);
        this.constraintLayoutEffects = findViewById(R.id.constraint_layout_effects);
        this.recyclerViewEffect = findViewById(R.id.recyclerViewEffect);
        this.recyclerViewFilter = findViewById(R.id.recycler_view_filter);
        this.recyclerViewAdjust = findViewById(R.id.recyclerViewAdjust);
        this.constraintLayoutView = findViewById(R.id.constraint_layout_root_view);
        this.constraintLayoutFilter = findViewById(R.id.constraint_layout_filter);
        this.constraintLayoutAdjust = findViewById(R.id.constraintLayoutAdjust);
        this.constraintLayoutOverlay = findViewById(R.id.constraint_layout_overlay);
        this.constraintLayoutConfirmCompareOverlay = findViewById(R.id.constraintLayoutConfirmCompareOverlay);
        this.constraintLayoutSaveOverlay = findViewById(R.id.constraint_layout_confirm_save_overlay);
        this.linearLayoutOverlay = findViewById(R.id.linearLayoutOverlay);
        this.linearLayoutHue = findViewById(R.id.linearLayoutHue);
        this.linearLayoutColor = findViewById(R.id.linearLayoutColor);
        this.linearLayoutDodge = findViewById(R.id.linearLayoutDodge);
        this.linearLayoutBurn = findViewById(R.id.linearLayoutBurn);
        this.linearLayoutHardmix = findViewById(R.id.linearLayoutHardmix);
        this.text_view_overlay = findViewById(R.id.text_view_overlay);
        this.text_view_color = findViewById(R.id.text_view_color);
        this.text_view_dodge = findViewById(R.id.text_view_dodge);
        this.text_view_hardmix = findViewById(R.id.text_view_hardmix);
        this.text_view_hue = findViewById(R.id.text_view_hue);
        this.text_view_burn = findViewById(R.id.text_view_burn);
        this.recycler_view_overlay_effect = findViewById(R.id.recycler_view_overlay_effect);
        this.recycler_view_color_effect = findViewById(R.id.recycler_view_color_effect);
        this.recycler_view_dodge_effect = findViewById(R.id.recycler_view_dodge_effect);
        this.recycler_view_hardmix_effet = findViewById(R.id.recycler_view_hardmix_effet);
        this.recycler_view_hue_effect = findViewById(R.id.recycler_view_hue_effect);
        this.recycler_view_burn_effect = findViewById(R.id.recycler_view_burn_effect);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.view_overlay = findViewById(R.id.view_overlay);
        this.view_hue = findViewById(R.id.view_hue);
        this.view_color = findViewById(R.id.view_color);
        this.view_dodge = findViewById(R.id.view_dodge);
        this.view_hardmix = findViewById(R.id.view_hardmix);
        this.view_burn = findViewById(R.id.view_burn);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
        this.constraintLayoutSticker = findViewById(R.id.constraint_layout_sticker);
        this.constraintLayoutAddText = findViewById(R.id.constraint_layout_confirm_text);
        this.viewPagerStickers = findViewById(R.id.stickerViewpaper);
        this.linear_layout_wrapper_sticker_list = findViewById(R.id.linear_layout_wrapper_sticker_list);
        this.guidelinePaint = findViewById(R.id.guidelinePaint);
        this.guideline = findViewById(R.id.guideline);
        this.seekbarSticker = findViewById(R.id.seekbarStickerAlpha);
        this.seekbarSticker.setVisibility(View.GONE);
        this.textViewTitleBrush = findViewById(R.id.textViewTitleBrush);
        this.imageViewBrushSize = findViewById(R.id.imageViewSizePaint);
        this.constraintLayoutSaveText = findViewById(R.id.constraint_layout_confirm_save_text);
        this.constraintLayoutSaveSticker = findViewById(R.id.constraint_layout_confirm_save_sticker);
        this.imageViewBrushOpacity = findViewById(R.id.imageViewOpacityPaint);
        this.imageViewBrushEraser = findViewById(R.id.imageViewEraserPaint);
        this.textViewValueBrush = findViewById(R.id.seekbarBrushValue);
        this.textViewValueOpacity = findViewById(R.id.seekbarOpacityValue);
        this.textViewValueEraser = findViewById(R.id.seekbarEraserValue);
        this.seekBarBrush = findViewById(R.id.seekbarBrushSize);
        this.seekBarOpacity = findViewById(R.id.seekbarOpacitySize);
        this.seekBarEraser = findViewById(R.id.seekbarEraserSize);
        this.textViewTitleMagic = findViewById(R.id.textViewTitleMagic);
        this.imageViewMagicSize = findViewById(R.id.imageViewSizeMagic);
        this.imageViewMagicOpacity = findViewById(R.id.imageViewOpacityMagic);
        this.imageViewMagicEraser = findViewById(R.id.imageViewEraserMagic);
        this.textViewMagicBrush = findViewById(R.id.seekbarBrushMagicValue);
        this.textViewMagicOpacity = findViewById(R.id.seekbarOpacityMagicValue);
        this.textViewMagicEraser = findViewById(R.id.seekbarEraserMagicValue);
        this.seekBarMagicBrush = findViewById(R.id.seekbarMagicSize);
        this.seekBarMagicOpacity = findViewById(R.id.seekbarOpacityMagic);
        this.seekBarMagicEraser = findViewById(R.id.seekbarEraserMagic);
        this.textViewTitleNeon = findViewById(R.id.textViewTitleNeon);
        this.imageViewNeonSize = findViewById(R.id.imageViewSizeNeon);
        this.imageViewNeonEraser = findViewById(R.id.imageViewEraserNeon);
        this.textViewNeonBrush = findViewById(R.id.seekbarNeonValue);
        this.textViewNeonEraser = findViewById(R.id.seekbarEraserNeon);
        this.seekBarNeonBrush = findViewById(R.id.seekbarNeonSize);
        this.seekBarNeonEraser = findViewById(R.id.seekbarNeonEraser);
        this.relativeLayoutPaint = findViewById(R.id.viewPaint);
        this.relativeLayoutNeon = findViewById(R.id.viewNeon);
        this.relativeLayoutMagic = findViewById(R.id.viewMagic);
        this.redo = findViewById(R.id.redo);
        this.undo = findViewById(R.id.undo);
        this.constraintLayoutPaint = findViewById(R.id.constraintLayoutPaint);
        this.constraintLayoutPaintTool = findViewById(R.id.constraintLayoutPaintTool);
        this.recyclerViewPaintListColor = findViewById(R.id.recyclerViewColorPaint);
        this.recyclerViewMagicListColor = findViewById(R.id.recyclerViewColorMagic);
        this.constraintLayoutMagic = findViewById(R.id.constraintLayoutMagic);
        this.constraintLayoutMagicTool = findViewById(R.id.constraintLayoutMagicTool);
        this.constraintLayoutNeon = findViewById(R.id.constraintLayoutNeon);
        this.constraintLayoutNeonTool = findViewById(R.id.constraintLayoutNeonTool);
        this.recyclerViewNeonListColor = findViewById(R.id.recyclerViewColorNeon);
        this.imageViewUndoPaint = findViewById(R.id.image_view_undo);
        this.imageViewUndoPaint.setVisibility(View.GONE);
        this.imageViewUndoMagic = findViewById(R.id.image_view_undo_Magic);
        this.imageViewUndoMagic.setVisibility(View.GONE);
        this.imageViewRedoPaint = findViewById(R.id.image_view_redo);
        this.imageViewRedoPaint.setVisibility(View.GONE);
        this.imageViewRedoMagic = findViewById(R.id.image_view_redo_Magic);
        this.imageViewRedoMagic.setVisibility(View.GONE);
        this.imageViewCleanPaint = findViewById(R.id.image_view_clean);
        this.imageViewCleanPaint.setVisibility(View.GONE);
        this.imageViewCleanMagic = findViewById(R.id.image_view_clean_Magic);
        this.imageViewCleanMagic.setVisibility(View.GONE);
        this.imageViewCleanNeon = findViewById(R.id.image_view_clean_neon);
        this.imageViewCleanNeon.setVisibility(View.GONE);
        this.imageViewUndoNeon = findViewById(R.id.image_view_undo_neon);
        this.imageViewUndoNeon.setVisibility(View.GONE);
        this.imageViewRedoNeon = findViewById(R.id.image_view_redo_neon);
        this.imageViewRedoNeon.setVisibility(View.GONE);
        this.relativeLayoutWrapper = findViewById(R.id.relative_layout_wrapper_photo);
        this.textViewSaveEditing = findViewById(R.id.text_view_save);
        this.image_view_exit = findViewById(R.id.image_view_exit);
        this.constraintLayoutSave = findViewById(R.id.constraintLayoutSave);
        this.constraintLayoutDraw = findViewById(R.id.constraint_layout_draw);
        this.constraintLayoutSplash = findViewById(R.id.constraint_layout_blur_sqaure);
        this.imageViewCompareAdjust = findViewById(R.id.imageViewCompareAdjust);
        this.imageViewCompareAdjust.setOnTouchListener(this.onTouchListener);
        this.imageViewCompareFilter = findViewById(R.id.image_view_compare_filter);
        this.imageViewCompareFilter.setOnTouchListener(this.onTouchListener);
        this.imageViewCompareFilter.setVisibility(View.GONE);
        this.imageViewCompareOverlay = findViewById(R.id.image_view_compare_overlay);
        this.imageViewCompareOverlay.setOnTouchListener(this.onTouchListener);
        this.imageViewCompareOverlay.setVisibility(View.GONE);
        this.relativeLayoutAddText = findViewById(R.id.relative_layout_add_text);
    }

    private void setOnBackPressDialog() {
        final Dialog dialogOnBackPressed = new Dialog(PhotoEditorActivity.this, R.style.UploadDialog);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.show();
        this.textViewCancel = dialogOnBackPressed.findViewById(R.id.textViewCancel);
        this.textViewDiscard = dialogOnBackPressed.findViewById(R.id.textViewDiscard);
        this.textViewCancel.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
        });

        this.textViewDiscard.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
            PhotoEditorActivity.this.moduleToolsId = null;
            PhotoEditorActivity.this.finish();
            finish();
        });

    }

    private void setView() {
        this.recyclerViewTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewTools.setAdapter(this.mEditingToolsAdapter);
        this.recyclerViewTools.setHasFixedSize(true);
        this.recyclerViewDraw.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewDraw.setAdapter(this.mEditingDrawToolsAdapter);
        this.recyclerViewDraw.setHasFixedSize(true);
        this.recyclerViewSpalsh.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewSpalsh.setAdapter(this.mEditingSplashToolsAdapter);
        this.recyclerViewSpalsh.setHasFixedSize(true);
        this.recyclerViewEffect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewEffect.setAdapter(this.mEditingEffectToolsAdapter);
        this.recyclerViewFilter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewFilter.setHasFixedSize(true);
        this.recycler_view_overlay_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_overlay_effect.setHasFixedSize(true);
        this.recycler_view_color_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_color_effect.setHasFixedSize(true);
        this.recycler_view_dodge_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_dodge_effect.setHasFixedSize(true);
        this.recycler_view_hardmix_effet.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_hardmix_effet.setHasFixedSize(true);
        this.recycler_view_hue_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_hue_effect.setHasFixedSize(true);
        this.recycler_view_burn_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_burn_effect.setHasFixedSize(true);
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        this.recyclerViewAdjust.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewAdjust.setHasFixedSize(true);
        this.mAdjustAdapter = new AdjustAdapter(getApplicationContext(), this);
        this.recyclerViewAdjust.setAdapter(this.mAdjustAdapter);
        this.recyclerViewPaintListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewPaintListColor.setHasFixedSize(true);
        this.recyclerViewPaintListColor.setAdapter(new ColorAdapter(getApplicationContext(), this));
        this.recyclerViewNeonListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewNeonListColor.setHasFixedSize(true);
        this.recyclerViewNeonListColor.setAdapter(new ColorAdapter(getApplicationContext(), this));
        this.recyclerViewMagicListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewMagicListColor.setHasFixedSize(true);
        this.recyclerViewMagicListColor.setAdapter(new MagicBrushAdapter(getApplicationContext(), this));

        // new Handler().postDelayed(this::checkData, 3000);

        viewPagerStickers.setAdapter(new PagerAdapter() {
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
                View inflate = LayoutInflater.from(PhotoEditorActivity.this.getBaseContext()).inflate(R.layout.list_sticker, null, false);
                RecyclerView recycler_view_sticker = inflate.findViewById(R.id.recyclerViewSticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(PhotoEditorActivity.this.getApplicationContext(), 7));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.bubbleList(), i, PhotoEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.rainbowList(), i, PhotoEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.flowerList(), i, PhotoEditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.amojiList(), i, PhotoEditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.deliciousList(), i, PhotoEditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.popularList(), i, PhotoEditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.valentineList(), i, PhotoEditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.emojList(), i, PhotoEditorActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.christmasList(), i, PhotoEditorActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.unicornList(), i, PhotoEditorActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.stickerList(), i, PhotoEditorActivity.this));
                        break;


                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickersTabAdapter(viewPagerStickers, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            new loadBitmapUri().execute(bundle.getString(PhotoPickerView.KEY_SELECTED_PHOTOS));
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        CGENativeLibrary.setLoadImageCallback(this.loadImageCallback, null);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        this.photoEditor = new PhotoEditor.Builder(this, this.photoView).setPinchTextScalable(true).build();
        this.photoEditor.setOnPhotoEditorListener(this);

        Preference.setKeyboard(getApplicationContext(), 0);
        this.keyboardProvider = new KeyboardHeightProvider(this);
        this.keyboardProvider.addKeyboardListener(i -> {
            if (i <= 0) {
                Preference.setHeightOfNotch(getApplicationContext(), -i);
            } else if (textFragment != null) {
                textFragment.updateAddTextBottomToolbarHeight(Preference.getHeightOfNotch(getApplicationContext()) + i);
                Preference.setKeyboard(getApplicationContext(), i + Preference.getHeightOfNotch(getApplicationContext()));
            }
        });
    }

    private void  onClickListener(){
        this.textViewSaveEditing.setOnClickListener(view -> {
            SaveView();
        });

        this.undo.setOnClickListener(view -> PhotoEditorActivity.this.setUndo());
        this.redo.setOnClickListener(view -> PhotoEditorActivity.this.setRedo());
        this.linearLayoutOverlay.setOnClickListener(view -> PhotoEditorActivity.this.setOverlayEffect());
        this.linearLayoutColor.setOnClickListener(view -> PhotoEditorActivity.this.setColorEffect());
        this.linearLayoutDodge.setOnClickListener(view -> PhotoEditorActivity.this.setDodgeEffect());
        this.linearLayoutHardmix.setOnClickListener(view -> PhotoEditorActivity.this.setHardMixEffect());
        this.linearLayoutHue.setOnClickListener(view -> PhotoEditorActivity.this.setHueEffect());
        this.linearLayoutBurn.setOnClickListener(view -> PhotoEditorActivity.this.setBurnEffect());

        this.image_view_exit.setOnClickListener(view -> PhotoEditorActivity.this.onBackPressed());
        this.imageViewBrushEraser.setOnClickListener(view -> PhotoEditorActivity.this.setErasePaint());
        this.imageViewBrushSize.setOnClickListener(view -> PhotoEditorActivity.this.setColorPaint());
        this.imageViewBrushOpacity.setOnClickListener(view -> PhotoEditorActivity.this.setPaintOpacity());

        this.imageViewNeonEraser.setOnClickListener(view -> PhotoEditorActivity.this.setEraseNeon());
        this.imageViewNeonSize.setOnClickListener(view -> PhotoEditorActivity.this.setColorNeon());

        this.imageViewMagicEraser.setOnClickListener(view -> PhotoEditorActivity.this.setEraseMagic());
        this.imageViewMagicSize.setOnClickListener(view -> PhotoEditorActivity.this.setMagicBrush());
        this.imageViewMagicOpacity.setOnClickListener(view -> PhotoEditorActivity.this.setMagicOpacity());

        this.seekBarBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoEditor.setBrushSize((float) (i + 5));
                String brshValue = String.valueOf(i);
                textViewValueBrush.setText(brshValue);
            }
        });
        this.seekBarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoEditor.setPaintOpacity(i);
                String brshValue = String.valueOf(i);
                textViewValueOpacity.setText(brshValue);
            }
        });
        this.seekBarEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoEditor.setBrushEraserSize((float) i);
                PhotoEditorActivity.this.photoEditor.brushEraser();
                String brshValue = String.valueOf(i);
                textViewValueEraser.setText(brshValue);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.seekBarMagicBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoEditor.setBrushSize((float) (i + 5));
                String brshValue = String.valueOf(i);
                textViewMagicBrush.setText(brshValue);
            }
        });
        this.seekBarMagicOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoEditor.setMagicOpacity(i);
                String brshValue = String.valueOf(i);
                textViewMagicOpacity.setText(brshValue);
            }
        });
        this.seekBarMagicEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoEditor.setBrushEraserSize((float) i);
                PhotoEditorActivity.this.photoEditor.brushEraser();
                String brshValue = String.valueOf(i);
                textViewMagicEraser.setText(brshValue);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.seekBarNeonBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoEditor.setBrushSize((float) (i + 5));
                String brshValue = String.valueOf(i);
                textViewNeonBrush.setText(brshValue);
            }
        });
        this.seekBarNeonEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoEditor.setBrushEraserSize((float) i);
                PhotoEditorActivity.this.photoEditor.brushEraser();
                String brshValue = String.valueOf(i);
                textViewNeonEraser.setText(brshValue);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        this.seekbarSticker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = PhotoEditorActivity.this.photoView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });

        this.imageViewAddSticker = findViewById(R.id.imageViewAddSticker);
        this.imageViewAddSticker.setVisibility(View.GONE);
        this.imageViewAddSticker.setOnClickListener(view -> {
            PhotoEditorActivity.this.imageViewAddSticker.setVisibility(View.GONE);
            PhotoEditorActivity.this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
        });

        this.relativeLayoutAddText.setOnClickListener(view -> {
            PhotoEditorActivity.this.photoView.setHandlingSticker(null);
            PhotoEditorActivity.this.textFragment();
        });
        this.adjustSeekBar = (DegreeSeekBar) findViewById(R.id.seekbarAdjust);
        this.adjustSeekBar.setCenterTextColor(getResources().getColor(R.color.mainColor));
        this.adjustSeekBar.setTextColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setPointColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setDegreeRange(-50, 50);
        this.adjustSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
            public void onScrollEnd() {
            }

            public void onScrollStart() {
            }

            public void onScroll(int i) {
                AdjustAdapter.AdjustModel currentAdjustModel = PhotoEditorActivity.this.mAdjustAdapter.getCurrentAdjustModel();
                currentAdjustModel.originValue = (((float) Math.abs(i + 50)) * ((currentAdjustModel.maxValue - ((currentAdjustModel.maxValue + currentAdjustModel.minValue) / 2.0f)) / 50.0f)) + currentAdjustModel.minValue;
                PhotoEditorActivity.this.photoEditor.setAdjustFilter(PhotoEditorActivity.this.mAdjustAdapter.getFilterConfig());
            }
        });
        this.adjustFilter = (DegreeSeekBar) findViewById(R.id.seekbarFilter);
        this.adjustFilter.setCenterTextColor(getResources().getColor(R.color.mainColor));
        this.adjustFilter.setTextColor(getResources().getColor(R.color.white));
        this.adjustFilter.setPointColor(getResources().getColor(R.color.white));
        this.adjustFilter.setDegreeRange(0, 100);
        this.adjustFilter.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
            public void onScrollEnd() {
            }

            public void onScrollStart() {
            }

            public void onScroll(int i) {
                PhotoEditorActivity.this.photoView.setFilterIntensity(((float) i) / 100.0f);
            }
        });

        this.seekBarOverlay = findViewById(R.id.seekbarOverlay);
        this.seekBarOverlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photoView.setFilterIntensity(((float) i) / 100.0f);
            }
        });


        photoStickerIconClose = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, PhotoStickerIcons.DELETE);
        photoStickerIconClose.setIconEvent(new DeleteIconEvent());
        photoStickerIconScale = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, PhotoStickerIcons.SCALE);
        photoStickerIconScale.setIconEvent(new ZoomIconEvent());
        photoStickerIconFlip = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, PhotoStickerIcons.FLIP);
        photoStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        photoStickerIconRotate = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, PhotoStickerIcons.ROTATE);
        photoStickerIconRotate.setIconEvent(new ZoomIconEvent());
        photoStickerIconEdit = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, PhotoStickerIcons.EDIT);
        photoStickerIconEdit.setIconEvent(new EditTextIconEvent());
        photoStickerIconAlign = new PhotoStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, PhotoStickerIcons.ALIGN);
        photoStickerIconAlign.setIconEvent(new AlignHorizontallyEvent());
        this.photoView.setIcons(Arrays.asList(photoStickerIconClose, photoStickerIconScale,
                photoStickerIconFlip, photoStickerIconEdit, photoStickerIconRotate, photoStickerIconAlign));
        this.photoView.setBackgroundColor(-16777216);
        this.photoView.setLocked(false);
        this.photoView.setConstrained(true);
        this.photoView.setOnStickerOperationListener(new PhotoStickerView.OnStickerOperationListener() {
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
                PhotoEditorActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
                PhotoEditorActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
            }

            public void onStickerSelected(@NonNull Sticker sticker) {
                if (sticker instanceof PhotoTextView) {
                    ((PhotoTextView) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    PhotoEditorActivity.this.photoView.replace(sticker);
                    PhotoEditorActivity.this.photoView.invalidate();
                }
                PhotoEditorActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
                PhotoEditorActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
            }

            public void onStickerDeleted(@NonNull Sticker sticker) {
                PhotoEditorActivity.this.seekbarSticker.setVisibility(View.GONE);
            }

            public void onStickerTouchOutside() {
                PhotoEditorActivity.this.seekbarSticker.setVisibility(View.GONE);
            }

            public void onStickerDoubleTap(@NonNull Sticker sticker) {
                if (sticker instanceof PhotoTextView) {
                    sticker.setShow(false);
                    PhotoEditorActivity.this.photoView.setHandlingSticker((Sticker) null);
                    PhotoEditorActivity.this.textFragment = TextFragment.show(PhotoEditorActivity.this, ((PhotoTextView) sticker).getPhotoText());
                    PhotoEditorActivity.this.textEditor = new TextFragment.TextEditor() {
                        public void onDone(PhotoText photoText) {
                            PhotoEditorActivity.this.photoView.getStickers().remove(PhotoEditorActivity.this.photoView.getLastHandlingSticker());
                            PhotoEditorActivity.this.photoView.addSticker(new PhotoTextView(PhotoEditorActivity.this, photoText));
                        }

                        public void onBackButton() {
                            PhotoEditorActivity.this.photoView.showLastHandlingSticker();
                        }
                    };
                    PhotoEditorActivity.this.textFragment.setOnTextEditorListener(PhotoEditorActivity.this.textEditor);
                }
            }
        });
    }


    public void setOverlayEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.VISIBLE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.VISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setColorEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.VISIBLE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.VISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setDodgeEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.VISIBLE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.VISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setHardMixEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.VISIBLE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.VISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setHueEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.VISIBLE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.VISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setBurnEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.VISIBLE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.VISIBLE);
    }

    private void setBottomToolbar(boolean z) {
        int mVisibility = !z ? View.GONE : View.VISIBLE;
        this.imageViewUndoPaint.setVisibility(mVisibility);
        this.imageViewRedoPaint.setVisibility(mVisibility);
        this.imageViewCleanPaint.setVisibility(mVisibility);
        this.imageViewCleanNeon.setVisibility(mVisibility);
        this.imageViewUndoNeon.setVisibility(mVisibility);
        this.imageViewRedoNeon.setVisibility(mVisibility);
        this.imageViewCleanMagic.setVisibility(mVisibility);
        this.imageViewUndoMagic.setVisibility(mVisibility);
        this.imageViewRedoMagic.setVisibility(mVisibility);
    }

    public void setErasePaint() {
        relativeLayoutPaint.setVisibility(View.VISIBLE);
        this.recyclerViewPaintListColor.setVisibility(View.VISIBLE);
        this.photoEditor.brushEraser();
        this.seekBarEraser.setProgress(20);
        this.seekBarEraser.setVisibility(View.VISIBLE);
        this.seekBarOpacity.setVisibility(View.GONE);
        this.seekBarBrush.setVisibility(View.GONE);
        this.textViewValueEraser.setVisibility(View.VISIBLE);
        this.textViewValueBrush.setVisibility(View.GONE);
        this.textViewValueOpacity.setVisibility(View.GONE);
        this.textViewTitleBrush.setText("Eraser");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser_select);
    }

    public void setColorPaint() {
        this.relativeLayoutPaint.setVisibility(View.GONE);
        this.recyclerViewPaintListColor.setVisibility(View.VISIBLE);
        this.recyclerViewPaintListColor.scrollToPosition(0);
        colorAdapter = (ColorAdapter) this.recyclerViewPaintListColor.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        this.photoEditor.setBrushMode(1);
        this.photoEditor.setBrushDrawingMode(true);
        this.seekBarBrush.setProgress(20);
        this.seekBarEraser.setVisibility(View.GONE);
        this.seekBarOpacity.setVisibility(View.GONE);
        this.seekBarBrush.setVisibility(View.VISIBLE);
        this.textViewValueEraser.setVisibility(View.GONE);
        this.textViewValueBrush.setVisibility(View.VISIBLE);
        this.textViewValueOpacity.setVisibility(View.GONE);
        this.textViewTitleBrush.setText("Brush");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setPaintOpacity() {
        this.relativeLayoutPaint.setVisibility(View.GONE);
        this.seekBarOpacity.setProgress(100);
        this.seekBarEraser.setVisibility(View.GONE);
        this.seekBarOpacity.setVisibility(View.VISIBLE);
        this.seekBarBrush.setVisibility(View.GONE);
        this.textViewValueEraser.setVisibility(View.GONE);
        this.textViewValueBrush.setVisibility(View.GONE);
        this.textViewValueOpacity.setVisibility(View.VISIBLE);
        this.textViewTitleBrush.setText("Opacity");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity_select);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setEraseNeon() {
        relativeLayoutNeon.setVisibility(View.VISIBLE);
        this.recyclerViewNeonListColor.setVisibility(View.VISIBLE);
        this.photoEditor.brushEraser();
        this.seekBarNeonEraser.setProgress(20);
        this.seekBarNeonEraser.setVisibility(View.VISIBLE);
        this.seekBarNeonBrush.setVisibility(View.GONE);
        this.textViewNeonEraser.setVisibility(View.VISIBLE);
        this.textViewNeonBrush.setVisibility(View.GONE);
        this.textViewTitleNeon.setText("Eraser");
        this.imageViewNeonSize.setImageResource(R.drawable.ic_brush);
        this.imageViewNeonEraser.setImageResource(R.drawable.ic_eraser_select);
    }

    public void setColorNeon() {
        this.relativeLayoutNeon.setVisibility(View.GONE);
        this.recyclerViewNeonListColor.setVisibility(View.VISIBLE);
        this.recyclerViewNeonListColor.scrollToPosition(0);
        colorAdapter = (ColorAdapter) this.recyclerViewNeonListColor.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        this.photoEditor.setBrushMode(2);
        this.photoEditor.setBrushDrawingMode(true);
        this.seekBarNeonBrush.setProgress(20);
        this.seekBarNeonEraser.setVisibility(View.GONE);
        this.seekBarNeonBrush.setVisibility(View.VISIBLE);
        this.textViewNeonEraser.setVisibility(View.GONE);
        this.textViewNeonBrush.setVisibility(View.VISIBLE);
        this.textViewTitleNeon.setText("Brush");
        this.imageViewNeonSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewNeonEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setEraseMagic() {
        this.relativeLayoutMagic.setVisibility(View.VISIBLE);
        this.recyclerViewMagicListColor.setVisibility(View.VISIBLE);
        this.photoEditor.brushEraser();
        this.seekBarMagicEraser.setProgress(20);
        this.seekBarMagicEraser.setVisibility(View.VISIBLE);
        this.seekBarMagicOpacity.setVisibility(View.GONE);
        this.seekBarMagicBrush.setVisibility(View.GONE);
        this.textViewMagicEraser.setVisibility(View.VISIBLE);
        this.textViewMagicBrush.setVisibility(View.GONE);
        this.textViewMagicOpacity.setVisibility(View.GONE);
        this.textViewTitleMagic.setText("Eraser");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser_select);
    }

    public void setMagicBrush() {
        this.relativeLayoutMagic.setVisibility(View.GONE);
        this.recyclerViewMagicListColor.setVisibility(View.VISIBLE);
        this.recyclerViewMagicListColor.scrollToPosition(0);
        this.photoEditor.setBrushMagic(MagicBrushAdapter.lstDrawBitmapModel(getApplicationContext()).get(0));
        MagicBrushAdapter magicBrushAdapter = (MagicBrushAdapter) this.recyclerViewMagicListColor.getAdapter();
        if (magicBrushAdapter != null) {
            magicBrushAdapter.setSelectedColorIndex(0);
        }
        this.recyclerViewMagicListColor.scrollToPosition(0);
        if (magicBrushAdapter != null) {
            magicBrushAdapter.notifyDataSetChanged();
        }
        this.photoEditor.setBrushMode(3);
        this.photoEditor.setBrushDrawingMode(true);
        this.seekBarMagicBrush.setProgress(20);
        this.seekBarMagicEraser.setVisibility(View.GONE);
        this.seekBarMagicOpacity.setVisibility(View.GONE);
        this.seekBarMagicBrush.setVisibility(View.VISIBLE);
        this.textViewMagicEraser.setVisibility(View.GONE);
        this.textViewMagicBrush.setVisibility(View.VISIBLE);
        this.textViewMagicOpacity.setVisibility(View.GONE);
        this.textViewTitleMagic.setText("Brush");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setMagicOpacity() {
        this.relativeLayoutMagic.setVisibility(View.GONE);
        this.photoEditor.setBrushMode(3);
        this.seekBarMagicOpacity.setProgress(100);
        this.photoEditor.setBrushDrawingMode(true);
        this.seekBarMagicEraser.setVisibility(View.GONE);
        this.seekBarMagicOpacity.setVisibility(View.VISIBLE);
        this.seekBarMagicBrush.setVisibility(View.GONE);
        this.textViewMagicEraser.setVisibility(View.GONE);
        this.textViewMagicBrush.setVisibility(View.GONE);
        this.textViewMagicOpacity.setVisibility(View.VISIBLE);
        this.textViewTitleMagic.setText("Opacity");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity_select);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void viewSlideUp(final View showLayout) {
        showLayout.setVisibility(View.VISIBLE);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        showLayout.startAnimation(slideUpAnimation);
        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void viewSlideDown(final View hideLayout) {
        hideLayout.setVisibility(View.GONE);
        hideLayout.startAnimation(slideDownAnimation);
        slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }


    public CGENativeLibrary.LoadImageCallback loadImageCallback = new CGENativeLibrary.LoadImageCallback() {
        public Bitmap loadImage(String string, Object object) {
            try {
                return BitmapFactory.decodeStream(PhotoEditorActivity.this.getAssets().open(string));
            } catch (IOException ioException) {
                return null;
            }
        }

        public void loadImageOK(Bitmap bitmap, Object object) {
            bitmap.recycle();
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    View.OnTouchListener onTouchListener = (view, motionEvent) -> {
        switch (motionEvent.getAction()) {
            case 0:
                PhotoEditorActivity.this.photoView.getGLSurfaceView().setAlpha(0.0f);
                return true;
            case 1:
                PhotoEditorActivity.this.photoView.getGLSurfaceView().setAlpha(1.0f);
                return false;
            default:
                return true;
        }
    };

    public void onRequestPermissionsResult(int i, @NonNull String[] string, @NonNull int[] i2) {
        super.onRequestPermissionsResult(i, string, i2);
    }

    public void onAddViewListener(Drawing viewType, int i) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(int i) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(Drawing viewType, int i) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onStartViewChangeListener(Drawing viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    public void onStopViewChangeListener(Drawing viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    private void setUndo() {
        photoView.undo();
    }

    private void setRedo() {
        photoView.redo();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewCloseAdjust:
            case R.id.image_view_close_sticker:
            case R.id.imageViewCloseText:
            case R.id.imageViewCloseFilter:
            case R.id.imageViewCloseOverlay:
                setVisibleSave();
                onBackPressed();
                return;
            case R.id.imageViewSaveAdjust:
                new SaveFilter().execute();
                this.constraintLayoutAdjust.setVisibility(View.GONE);
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraintLayoutSave.setVisibility(View.VISIBLE);
                setGuideLine();
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSavePaint:
                showLoading(true);
                runOnUiThread(() -> {
                    photoEditor.setBrushDrawingMode(false);
                    imageViewUndoPaint.setVisibility(View.GONE);
                    imageViewRedoPaint.setVisibility(View.GONE);
                    imageViewCleanPaint.setVisibility(View.GONE);
                    this.constraintLayoutPaintTool.setVisibility(View.GONE);
                    this.constraintLayoutSave.setVisibility(View.VISIBLE);
                    viewSlideUp(recyclerViewTools);
                    viewSlideDown(constraintLayoutPaint);
                    setGuideLine();
                    photoView.setImageSource(photoEditor.getBrushDrawingView().getDrawBitmap(photoView.getCurrentBitmap()));
                    photoEditor.clearBrushAllViews();
                    showLoading(false);
                    reloadingLayout();
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveMagic:
                showLoading(true);
                runOnUiThread(() -> {
                    photoEditor.setBrushDrawingMode(false);
                    imageViewUndoMagic.setVisibility(View.GONE);
                    imageViewRedoMagic.setVisibility(View.GONE);
                    imageViewCleanMagic.setVisibility(View.GONE);
                    viewSlideUp(recyclerViewTools);
                    viewSlideDown(constraintLayoutMagic);
                    this.constraintLayoutMagicTool.setVisibility(View.GONE);
                    this.constraintLayoutSave.setVisibility(View.VISIBLE);
                    setGuideLine();
                    photoView.setImageSource(photoEditor.getBrushDrawingView().getDrawBitmap(photoView.getCurrentBitmap()));
                    photoEditor.clearBrushAllViews();
                    showLoading(false);
                    reloadingLayout();
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveNeon:
                showLoading(true);
                runOnUiThread(() -> {
                    photoEditor.setBrushDrawingMode(false);
                    imageViewUndoNeon.setVisibility(View.GONE);
                    imageViewRedoNeon.setVisibility(View.GONE);
                    this.constraintLayoutNeonTool.setVisibility(View.GONE);
                    this.constraintLayoutSave.setVisibility(View.VISIBLE);
                    viewSlideUp(recyclerViewTools);
                    viewSlideDown(constraintLayoutNeon);
                    setGuideLine();
                    photoView.setImageSource(photoEditor.getBrushDrawingView().getDrawBitmap(photoView.getCurrentBitmap()));
                    photoEditor.clearBrushAllViews();
                    showLoading(false);
                    reloadingLayout();
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveFilter:
                new SaveFilter().execute();
                this.imageViewCompareFilter.setVisibility(View.GONE);
                viewSlideUp(recyclerViewTools);
                viewSlideDown(constraintLayoutFilter);
                setGuideLine();
                this.moduleToolsId = Module.NONE;
                setVisibleSave();
                return;
            case R.id.imageViewSaveOverlay:
                new SaveFilter().execute();
                this.imageViewCompareOverlay.setVisibility(View.GONE);
                this.constraintLayoutSaveOverlay.setVisibility(View.GONE);
                seekBarOverlay.setVisibility(View.GONE);
                viewSlideUp(recyclerViewTools);
                viewSlideDown(constraintLayoutOverlay);
                this.moduleToolsId = Module.NONE;
                setGuideLine();
                setVisibleSave();
                return;
            case R.id.image_view_save_sticker:
                this.photoView.setHandlingSticker(null);
                this.photoView.setLocked(true);
                this.seekbarSticker.setVisibility(View.GONE);
                this.imageViewAddSticker.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                if (!this.photoView.getStickers().isEmpty()) {
                    new SaveSticker().execute();
                }
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                viewSlideUp(recyclerViewTools);
                viewSlideDown(constraintLayoutSticker);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                setGuideLine();
                return;
            case R.id.imageViewSaveText:
                this.photoView.setHandlingSticker(null);
                this.photoView.setLocked(true);
                this.constraintLayoutSaveText.setVisibility(View.GONE);
                setGuideLine();
                if (!this.photoView.getStickers().isEmpty()) {
                    new SaveSticker().execute();
                }
                viewSlideUp(recyclerViewTools);
                viewSlideDown(constraintLayoutAddText);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.image_view_redo_neon:
            case R.id.image_view_redo_Magic:
            case R.id.image_view_redo:
                this.photoEditor.redoBrush();
                return;
            case R.id.image_view_undo_neon:
            case R.id.image_view_undo:
            case R.id.image_view_undo_Magic:
                this.photoEditor.undoBrush();
                return;
            case R.id.image_view_clean_neon:
            case R.id.image_view_clean_Magic:
            case R.id.image_view_clean:
                this.photoEditor.clearBrushAllViews();
                return;
            default:
        }
    }

    public void isPermissionGranted(boolean z, String string) {
        if (z) {
            new SaveEditingBitmap().execute();
        }
    }


    public void textFragment() {
        this.textFragment = TextFragment.show(this);
        this.textEditor = new TextFragment.TextEditor() {
            public void onDone(PhotoText photoText) {
                PhotoEditorActivity.this.photoView.addSticker(new PhotoTextView(PhotoEditorActivity.this.getApplicationContext(), photoText));
            }

            public void onBackButton() {
                if (PhotoEditorActivity.this.photoView.getStickers().isEmpty()) {
                    PhotoEditorActivity.this.onBackPressed();
                }
            }
        };
        this.textFragment.setOnTextEditorListener(this.textEditor);
    }

    public void onQuShotToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case TEXT:
                setGoneSave();
                setGuideLine();
                this.photoView.setLocked(false);
                textFragment();
                viewSlideDown(recyclerViewTools);
                viewSlideUp(constraintLayoutAddText);
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.VISIBLE);
                break;
            case STICKER:
                setGoneSave();
                setGuideLine();
                this.photoView.setLocked(false);
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.VISIBLE);
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                if (!this.photoView.getStickers().isEmpty()) {
                    this.photoView.getStickers().clear();
                    this.photoView.setHandlingSticker(null);
                }
                viewSlideDown(recyclerViewTools);
                viewSlideUp(constraintLayoutSticker);
                break;
            case ADJUST:
                setGoneSave();
                setGuideLinePaint();
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                viewSlideDown(recyclerViewTools);
                viewSlideUp(constraintLayoutAdjust);
                this.adjustSeekBar.setCurrentDegrees(0);
                this.mAdjustAdapter = new AdjustAdapter(getApplicationContext(), this);
                this.recyclerViewAdjust.setAdapter(this.mAdjustAdapter);
                this.photoEditor.setAdjustFilter(this.mAdjustAdapter.getFilterConfig());
                break;
            case FILTER:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                setGoneSave();
                new openFilters().execute();
                break;
            case DRAW:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                viewSlideUp(constraintLayoutDraw);
                break;
            case EFFECT:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                viewSlideUp(constraintLayoutEffects);
                break;
            case RATIO:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new openBlurFragment().execute();
                goneLayout();
                break;
            case BACKGROUND:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new openFrameFragment().execute();
                goneLayout();
                break;
            case HSL:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                HSlFragment.show(this, this, this.photoView.getCurrentBitmap());
                goneLayout();
                break;
            case MIRROR:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new PhotoEditorActivity.openMirrorFragment().execute(new Void[0]);
                goneLayout();
                break;
            case SQ_BG:
                viewSlideUp(constraintLayoutSplash);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                break;
            case CROP:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                CropFragment.show(this, this, this.photoView.getCurrentBitmap());
                goneLayout();
                break;
            case BLURE:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                BitmapTransfer.bitmap = this.photoView.getCurrentBitmap();
                Intent intent1 = new Intent(PhotoEditorActivity.this, BlurLayout.class);
                startActivityForResult(intent1, 900);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

        }
        this.photoView.setHandlingSticker(null);
    }

    public void onQuShotEffectToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case OVERLAY:
                setGoneSave();
                this.constraintLayoutSaveOverlay.setVisibility(View.VISIBLE);
                new effectOvarlay().execute();
                new effectColor().execute();
                new effectHardmix().execute();
                new effectDodge().execute();
                new effectDivide().execute();
                new effectBurn().execute();
                seekBarOverlay.setVisibility(View.VISIBLE);
                break;
            case NEON:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new neonEffect().execute();
                break;
            case PIX:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new pixEffect().execute();
                break;
            case ART:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new artEffect().execute();
                break;
            case WINGS:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new wingEffect().execute();
                break;
            case MOTION:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                new motionEffect().execute();
                goneLayout();
                break;
            case SPLASH:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                BitmapTransfer.bitmap = this.photoView.getCurrentBitmap();
                Intent intent = new Intent(PhotoEditorActivity.this, SplashLayout.class);
                startActivityForResult(intent, 900);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case DRIP:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new dripEffect().execute();
                break;
        }
        this.photoView.setHandlingSticker(null);
    }

    public void onQuShotSQToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case SPLASH_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSplashSquareBackgroundFragment(true).execute();
                break;
            case SPLASH_SQ:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSplashFragment(true).execute();
                break;
            case SKETCH_SQ:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSketchFragment(true).execute();
                break;
            case SKETCH_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSketchBackgroundFragment(true).execute();
                break;
            case BLUR_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openBlurSquareBackgroundFragment(true).execute();
                break;
        }
        this.photoView.setHandlingSticker(null);
    }

    public void onQuShotDrawToolSelected(Module module) {
        this.moduleToolsId = module;
        ConstraintSet constraintSet;
        switch (module) {
            case PAINT:
                setColorPaint();
                this.photoEditor.setBrushDrawingMode(true);
                this.constraintLayoutPaint.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSave.setVisibility(View.GONE);
                PhotoEditorActivity.this.constraintLayoutPaint.setVisibility(View.VISIBLE);
                this.constraintLayoutPaintTool.setVisibility(View.VISIBLE);
                this.photoEditor.clearBrushAllViews();
                this.photoEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(this.constraintLayoutView);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutPaint.getId(), 3, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet.applyTo(this.constraintLayoutView);
                this.photoEditor.setBrushMode(1);
                reloadingLayout();
                break;
            case COLORED:
                new openColoredFragment().execute();
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
            case NEON:
                setColorNeon();
                this.photoEditor.setBrushDrawingMode(true);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutNeonTool.setVisibility(View.VISIBLE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                PhotoEditorActivity.this.constraintLayoutSave.setVisibility(View.GONE);
                PhotoEditorActivity.this.constraintLayoutNeon.setVisibility(View.VISIBLE);
                this.photoEditor.clearBrushAllViews();
                this.photoEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(this.constraintLayoutView);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutNeon.getId(), 3, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet.applyTo(this.constraintLayoutView);
                this.photoEditor.setBrushMode(2);
                reloadingLayout();
                break;
            case MAGIC:
                setMagicBrush();
                this.photoEditor.setBrushDrawingMode(true);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutMagic.setVisibility(View.VISIBLE);
                this.constraintLayoutMagicTool.setVisibility(View.VISIBLE);
                this.photoEditor.clearBrushAllViews();
                this.photoEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(this.constraintLayoutView);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutMagic.getId(), 3, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet.applyTo(this.constraintLayoutView);
                this.photoEditor.setBrushMode(3);
                reloadingLayout();
                break;
            case MOSAIC:
                new openShapeFragment().execute();
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
        }
        this.photoView.setHandlingSticker(null);
    }

    private void goneLayout() {
        setVisibleSave();
    }

    public void setGoneSave() {
        this.constraintLayoutSave.setVisibility(View.GONE);
    }

    public void setGuideLine() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraintLayoutView);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.guideline.getId(), 3, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
        constraintSet.applyTo(this.constraintLayoutView);
    }

    public void setGuideLinePaint() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraintLayoutView);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.guidelinePaint.getId(), 3, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
        constraintSet.applyTo(this.constraintLayoutView);
    }

    public void setVisibleSave() {
        this.constraintLayoutSave.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (this.moduleToolsId != null) {
            try {
                switch (this.moduleToolsId) {
                    case PAINT:
                        setVisibleSave();
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutPaint);
                        this.imageViewUndoPaint.setVisibility(View.GONE);
                        this.imageViewRedoPaint.setVisibility(View.GONE);
                        this.imageViewCleanPaint.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.constraintLayoutPaintTool.setVisibility(View.GONE);
                        this.photoEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.photoEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case MAGIC:
                        setVisibleSave();
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutMagic);
                        this.imageViewUndoMagic.setVisibility(View.GONE);
                        this.imageViewRedoMagic.setVisibility(View.GONE);
                        this.imageViewCleanMagic.setVisibility(View.GONE);
                        this.constraintLayoutMagicTool.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.photoEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.photoEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case NEON:
                        setVisibleSave();
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutNeon);
                        this.imageViewUndoNeon.setVisibility(View.GONE);
                        this.imageViewRedoNeon.setVisibility(View.GONE);
                        this.constraintLayoutNeonTool.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.photoEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.photoEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case TEXT:
                        if (!this.photoView.getStickers().isEmpty()) {
                            this.photoView.getStickers().clear();
                            this.photoView.setHandlingSticker(null);
                        }
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutAddText);
                        this.constraintLayoutSaveText.setVisibility(View.GONE);
                        this.photoView.setHandlingSticker(null);
                        this.photoView.setLocked(true);
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case ADJUST:
                        this.photoEditor.setFilterEffect("");
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutAdjust);
                        setGuideLine();
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case FILTER:
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutFilter);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        setGuideLine();
                        setVisibleSave();
                        this.photoEditor.setFilterEffect("");
                        this.imageViewCompareFilter.setVisibility(View.GONE);
                        this.listFilter.clear();
                        if (this.recyclerViewFilter.getAdapter() != null) {
                            this.recyclerViewFilter.getAdapter().notifyDataSetChanged();
                        }
                        this.moduleToolsId = Module.NONE;
                        return;
                    case STICKER:
                        if (this.photoView.getStickers().size() <= 0) {
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                            viewSlideUp(recyclerViewTools);
                            viewSlideDown(constraintLayoutSticker);
                            this.imageViewAddSticker.setVisibility(View.GONE);
                            this.photoView.setHandlingSticker(null);
                            this.photoView.setLocked(true);
                            this.moduleToolsId = Module.NONE;
                        } else if (this.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                            this.photoView.getStickers().clear();
                            this.imageViewAddSticker.setVisibility(View.GONE);
                            viewSlideUp(recyclerViewTools);
                            viewSlideDown(constraintLayoutSticker);
                            this.photoView.setHandlingSticker(null);
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                            this.moduleToolsId = Module.NONE;
                        } else {
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
                            this.imageViewAddSticker.setVisibility(View.VISIBLE);
                        }
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.moduleToolsId = Module.NONE;
                        setVisibleSave();
                        this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                        setGuideLine();
                        return;
                    case OVERLAY:
                        this.photoEditor.setFilterEffect("");
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutOverlay);
                        this.imageViewCompareOverlay.setVisibility(View.GONE);
                        PhotoEditorActivity.this.constraintLayoutSaveOverlay.setVisibility(View.GONE);
                        PhotoEditorActivity.this.constraintLayoutConfirmCompareOverlay.setVisibility(View.GONE);
                        this.listOverlayEffect.clear();
                        if (this.recycler_view_overlay_effect.getAdapter() != null) {
                            this.recycler_view_overlay_effect.getAdapter().notifyDataSetChanged();
                        }
                        setGuideLine();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case SPLASH_DRAW:
                    case SKETCH_DRAW:
                    case BLUR:
                    case SPLASH_BG:
                    case SKETCH_SQ:
                    case SKETCH_BG:
                    case SPLASH_SQ:
                    case BLUR_BG:
                    case MOSAIC:
                    case COLORED:
                    case CROP:
                    case MIRROR:
                    case WOMEN:
                    case NONE:
                    case HSL:
                    case ART:
                    case PIX:
                    case DRIP:
                    case BLURE:
                    case SPLASH:
                    case WINGS:
                    case MOTION:
                        setOnBackPressDialog();
                        return;
                    default:
                        super.onBackPressed();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAdjustSelected(AdjustAdapter.AdjustModel adjustModel) {
        this.adjustSeekBar.setCurrentDegrees(((int) ((adjustModel.originValue - adjustModel.minValue) / ((adjustModel.maxValue - ((adjustModel.maxValue + adjustModel.minValue) / 2.0f)) / 50.0f))) - 50);
    }

    public void addSticker(int item, Bitmap bitmap) {
        this.photoView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
        this.imageViewAddSticker.setVisibility(View.VISIBLE);
    }

    public void finishCrop(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    public void onColorChanged(String string) {
        this.photoEditor.setBrushColor(Color.parseColor(string));
    }

    public void ratioSavedBitmap(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    @Override
    public void onSaveFilter(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveSplashBackground(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveBlurBackground(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveSketchBackground(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveSketch(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveMosaic(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onMagicChanged(DrawModel drawBitmapModel) {
        this.photoEditor.setBrushMagic(drawBitmapModel);
    }

    @Override
    public void onSaveSplash(Bitmap bitmap) {
        this.photoView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onFilterSelected(int itemCurrent, String string) {
        this.photoEditor.setFilterEffect(string);
        this.adjustFilter.setCurrentDegrees(50);
        if (this.moduleToolsId == Module.FILTER) {
            this.photoView.getGLSurfaceView().setFilterIntensity(0.5f);
        }
    }


    public void onOverlaySelected(int itemCurrent, String string) {
        this.photoEditor.setFilterEffect(string);
        this.seekBarOverlay.setProgress(50);
        if (this.moduleToolsId == Module.OVERLAY) {
            this.photoView.getGLSurfaceView().setFilterIntensity(0.5f);
        }
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class openFilters extends AsyncTask<Void, Void, Void> {
        openFilters() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PhotoEditorActivity.this.listFilter.clear();
            PhotoEditorActivity.this.listFilter.addAll(FilterFileAsset.getListBitmapFilter(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "allFilters " + PhotoEditorActivity.this.listFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            PhotoEditorActivity.this.recyclerViewFilter.setAdapter(new FilterAdapter(PhotoEditorActivity.this.listFilter, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(FilterFileAsset.FILTERS)));
            PhotoEditorActivity.this.constraintLayoutSave.setVisibility(View.GONE);
            PhotoEditorActivity.this.adjustFilter.setCurrentDegrees(50);
            PhotoEditorActivity.this.showLoading(false);
            viewSlideDown(recyclerViewTools);
            viewSlideUp(constraintLayoutFilter);
            viewSlideUp(imageViewCompareFilter);
            setGuideLinePaint();
        }
    }

    class openBlurFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openBlurFragment() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.showLoading(false);
            RatioFragment.show(PhotoEditorActivity.this, PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap(), bitmap);
        }
    }

    class openMirrorFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openMirrorFragment() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.showLoading(false);
            PhotoEditorActivity queShotEditorActivity = PhotoEditorActivity.this;
            MirrorFragment.show(queShotEditorActivity, queShotEditorActivity, queShotEditorActivity.photoView.getCurrentBitmap(), bitmap);
        }
    }

    class dripEffect extends AsyncTask<Void, Void, Void> {
        dripEffect() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PhotoEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PhotoEditorActivity.this, (Bitmap) null);
            DripLayout.setFaceBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap());
            Intent dripIntent = new Intent(PhotoEditorActivity.this, DripLayout.class);
            startActivityForResult(dripIntent, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class motionEffect extends AsyncTask<Void, Void, Void> {
        motionEffect() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PhotoEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PhotoEditorActivity.this, (Bitmap) null);
            MotionLayout.setFaceBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap());
            Intent motionIntent = new Intent(PhotoEditorActivity.this, MotionLayout.class);
            startActivityForResult(motionIntent, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class wingEffect extends AsyncTask<Void, Void, Void> {
        wingEffect() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PhotoEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PhotoEditorActivity.this, (Bitmap) null);
            WingLayout.setFaceBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap());
            Intent neonIntent2 = new Intent(PhotoEditorActivity.this, WingLayout.class);
            startActivityForResult(neonIntent2, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class neonEffect extends AsyncTask<Void, Void, Void> {
        neonEffect() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PhotoEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PhotoEditorActivity.this, (Bitmap) null);
            NeonLayout.setFaceBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap());
            Intent neonIntent = new Intent(PhotoEditorActivity.this, NeonLayout.class);
            startActivityForResult(neonIntent, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class artEffect extends AsyncTask<Void, Void, Void> {
        artEffect() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PhotoEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PhotoEditorActivity.this, (Bitmap) null);
            ArtLayout.setFaceBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap());
            Intent art = new Intent(PhotoEditorActivity.this, ArtLayout.class);
            startActivityForResult(art, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class pixEffect extends AsyncTask<Void, Void, Void> {
        pixEffect() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PhotoEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PhotoEditorActivity.this, (Bitmap) null);
            PixLabLayout.setFaceBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap());
            Intent dripIntent = new Intent(PhotoEditorActivity.this, PixLabLayout.class);
            startActivityForResult(dripIntent, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class openFrameFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openFrameFragment() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.showLoading(false);
            FrameFragment.show(PhotoEditorActivity.this, PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap(), bitmap);
        }
    }

    class openShapeFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openShapeFragment() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            PhotoEditorActivity.this.showLoading(false);
            MosaicFragment.show(PhotoEditorActivity.this, list.get(0), list.get(1), PhotoEditorActivity.this);
        }
    }

    class openColoredFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openColoredFragment() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            PhotoEditorActivity.this.showLoading(false);
            ColoredFragment.show(PhotoEditorActivity.this, list.get(0), list.get(1), PhotoEditorActivity.this);
        }
    }

    class effectDodge extends AsyncTask<Void, Void, Void> {
        effectDodge() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PhotoEditorActivity.this.listDodgeEffect.clear();
            PhotoEditorActivity.this.listDodgeEffect.addAll(OverlayFileAsset.getListBitmapDodgeEffect(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PhotoEditorActivity.this.recycler_view_dodge_effect.setAdapter(new OverlayAdapter(PhotoEditorActivity.this.listDodgeEffect, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.DODGE_EFFECTS)));
            PhotoEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectColor extends AsyncTask<Void, Void, Void> {
        effectColor() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PhotoEditorActivity.this.listColorEffect.clear();
            PhotoEditorActivity.this.listColorEffect.addAll(OverlayFileAsset.getListBitmapColorEffect(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PhotoEditorActivity.this.recycler_view_color_effect.setAdapter(new OverlayAdapter(PhotoEditorActivity.this.listColorEffect, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.COLOR_EFFECTS)));
            PhotoEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectDivide extends AsyncTask<Void, Void, Void> {
        effectDivide() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PhotoEditorActivity.this.listHueEffect.clear();
            PhotoEditorActivity.this.listHueEffect.addAll(OverlayFileAsset.getListBitmapHueEffect(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PhotoEditorActivity.this.recycler_view_hue_effect.setAdapter(new OverlayAdapter(PhotoEditorActivity.this.listHueEffect, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.HUE_EFFECTS)));
            PhotoEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectHardmix extends AsyncTask<Void, Void, Void> {
        effectHardmix() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PhotoEditorActivity.this.listHardMixEffect.clear();
            PhotoEditorActivity.this.listHardMixEffect.addAll(OverlayFileAsset.getListBitmapHardmixEffect(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PhotoEditorActivity.this.recycler_view_hardmix_effet.setAdapter(new OverlayAdapter(PhotoEditorActivity.this.listHardMixEffect, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.HARDMIX_EFFECTS)));
            PhotoEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectOvarlay extends AsyncTask<Void, Void, Void> {
        effectOvarlay() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PhotoEditorActivity.this.listOverlayEffect.clear();
            PhotoEditorActivity.this.listOverlayEffect.addAll(OverlayFileAsset.getListBitmapOverlayEffect(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PhotoEditorActivity.this.recycler_view_overlay_effect.setAdapter(new OverlayAdapter(PhotoEditorActivity.this.listOverlayEffect, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.OVERLAY_EFFECTS)));
            PhotoEditorActivity.this.imageViewCompareOverlay.setVisibility(View.VISIBLE);
            PhotoEditorActivity.this.constraintLayoutSave.setVisibility(View.GONE);
            PhotoEditorActivity.this.seekBarOverlay.setProgress(100);
            PhotoEditorActivity.this.showLoading(false);
            viewSlideDown(recyclerViewTools);
            viewSlideDown(constraintLayoutEffects);
            viewSlideUp(constraintLayoutConfirmCompareOverlay);
            viewSlideUp(constraintLayoutOverlay);
            setGuideLinePaint();
        }
    }

    class effectBurn extends AsyncTask<Void, Void, Void> {
        effectBurn() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PhotoEditorActivity.this.listBurnEffect.clear();
            PhotoEditorActivity.this.listBurnEffect.addAll(OverlayFileAsset.getListBitmapBurnEffect(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photoView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PhotoEditorActivity.this.recycler_view_burn_effect.setAdapter(new OverlayAdapter(PhotoEditorActivity.this.listBurnEffect, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.BURN_EFFECTS)));
            PhotoEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class openSplashSquareBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSplashSquareBackgroundFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PhotoEditorActivity.this.photoView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PhotoEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SaturationSquareBackgroundFragment.show(PhotoEditorActivity.this, list.get(0), null, list.get(1), PhotoEditorActivity.this, true);
            }
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class openBlurSquareBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openBlurSquareBackgroundFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PhotoEditorActivity.this.photoView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlurImageFromBitmap(currentBitmap, 2.5f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PhotoEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                BlurSquareBgFragment.show(PhotoEditorActivity.this, list.get(0), null, list.get(1), PhotoEditorActivity.this, true);
            }
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class openSplashFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSplashFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PhotoEditorActivity.this.photoView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PhotoEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SaturationSquareFragment.show(PhotoEditorActivity.this, list.get(0), null, list.get(1), PhotoEditorActivity.this, true);
            }
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class openSketchFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSketchFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PhotoEditorActivity.this.photoView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getSketchImageFromBitmap(currentBitmap, 0.8f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PhotoEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SketchSquareFragment.show(PhotoEditorActivity.this, list.get(0), null, list.get(1), PhotoEditorActivity.this, true);
            }
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class openSketchBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSketchBackgroundSquared;

        public openSketchBackgroundFragment(boolean z) {
            this.isSketchBackgroundSquared = z;
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PhotoEditorActivity.this.photoView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSketchBackgroundSquared) {
                arrayList.add(FilterUtils.getSketchImageFromBitmap(currentBitmap, 0.8f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSketchBackgroundSquared) {
                PhotoEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SketchSquareBackgroundFragment.show(PhotoEditorActivity.this, list.get(0), null, list.get(1), PhotoEditorActivity.this, true);
            }
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class SaveFilter extends AsyncTask<Void, Void, Bitmap> {
        SaveFilter() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            final Bitmap[] bitmaps = {null};
            PhotoEditorActivity.this.photoView.saveGLSurfaceViewAsBitmap(bitmap -> bitmaps[0] = bitmap);
            while (bitmaps[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return bitmaps[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.photoView.setImageSource(bitmap);
            PhotoEditorActivity.this.photoView.setFilterEffect("");
            PhotoEditorActivity.this.showLoading(false);
        }
    }

    class SaveSticker extends AsyncTask<Void, Void, Bitmap> {
        SaveSticker() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.photoView.getGLSurfaceView().setAlpha(0.0f);
            PhotoEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            final Bitmap[] bitmaps = {null};
            while (bitmaps[0] == null) {
                try {
                    PhotoEditorActivity.this.photoEditor.saveStickerAsBitmap(bitmap -> bitmaps[0] = bitmap);
                    while (bitmaps[0] == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                }
            }
            return bitmaps[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.photoView.setImageSource(bitmap);
            PhotoEditorActivity.this.photoView.getStickers().clear();
            PhotoEditorActivity.this.photoView.getGLSurfaceView().setAlpha(1.0f);
            PhotoEditorActivity.this.showLoading(false);
            PhotoEditorActivity.this.reloadingLayout();
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 123) {
            if (i2 == -1) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    float width = (float) bitmap.getWidth();
                    float height = (float) bitmap.getHeight();
                    float max = Math.max(width / 1280.0f, height / 1280.0f);
                    if (max > 1.0f) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                    }
                    if (SystemUtil.rotateBitmap(bitmap, new ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) != bitmap) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    this.photoView.setImageSource(bitmap);
                    reloadingLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                    MsgUtil.toastMsg(this, "Error: Can not open image");
                }
            } else {
                finish();
            }
        } else if (i == 900) {
            if (intent != null && intent.getStringExtra("MESSAGE").equals("done")) {
                if (BitmapTransfer.bitmap != null) {

                    new loadBitmap().execute(BitmapTransfer.bitmap);
                }
            }

        }
    }

    class loadBitmap extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        loadBitmap() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmaps) {
            try {
                Bitmap bitmap = bitmaps[0];//MediaStore.Images.Media.getBitmap(QueShotEditorActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
//                Bitmap bitmap1 = SystemUtil.rotateBitmap(bitmap, new ExifInterface(QueShotEditorActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
//                if (bitmap1 != bitmap) {
//                    bitmap.recycle();
//                }
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.photoView.setImageSource(bitmap);
            PhotoEditorActivity.this.reloadingLayout();
        }
    }

    class loadBitmapUri extends AsyncTask<String, Bitmap, Bitmap> {
        loadBitmapUri() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(String... string) {
            try {
                File file = new File(string[0]);
                Uri fromFile = Uri.fromFile(file);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(PhotoEditorActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                Bitmap bitmap1 = SystemUtil.rotateBitmap(bitmap, new ExifInterface(PhotoEditorActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                if (bitmap1 != bitmap) {
                    bitmap.recycle();
                }

                return bitmap1;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.photoView.setImageSource(bitmap);
            PhotoEditorActivity.this.reloadingLayout();
        }
    }


    public void reloadingLayout() {
        this.photoView.postDelayed(() -> {
            try {
                Display display = PhotoEditorActivity.this.getWindowManager().getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int i = point.x;
                int height = PhotoEditorActivity.this.relativeLayoutWrapper.getHeight();
                int i2 = PhotoEditorActivity.this.photoView.getGLSurfaceView().getRenderViewport().width;
                float f = (float) PhotoEditorActivity.this.photoView.getGLSurfaceView().getRenderViewport().height;
                float f2 = (float) i2;
                if (((int) ((((float) i) * f) / f2)) <= height) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
                    params.addRule(13);
                    PhotoEditorActivity.this.photoView.setLayoutParams(params);
                    PhotoEditorActivity.this.photoView.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) ((((float) height) * f2) / f), -1);
                    params.addRule(13);
                    PhotoEditorActivity.this.photoView.setLayoutParams(params);
                    PhotoEditorActivity.this.photoView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PhotoEditorActivity.this.showLoading(false);
        }, 300);
    }

    class SaveEditingBitmap extends AsyncTask<Void, String, String> {
        SaveEditingBitmap() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.showLoading(true);
        }

        public String doInBackground(Void... voids) {
            try {
                String fileName = "PhotoEditor_"+ String.format("%d.jpg", System.currentTimeMillis());
                return SaveFileUtils.saveBitmapFileEditor(PhotoEditorActivity.this, PhotoEditorActivity.this.photoView.getCurrentBitmap(),fileName, null).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
            PhotoEditorActivity.this.showLoading(false);
            if (string == null) {
                Toast.makeText(PhotoEditorActivity.this.getApplicationContext(), "Oop! Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(PhotoEditorActivity.this, PhotoShareActivity.class);
            intent.putExtra("path", string);
            intent.putExtra("activity","PhotoEditorActivity");
            PhotoEditorActivity.this.startActivity(intent);
        }

    }

    public void showLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            this.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        this.relativeLayoutLoading.setVisibility(View.GONE);
    }


    private void SaveView() {
        if (PermissionsUtils.checkWriteStoragePermission(PhotoEditorActivity.this)) {
            new SaveEditingBitmap().execute();
        }
    }

}
