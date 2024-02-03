package com.rohitneel.photopixelpro.photocollage.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.adapters.FontAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.ShadowAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.TextBackgroundAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.TextColorAdapter;
import com.rohitneel.photopixelpro.photocollage.assets.FontFileAsset;
import com.rohitneel.photopixelpro.photocollage.picker.PhotoCarouselPicker;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoEditText;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoText;
import com.rohitneel.photopixelpro.photocollage.preference.Preference;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class TextFragment extends DialogFragment implements View.OnClickListener, FontAdapter.ItemClickListener,
        ShadowAdapter.ShadowItemClickListener, TextColorAdapter.ColorListener, TextBackgroundAdapter.BackgroundColorListener {
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String TAG = "TextFragment";
    LinearLayout linear_layout_edit_text_tools;

    public PhotoText photoText;
    ImageView image_view_text_texture;
    SeekBar seekbar_radius;
    SeekBar seekbar_height;
    SeekBar seekbar_background_opacity;
    SeekBar seekbar_width;
    ImageView image_view_color;
    TextView textViewFont;
    TextView textViewShadow;
    ScrollView scroll_view_change_color_layout;
    ImageView image_view_fonts;
    ImageView image_view_adjust;
    ScrollView scroll_view_change_font_adjust;
    LinearLayout scroll_view_change_font_layout;
    ImageView image_view_align_left;
    ImageView image_view_align_center;
    ImageView image_view_align_right;
    public RecyclerView recycler_view_color;
    public RecyclerView recycler_view_background;
    public List<PhotoCarouselPicker.PickerItem> colorItems;
    private FontAdapter fontAdapter;
    private ShadowAdapter shadowAdapter;
    View view_highlight_texture;
    LinearLayout linear_layout_preview;
    RecyclerView recycler_view_fonts;
    RecyclerView recycler_view_shadow;
    PhotoEditText add_text_edit_text;
    private InputMethodManager inputMethodManager;
    TextView text_view_preview_effect;
    TextView textViewSeekBarSize;
    TextView textViewSeekBarColor;
    TextView textViewSeekBarBackground;
    TextView textViewSeekBarRadius;
    TextView textViewSeekBarWith;
    TextView textViewSeekBarHeight;
    ImageView image_view_save_change;
    ImageView image_view_keyboard;
    CheckBox checkbox_background;
    private TextEditor textEditor;
    private List<ImageView> textFunctions;
    SeekBar seekbar_text_size;
    public List<PhotoCarouselPicker.PickerItem> textTextureItems;
    PhotoCarouselPicker texture_carousel_picker;
    SeekBar textColorOpacity;

    public interface TextEditor {
        void onBackButton();
        void onDone(PhotoText photoText);
    }

    public void onItemClick(View view, int i) {
        FontFileAsset.setFontByName(requireContext(), this.text_view_preview_effect, FontFileAsset.getListFonts().get(i));
        this.photoText.setFontName(FontFileAsset.getListFonts().get(i));
        this.photoText.setFontIndex(i);
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity, @NonNull String str, @ColorInt int i) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INPUT_TEXT, str);
        bundle.putInt(EXTRA_COLOR_CODE, i);
        TextFragment textFragment = new TextFragment();
        textFragment.setArguments(bundle);
        textFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return textFragment;
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity, PhotoText addTextProperties) {
        TextFragment addTextFragment = new TextFragment();
        addTextFragment.setPhotoText(addTextProperties);
        addTextFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return addTextFragment;
    }

    public void onShadowItemClick(View view, int i) {
        PhotoText.TextShadow textShadow = PhotoText.getLstTextShadow().get(i);
        this.text_view_preview_effect.setShadowLayer((float) textShadow.getRadius(), (float) textShadow.getDx(), (float) textShadow.getDy(), textShadow.getColorShadow());
        this.text_view_preview_effect.invalidate();
        this.photoText.setTextShadow(textShadow);
        this.photoText.setTextShadowIndex(i);
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "Test", ContextCompat.getColor(appCompatActivity, R.color.black));
    }

    public void setPhotoText(PhotoText photoText) {
        this.photoText = photoText;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        return layoutInflater.inflate(R.layout.fragment_add_text, viewGroup, false);
    }

    public void dismissAndShowSticker() {
        if (this.textEditor != null) {
            this.textEditor.onBackButton();
        }
        dismiss();
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(view);
        if (this.photoText == null) {
            this.photoText = PhotoText.getDefaultProperties();
        }
        this.add_text_edit_text.setTextFragment(this);
        initAddTextLayout();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        this.inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setDefaultStyleForEdittext();
        this.inputMethodManager.toggleSoftInput(2, 0);
        highlightFunction(this.image_view_keyboard);
        this.recycler_view_fonts.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.fontAdapter = new FontAdapter(getContext(), FontFileAsset.getListFonts());
        this.fontAdapter.setClickListener(this);
        this.recycler_view_fonts.setAdapter(this.fontAdapter);
        this.recycler_view_shadow.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.shadowAdapter = new ShadowAdapter(getContext(), PhotoText.getLstTextShadow());
        this.shadowAdapter.setClickListener(this);
        this.recycler_view_shadow.setAdapter(this.shadowAdapter);
        this.texture_carousel_picker.setAdapter(new PhotoCarouselPicker.CarouselViewAdapter(getContext(), this.textTextureItems, 0));
        this.texture_carousel_picker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (f > 0.0f) {
                    if (TextFragment.this.image_view_text_texture.getVisibility() == View.INVISIBLE) {
                        TextFragment.this.image_view_text_texture.setVisibility(View.VISIBLE);
                        TextFragment.this.view_highlight_texture.setVisibility(View.VISIBLE);
                    }
                    float f2 = ((float) i) + f;
                    BitmapShader bitmapShader = new BitmapShader((TextFragment.this.textTextureItems.get(Math.round(f2))).getBitmap(), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                    TextFragment.this.text_view_preview_effect.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    TextFragment.this.text_view_preview_effect.getPaint().setShader(bitmapShader);
                    TextFragment.this.photoText.setTextShader(bitmapShader);
                    TextFragment.this.photoText.setTextShaderIndex(Math.round(f2));
                }
            }
        });

        this.textColorOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarColor.setText(value);
                int i2 = 255 - i;
                TextFragment.this.photoText.setTextAlpha(i2);
                TextFragment.this.text_view_preview_effect.setTextColor(Color.argb(i2, Color.red(TextFragment.this.photoText.getTextColor()), Color.green(TextFragment.this.photoText.getTextColor()), Color.blue(TextFragment.this.photoText.getTextColor())));
            }
        });
        this.add_text_edit_text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                TextFragment.this.text_view_preview_effect.setText(charSequence.toString());
                TextFragment.this.photoText.setText(charSequence.toString());
            }
        });
        this.checkbox_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    TextFragment.this.photoText.setShowBackground(false);
                    TextFragment.this.text_view_preview_effect.setBackgroundResource(0);
                    TextFragment.this.text_view_preview_effect.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                } else if (TextFragment.this.checkbox_background.isPressed() || TextFragment.this.photoText.isShowBackground()) {
                    TextFragment.this.photoText.setShowBackground(true);
                    TextFragment.this.initPreviewText();
                } else {
                    TextFragment.this.checkbox_background.setChecked(false);
                    TextFragment.this.photoText.setShowBackground(false);
                    TextFragment.this.initPreviewText();
                }
            }
        });
        this.seekbar_width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarWith.setText(value);
                TextFragment.this.text_view_preview_effect.setPadding(SystemUtil.dpToPx(TextFragment.this.requireContext(), i), TextFragment.this.text_view_preview_effect.getPaddingTop(), SystemUtil.dpToPx(TextFragment.this.getContext(), i), TextFragment.this.text_view_preview_effect.getPaddingBottom());
                TextFragment.this.photoText.setPaddingWidth(i);
            }
        });
        this.seekbar_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarHeight.setText(value);
                TextFragment.this.text_view_preview_effect.setPadding(TextFragment.this.text_view_preview_effect.getPaddingLeft(), SystemUtil.dpToPx(TextFragment.this.requireContext(), i), TextFragment.this.text_view_preview_effect.getPaddingRight(), SystemUtil.dpToPx(TextFragment.this.getContext(), i));
                TextFragment.this.photoText.setPaddingHeight(i);
            }
        });
        this.seekbar_background_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarBackground.setText(value);
                TextFragment.this.photoText.setBackgroundAlpha(255 - i);
                    int red = Color.red(TextFragment.this.photoText.getBackgroundColor());
                    int green = Color.green(TextFragment.this.photoText.getBackgroundColor());
                    int blue = Color.blue(TextFragment.this.photoText.getBackgroundColor());
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.argb(TextFragment.this.photoText.getBackgroundAlpha(), red, green, blue));
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.photoText.getBackgroundBorder()));
                    TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

            }
        });
        this.seekbar_text_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarSize.setText(value);
                int i2 = 15;
                if (i >= 15) {
                    i2 = i;
                }
                TextFragment.this.text_view_preview_effect.setTextSize((float) i2);
                TextFragment.this.photoText.setTextSize(i2);
            }
        });
        this.seekbar_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarRadius.setText(value);
                TextFragment.this.photoText.setBackgroundBorder(i);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), i));
                    gradientDrawable.setColor(Color.argb(TextFragment.this.photoText.getBackgroundAlpha(), Color.red(TextFragment.this.photoText.getBackgroundColor()), Color.green(TextFragment.this.photoText.getBackgroundColor()), Color.blue(TextFragment.this.photoText.getBackgroundColor())));
                    TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

            }
        });
        if (Preference.getKeyboard(requireContext()) > 0) {
            updateAddTextBottomToolbarHeight(Preference.getKeyboard(getContext()));
        }
        initPreviewText();
    }

    public void initView(View view) {
        this.add_text_edit_text = view.findViewById(R.id.add_text_edit_text);
        this.image_view_keyboard = view.findViewById(R.id.image_view_keyboard);
        this.image_view_fonts = view.findViewById(R.id.image_view_fonts);
        this.image_view_color = view.findViewById(R.id.image_view_color);
        this.image_view_align_left = view.findViewById(R.id.imageViewAlignLeft);
        this.image_view_align_center = view.findViewById(R.id.imageViewAlignCenter);
        this.image_view_align_right = view.findViewById(R.id.imageViewAlignRight);
        this.textViewFont = view.findViewById(R.id.textViewFont);
        this.textViewShadow = view.findViewById(R.id.textViewShadow);
        this.textViewSeekBarSize = view.findViewById(R.id.seekbarSize);
        this.textViewSeekBarColor = view.findViewById(R.id.seekbarColor);
        this.textViewSeekBarBackground = view.findViewById(R.id.seekbarBackground);
        this.textViewSeekBarRadius = view.findViewById(R.id.seekbarRadius);
        this.textViewSeekBarWith = view.findViewById(R.id.seekbarWith);
        this.textViewSeekBarHeight = view.findViewById(R.id.seekbarHeight);
        this.image_view_adjust = view.findViewById(R.id.image_view_adjust);
        this.image_view_save_change = view.findViewById(R.id.image_view_save_change);
        this.scroll_view_change_font_layout = view.findViewById(R.id.scroll_view_change_font_layout);
        this.scroll_view_change_font_adjust = view.findViewById(R.id.scroll_view_change_color_adjust);
        this.linear_layout_edit_text_tools = view.findViewById(R.id.linear_layout_edit_text_tools);
        this.recycler_view_fonts = view.findViewById(R.id.recycler_view_fonts);
        this.recycler_view_shadow = view.findViewById(R.id.recycler_view_shadow);
        this.scroll_view_change_color_layout = view.findViewById(R.id.scroll_view_change_color_layout);
        this.texture_carousel_picker = view.findViewById(R.id.texture_carousel_picker);
        this.image_view_text_texture = view.findViewById(R.id.image_view_text_texture);
        this.view_highlight_texture = view.findViewById(R.id.view_highlight_texture);
        this.textColorOpacity = view.findViewById(R.id.seekbar_text_opacity);
        this.text_view_preview_effect = view.findViewById(R.id.text_view_preview_effect);
        this.linear_layout_preview = view.findViewById(R.id.linear_layout_preview);
        this.checkbox_background = view.findViewById(R.id.checkbox_background);
        this.seekbar_width = view.findViewById(R.id.seekbar_width);
        this.seekbar_height = view.findViewById(R.id.seekbar_height);
        this.seekbar_background_opacity = view.findViewById(R.id.seekbar_background_opacity);
        this.seekbar_text_size = view.findViewById(R.id.seekbar_text_size);
        this.seekbar_radius = view.findViewById(R.id.seekbar_radius);

        this.recycler_view_color = view.findViewById(R.id.recyclerViewColor);
        this.recycler_view_color.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_color.setAdapter(new TextColorAdapter(getContext(), this));
        this.recycler_view_color.setVisibility(View.VISIBLE);

        this.recycler_view_background = view.findViewById(R.id.recyclerViewBackground);
        this.recycler_view_background.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_background.setAdapter(new TextBackgroundAdapter(getContext(), this));
        this.recycler_view_background.setVisibility(View.VISIBLE);
    }

    public void initPreviewText() {
        if (this.photoText.isShowBackground()) {
            if (this.photoText.getBackgroundColor() != 0) {
                this.text_view_preview_effect.setBackgroundColor(this.photoText.getBackgroundColor());
            }
            if (this.photoText.getBackgroundAlpha() < 255) {
                this.text_view_preview_effect.setBackgroundColor(Color.argb(this.photoText.getBackgroundAlpha(), Color.red(this.photoText.getBackgroundColor()), Color.green(this.photoText.getBackgroundColor()), Color.blue(this.photoText.getBackgroundColor())));
            }
            if (this.photoText.getBackgroundBorder() > 0) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(requireContext(), this.photoText.getBackgroundBorder()));
                gradientDrawable.setColor(Color.argb(this.photoText.getBackgroundAlpha(), Color.red(this.photoText.getBackgroundColor()), Color.green(this.photoText.getBackgroundColor()), Color.blue(this.photoText.getBackgroundColor())));
                this.text_view_preview_effect.setBackground(gradientDrawable);
            }
        }
        if (this.photoText.getPaddingHeight() > 0) {
            this.text_view_preview_effect.setPadding(this.text_view_preview_effect.getPaddingLeft(), this.photoText.getPaddingHeight(), this.text_view_preview_effect.getPaddingRight(), this.photoText.getPaddingHeight());
            this.seekbar_height.setProgress(this.photoText.getPaddingHeight());
        }
        if (this.photoText.getPaddingWidth() > 0) {
            this.text_view_preview_effect.setPadding(this.photoText.getPaddingWidth(), this.text_view_preview_effect.getPaddingTop(), this.photoText.getPaddingWidth(), this.text_view_preview_effect.getPaddingBottom());
            this.seekbar_width.setProgress(this.photoText.getPaddingWidth());
        }
        if (this.photoText.getText() != null) {
            this.text_view_preview_effect.setText(this.photoText.getText());
            this.add_text_edit_text.setText(this.photoText.getText());
        }
        if (this.photoText.getTextShader() != null) {
            this.text_view_preview_effect.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            this.text_view_preview_effect.getPaint().setShader(this.photoText.getTextShader());
        }
        if (this.photoText.getTextAlign() == 4) {
            this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center_select));
        } else if (this.photoText.getTextAlign() == 3) {
            this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
        } else if (this.photoText.getTextAlign() == 2) {
            this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
        }
        this.text_view_preview_effect.setPadding(SystemUtil.dpToPx(getContext(), this.photoText.getPaddingWidth()), this.text_view_preview_effect.getPaddingTop(), SystemUtil.dpToPx(getContext(), this.photoText.getPaddingWidth()), this.text_view_preview_effect.getPaddingBottom());
        this.text_view_preview_effect.setTextColor(this.photoText.getTextColor());
        this.text_view_preview_effect.setTextAlignment(this.photoText.getTextAlign());
        this.text_view_preview_effect.setTextSize((float) this.photoText.getTextSize());
        FontFileAsset.setFontByName(getContext(), this.text_view_preview_effect, this.photoText.getFontName());
        this.text_view_preview_effect.invalidate();
    }

    private void setDefaultStyleForEdittext() {
        this.add_text_edit_text.requestFocus();
        this.add_text_edit_text.setTextSize(20.0f);
        this.add_text_edit_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.add_text_edit_text.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void initAddTextLayout() {
        this.textFunctions = getTextFunctions();
        this.image_view_keyboard.setOnClickListener(this);
        this.image_view_fonts.setOnClickListener(this);
        this.textViewFont.setOnClickListener(this);
        this.textViewShadow.setOnClickListener(this);
        this.image_view_adjust.setOnClickListener(this);
        this.image_view_color.setOnClickListener(this);
        this.image_view_save_change.setOnClickListener(this);
        this.image_view_align_left.setOnClickListener(this);
        this.image_view_align_center.setOnClickListener(this);
        this.image_view_align_right.setOnClickListener(this);
        this.scroll_view_change_font_layout.setVisibility(View.GONE);
        this.scroll_view_change_font_adjust.setVisibility(View.GONE);
        this.scroll_view_change_color_layout.setVisibility(View.GONE);
        this.image_view_text_texture.setVisibility(View.INVISIBLE);
        this.view_highlight_texture.setVisibility(View.GONE);
        this.seekbar_width.setProgress(this.photoText.getPaddingWidth());
        this.colorItems = getColorItems();
        this.textTextureItems = getTextTextures();
    }

    @Override
    public void onColorSelected(TextColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            TextFragment.this.text_view_preview_effect.setTextColor(squareView.drawableId);
            TextFragment.this.photoText.setTextColor(squareView.drawableId);
            TextFragment.this.text_view_preview_effect.getPaint().setShader(null);
            TextFragment.this.photoText.setTextShader(null);
        }  else {
            TextFragment.this.text_view_preview_effect.setTextColor(squareView.drawableId);
            TextFragment.this.photoText.setTextColor(squareView.drawableId);
            TextFragment.this.text_view_preview_effect.getPaint().setShader(null);
            TextFragment.this.photoText.setTextShader(null);
        }
    }

    @Override
    public void onBackgroundColorSelected(TextBackgroundAdapter.SquareView squareView) {
        if (squareView.isColor) {
            TextFragment.this.text_view_preview_effect.setBackgroundColor(squareView.drawableId);
            TextFragment.this.photoText.setBackgroundColor(squareView.drawableId);
            TextFragment.this.seekbar_radius.setEnabled(true);
            TextFragment.this.photoText.setShowBackground(true);
            if (!TextFragment.this.checkbox_background.isChecked()) {
                TextFragment.this.checkbox_background.setChecked(true);
            }
            int red = Color.red(TextFragment.this.photoText.getBackgroundColor());
            int green = Color.green(TextFragment.this.photoText.getBackgroundColor());
            int blue = Color.blue(TextFragment.this.photoText.getBackgroundColor());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.argb(TextFragment.this.photoText.getBackgroundAlpha(), red, green, blue));
            gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.photoText.getBackgroundBorder()));
            TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

        }  else {
            TextFragment.this.text_view_preview_effect.setBackgroundColor(squareView.drawableId);
            TextFragment.this.photoText.setBackgroundColor(squareView.drawableId);
            TextFragment.this.seekbar_radius.setEnabled(true);
            TextFragment.this.photoText.setShowBackground(true);
            if (!TextFragment.this.checkbox_background.isChecked()) {
                TextFragment.this.checkbox_background.setChecked(true);
            }
            int red = Color.red(TextFragment.this.photoText.getBackgroundColor());
            int green = Color.green(TextFragment.this.photoText.getBackgroundColor());
            int blue = Color.blue(TextFragment.this.photoText.getBackgroundColor());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.argb(TextFragment.this.photoText.getBackgroundAlpha(), red, green, blue));
            gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.photoText.getBackgroundBorder()));
            TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);
        }
    }

    public void onResume() {
        super.onResume();
        ViewCompat.setOnApplyWindowInsetsListener(getDialog().getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return ViewCompat.onApplyWindowInsets(
                        TextFragment.this.getDialog().getWindow().getDecorView(),
                        windowInsetsCompat.inset(windowInsetsCompat.getSystemWindowInsetLeft(), 0, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom()));
            }
        });

    }

    public void updateAddTextBottomToolbarHeight(final int i) {
        new Handler().post(new Runnable() {
            public void run() {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) TextFragment.this.linear_layout_edit_text_tools.getLayoutParams();
                layoutParams.bottomMargin = i;
                TextFragment.this.linear_layout_edit_text_tools.setLayoutParams(layoutParams);
                TextFragment.this.linear_layout_edit_text_tools.invalidate();
                TextFragment.this.scroll_view_change_font_layout.invalidate();
                TextFragment.this.scroll_view_change_font_adjust.invalidate();
                TextFragment.this.scroll_view_change_color_layout.invalidate();
                Log.i("HIHIH", i + "");
            }
        });
    }

    public void setOnTextEditorListener(TextEditor textEditor2) {
        this.textEditor = textEditor2;
    }

    private void highlightFunction(ImageView imageView) {
        for (ImageView next : this.textFunctions) {
            if (next == imageView) {
                imageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.line));
            } else {
                next.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.line_fake));
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewFont:
                this.textViewShadow.setTextColor(getResources().getColor(R.color.white));
                this.textViewFont.setTextColor(getResources().getColor(R.color.mainColor));
                this.recycler_view_fonts.setVisibility(View.VISIBLE);
                this.recycler_view_shadow.setVisibility(View.GONE);
                return;
            case R.id.textViewShadow:
                this.textViewShadow.setTextColor(getResources().getColor(R.color.mainColor));
                this.textViewFont.setTextColor(getResources().getColor(R.color.white));
                this.recycler_view_fonts.setVisibility(View.GONE);
                this.recycler_view_shadow.setVisibility(View.VISIBLE);
                return;
            case R.id.imageViewAlignLeft:
                if (this.photoText.getTextAlign() == 3 || this.photoText.getTextAlign() == 4) {
                    this.photoText.setTextAlign(2);
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left_select));
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
                }
                this.text_view_preview_effect.setTextAlignment(this.photoText.getTextAlign());
                TextView textView = this.text_view_preview_effect;
                textView.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.imageViewAlignCenter:
                if (this.photoText.getTextAlign() == 2 || this.photoText.getTextAlign() == 3) {
                    this.photoText.setTextAlign(4);
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center_select));
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
                }
                this.text_view_preview_effect.setTextAlignment(this.photoText.getTextAlign());
                TextView textViews = this.text_view_preview_effect;
                textViews.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.imageViewAlignRight:
                if (this.photoText.getTextAlign() == 4 || this.photoText.getTextAlign() == 2) {
                    this.photoText.setTextAlign(3);
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right_select));
                }
                this.text_view_preview_effect.setTextAlignment(this.photoText.getTextAlign());
                TextView textViewz = this.text_view_preview_effect;
                textViewz.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.image_view_adjust:
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_color_layout.setVisibility(View.GONE);
                this.scroll_view_change_font_adjust.setVisibility(View.VISIBLE);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.seekbar_background_opacity.setProgress(255 - this.photoText.getBackgroundAlpha());
                this.seekbar_text_size.setProgress(this.photoText.getTextSize());
                this.seekbar_radius.setProgress(this.photoText.getBackgroundBorder());
                this.seekbar_width.setProgress(this.photoText.getPaddingWidth());
                this.seekbar_height.setProgress(this.photoText.getPaddingHeight());
                this.textColorOpacity.setProgress(255 - this.photoText.getTextAlpha());
                toggleTextEditEditable(false);
                highlightFunction(this.image_view_adjust);
                return;
            case R.id.image_view_color:
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_color_layout.setVisibility(View.VISIBLE);
                this.scroll_view_change_font_adjust.setVisibility(View.GONE);
                toggleTextEditEditable(false);
                highlightFunction(this.image_view_color);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.add_text_edit_text.setVisibility(View.GONE);
                this.texture_carousel_picker.setCurrentItem(this.photoText.getTextShaderIndex());
                this.checkbox_background.setChecked(this.photoText.isShowBackground());
                this.checkbox_background.setChecked(this.photoText.isShowBackground());
                if (this.photoText.getTextShader() != null && this.image_view_text_texture.getVisibility() == View.INVISIBLE) {
                    this.image_view_text_texture.setVisibility(View.VISIBLE);
                    this.view_highlight_texture.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            case R.id.image_view_fonts:
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_font_layout.setVisibility(View.VISIBLE);
                this.scroll_view_change_color_layout.setVisibility(View.GONE);
                this.scroll_view_change_font_adjust.setVisibility(View.GONE);
                this.add_text_edit_text.setVisibility(View.GONE);
                toggleTextEditEditable(false);
                highlightFunction(this.image_view_fonts);
                this.shadowAdapter.setSelectedItem(this.photoText.getFontIndex());
                this.fontAdapter.setSelectedItem(this.photoText.getFontIndex());
                return;
            case R.id.image_view_save_change:
                if (this.photoText.getText() == null || this.photoText.getText().length() == 0) {
                    this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    this.textEditor.onBackButton();
                    dismiss();
                    return;
                }
                this.photoText.setTextWidth(this.text_view_preview_effect.getMeasuredWidth());
                this.photoText.setTextHeight(this.text_view_preview_effect.getMeasuredHeight());
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.textEditor.onDone(this.photoText);
                dismiss();
                return;
            case R.id.image_view_keyboard:
                toggleTextEditEditable(true);
                this.add_text_edit_text.setVisibility(View.VISIBLE);
                this.add_text_edit_text.requestFocus();
                highlightFunction(this.image_view_keyboard);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.scroll_view_change_color_layout.setVisibility(View.GONE);
                this.scroll_view_change_font_adjust.setVisibility(View.GONE);
                this.linear_layout_edit_text_tools.invalidate();
                this.inputMethodManager.toggleSoftInput(2, 0);
                return;
            default:
        }
    }

    private void toggleTextEditEditable(boolean z) {
        this.add_text_edit_text.setFocusable(z);
        this.add_text_edit_text.setFocusableInTouchMode(z);
        this.add_text_edit_text.setClickable(z);

    }

    private List<ImageView> getTextFunctions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.image_view_keyboard);
        arrayList.add(this.image_view_fonts);
        arrayList.add(this.image_view_color);
        arrayList.add(this.image_view_adjust);
        arrayList.add(this.image_view_save_change);
        return arrayList;
    }

    public List<PhotoCarouselPicker.PickerItem> getTextTextures() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 42; i++) {
            try {
                AssetManager assets = getContext().getAssets();
                arrayList.add(new PhotoCarouselPicker.DrawableItem(Drawable.createFromStream(assets.open("texture/texture_" + (i + 1) + ".webp"), (String) null)));
            } catch (Exception e) {
            }
        }
        return arrayList;
    }

    public List<PhotoCarouselPicker.PickerItem> getColorItems() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PhotoCarouselPicker.ColorItem("#f44336"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#E91E63"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#EC407A"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#9C27B0"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#673AB7"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#3F51B5"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#2196F3"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#03A9F4"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#00BFA5"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#00BCD4"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#009688"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#4CAF50"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#8BC34A"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#CDDC39"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#FFEB3B"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#FFC107"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#FF9800"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#FF5722"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#795548"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#9E9E9E"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#607D8B"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#FFFFFF"));
        arrayList.add(new PhotoCarouselPicker.ColorItem("#000000"));
        return arrayList;
    }
}
