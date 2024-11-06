package com.rohitneel.photopixelpro.photoframe.activities;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.helper.SessionManager;
import com.rohitneel.photopixelpro.photoframe.filterclass.DataBinder;
import com.rohitneel.photopixelpro.photoframe.filterclass.Filter;
import com.rohitneel.photopixelpro.photoframe.filterclass.FilterFrameAdapter;
import com.rohitneel.photopixelpro.photoframe.adapters.AdapterFontList;
import com.rohitneel.photopixelpro.photoframe.adapters.AdapterStickersList;
import com.rohitneel.photopixelpro.photoframe.adapters.AdepterFrameListPopup;
import com.rohitneel.photopixelpro.photoframe.model.ModelFontDetail;
import com.rohitneel.photopixelpro.photoframe.model.ModelStickers;
import com.rohitneel.photopixelpro.photoframe.model.ModelclassFlipList;
import com.rohitneel.photopixelpro.photoframe.model.ModelclassFrameList;
import com.rohitneel.photopixelpro.photoframe.stickerclasses.DrawableSticker;
import com.rohitneel.photopixelpro.photoframe.stickerclasses.Sticker;
import com.rohitneel.photopixelpro.photoframe.stickerclasses.StickerView;
import com.rohitneel.photopixelpro.photoframe.stickerclasses.TextSticker;
import com.rohitneel.photopixelpro.photoframe.utils.AlertDialogBox;
import com.rohitneel.photopixelpro.constant.CommonKeys;
import com.rohitneel.photopixelpro.photoframe.utils.PaletteBar;
import com.rohitneel.photopixelpro.photoframe.utils.PathUtills;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ActivityCreatePhoto extends AppCompatActivity implements FilterFrameAdapter.FilterCallback {

    Context context;
    ImageView iv_createphoto_back;
    public ImageView ivframe, ivImage;
    private TextView save;

    // TextView
    public TextView textViewCancel;
    public TextView textViewDiscard;

    //AdView adView;
    ArrayList<ModelclassFrameList> frameLists;
    LinearLayout image, frame, flip, stickers, text, ll_frame_background, border, ll_filter, album;
    PopupWindow mPopupWindow, mPopupWindowpw;
    RelativeLayout rl_main, rl_main1;
    TextView tvHorizonal, tvVertical;
    String textStickerData = "";
    public static ActivityCreatePhoto instance = null;
    float scalediff;
    public static final int RequestPermissionCode = 1;
    String[] permissionsRequired = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 200;
    Uri photouri;
    String mCurrentPhotoPath;
    EditText et_text_sticker;
    TextSticker txtsticker;
    int textStickerColor = R.color.black;
    StickerView sticker_view;
    StickerView sticker_view_emoji;
    String[] fontList = new String[]{"0.ttf", "1.ttf", "2.otf", "3.ttf", "4.ttf", "5.ttf", "6.ttf", "7.ttf", "8.ttf", "9.otf", "10.ttf", "36.ttf", "font.ttf", "satisfy_regular.ttf"};
    ModelStickers[] stickerlist;
    ModelclassFlipList[] modelclassFlipLists;
    public ArrayList<ModelFontDetail> arrayList;
    int count = 0, textcolor;
    public Bitmap bitmapsave;
    public boolean isForShareGlobal;
    public Date currentTime;
    public String filePath = "";
    public String imgFileName;
    File imagesDir;
    public boolean showingFirst = true;
    public boolean showingsecond = true;
    TextView etcount;
    String abc = "0";
    androidx.appcompat.app.AlertDialog alertDialog;

    public ActivityCreatePhoto() {
        instance = ActivityCreatePhoto.this;
    }

    public static synchronized ActivityCreatePhoto getInstance() {
        if (instance == null) {
            instance = new ActivityCreatePhoto();
        }
        return instance;
    }

    ImageView gpuImageView;

    int progressChangedValue = 0;
    float alpha = 0.5f;
    float FontSize = 12f;
    public float letterSpace = 0.2f;
    public float lineSpaceAdd = 1f;
    public float lineSpaceMult = 1f;
    public float rotation = 90;

    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;
    float[] lastEvent = null;
    private SessionManager sessionManager;

    Bitmap galleryImage;


    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    ArrayList<Filter> filterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.login_sign_up_background));
        } else {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
        }
        setContentView(R.layout.activity_create_photo);
        context = ActivityCreatePhoto.this;

        sessionManager = new SessionManager(getApplicationContext());
        ivframe = findViewById(R.id.iv_frame);
        ivImage = findViewById(R.id.iv_image);
        image = findViewById(R.id.image);
        frame = findViewById(R.id.frame);
        stickers = findViewById(R.id.sticker);
        text = findViewById(R.id.text);
        album = findViewById(R.id.album);
        save = findViewById(R.id.txtSave);
        rl_main = findViewById(R.id.rl_main);
        rl_main1 = findViewById(R.id.rl_main1);
        iv_createphoto_back = findViewById(R.id.iv_createphoto_back);

        et_text_sticker = findViewById(R.id.et_text_sticker);
        et_text_sticker.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sticker_view = findViewById(R.id.sticker_view);
        sticker_view_emoji = findViewById(R.id.sticker_view_emoji);
        ll_frame_background = findViewById(R.id.ll_frame_background);
        border = findViewById(R.id.border);
        gpuImageView = findViewById(R.id.gpuImageView);
        ll_filter = findViewById(R.id.ll_filter);
        arrayList = new ArrayList<>();
        frameLists = new ArrayList<>();

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        Glide.with(this)
                .load(R.drawable.home) // Load drawable
                .override(160, 160) // Set dimensions as required
                .centerCrop()
                .into(gpuImageView);

        for (int i = 0; i < fontList.length; i++) {

            arrayList.add(new ModelFontDetail(fontList[i], fontList[i]));
        }

        iv_createphoto_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        rl_main1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker_view.hideIcons(true);
                sticker_view.setLocked(false);
                sticker_view_emoji.hideIcons(true);
                sticker_view_emoji.setLocked(false);
            }
        });

        stickerlist = new ModelStickers[]{
                new ModelStickers(R.drawable.birthdaystick1), new ModelStickers(R.drawable.birthdaystick3),
                new ModelStickers(R.drawable.birthdaystick5), new ModelStickers(R.drawable.birthdaystick6), new ModelStickers(R.drawable.birthdaystick7),
                new ModelStickers(R.drawable.birthdaystick8), new ModelStickers(R.drawable.birthdaystick9), new ModelStickers(R.drawable.birthdaystick10),
                new ModelStickers(R.drawable.catstick3),
                new ModelStickers(R.drawable.christmasstick1), new ModelStickers(R.drawable.christmasstick2), new ModelStickers(R.drawable.christmasstick3),
                new ModelStickers(R.drawable.christmasstick5), new ModelStickers(R.drawable.christmasstick6), new ModelStickers(R.drawable.christmasstick7), new ModelStickers(R.drawable.christmasstick8),
                new ModelStickers(R.drawable.christmasstick9), new ModelStickers(R.drawable.christmasstick10),
                new ModelStickers(R.drawable.goodmorningstick4), new ModelStickers(R.drawable.goodmorningstick4),
                new ModelStickers(R.drawable.goodmorningstick5), new ModelStickers(R.drawable.goodmorningstick7),
                new ModelStickers(R.drawable.couple_frame),new ModelStickers(R.drawable.mirror_3d_9_icon),
                new ModelStickers(R.drawable.newyearstick5), new ModelStickers(R.drawable.newyearstick6),
                new ModelStickers(R.drawable.newyearstick8),
                new ModelStickers(R.drawable.partystick3), new ModelStickers(R.drawable.partystick4), new ModelStickers(R.drawable.partystick5),
                new ModelStickers(R.drawable.partystick6), new ModelStickers(R.drawable.partystick8),
                new ModelStickers(R.drawable.santaclausstick2),
                new ModelStickers(R.drawable.santaclausstick3), new ModelStickers(R.drawable.santaclausstick4),
                new ModelStickers(R.drawable.textstick1), new ModelStickers(R.drawable.textstick2), new ModelStickers(R.drawable.textstick3), new ModelStickers(R.drawable.textstick4),
                new ModelStickers(R.drawable.textstick6), new ModelStickers(R.drawable.textstick7),
                new ModelStickers(R.drawable.textstick10)
        };


        stickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStickers();
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityMyCreations.class);
                startActivity(intent);
            }
        });

        border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBorder();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textPopUp();
            }
        });


        et_text_sticker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you wants to Edit Sticker");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            textStickerEditPopUp();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {
                                AlertDialogBox.AlertMessage(context, "Please enter text");
                            } else {
                                et_text_sticker.setVisibility(View.GONE);
                                txtsticker = new TextSticker(context);

                                et_text_sticker.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(et_text_sticker.getWindowToken(), 0);
                                    }
                                });


                                txtsticker.setText("");
                                txtsticker.getText();
                                txtsticker.setText(et_text_sticker.getText().toString());
                                txtsticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + CommonKeys.FontStyle));
                                txtsticker.setTextColor(getResources().getColor(textStickerColor));
                                textStickerColor = R.color.black;
                                txtsticker.resizeText();
                                sticker_view.addSticker(txtsticker);

                            }

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.BLACK);
                    pbutton.setBackgroundColor(Color.WHITE);

                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.BLACK);
                    nbutton.setBackgroundColor(Color.WHITE);

                }
                return false;
            }
        });


        Glide.with(context).load(getIntent().getIntExtra("frame", 0)).into(ivframe);

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFrames();
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ActivityGalleryFile.class);
                startActivity(intent);
            }
        });

        try {
            ivImage.setImageDrawable(getResources().getDrawable(R.drawable.white_bg));

        } catch (OutOfMemoryError e) {

        }
        ll_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterList();
            }
        });

        sticker_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {


            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker1) {
                sticker_view.hideIcons(false);
                sticker_view.setLocked(false);
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                sticker_view.hideIcons(false);
                sticker_view.setLocked(false);


                //et_text_sticker.setVisibility(View.VISIBLE);
                sticker = (TextSticker) sticker_view.getCurrentSticker();
                textStickerData = ((TextSticker) sticker).getText().toString();
                // et_text_sticker.setText(((TextSticker) sticker).getText());
                //et_text_sticker.setTextColor(((TextSticker) sticker).getTextColor());
                sticker_view.remove(sticker);


                textPopUp();
            }
        });

        sticker_view_emoji.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {


            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker1) {
                sticker_view_emoji.hideIcons(false);
                sticker_view_emoji.setLocked(false);
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                sticker_view_emoji.hideIcons(false);
                sticker_view_emoji.setLocked(false);


            }
        });

        ivImage.setOnTouchListener(new View.OnTouchListener() {

            RelativeLayout.LayoutParams parms;
            int startwidth;
            int startheight;
            float dx = 0, dy = 0, x = 0, y = 0;
            float angle = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final ImageView view = (ImageView) v;

                viewTransformation(view, event);

                return true;

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker_view.hideIcons(true);
                imgFileName = "PhotoFrame_" + System.currentTimeMillis();
                Uri result = null;
                bitmapsave = viewToBitmap(rl_main1);
                CommonKeys.Image = bitmapsave;
                try {
                    result = saveImage(context, CommonKeys.Image,getApplicationContext().getString(R.string.app_name)
                            ,imgFileName +".jpg");
                    CommonKeys.filePath = new File(PathUtills.getPath(ActivityCreatePhoto.this, result));
                    scanFile(context, result);
                    Toast.makeText(getApplicationContext(), "Saved to Gallery!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, ActivityPreviewImage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setImageUri(Uri uri) {
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(gpuImageView); // Load image URI into the ImageView
    }

    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }


    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    void textPopUp() {
        final androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.text_alert_raw, null);
        dialogBuilder.setView(dialogView);
        final EditText et_alert_text_sticker = dialogView.findViewById(R.id.et_alert_text_sticker);
        LinearLayout ll_alert_close = dialogView.findViewById(R.id.ll_alert_close);
        LinearLayout ll_alert_ok = dialogView.findViewById(R.id.ll_alert_ok);
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        et_alert_text_sticker.requestFocus();
        et_alert_text_sticker.setText(textStickerData);
        et_alert_text_sticker.setTextColor(getResources().getColor(R.color.hint));
        et_alert_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + "0.ttf"));

        ll_alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_text_sticker.setVisibility(View.VISIBLE);
                et_text_sticker.setText(et_alert_text_sticker.getText().toString());
                et_text_sticker.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                textStickerEditPopUp();
                alertDialog.dismiss();
            }
        });
        ll_alert_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (CommonKeys.imageSet) {
            Log.e("File", "" + "file://" + getIntent().getStringExtra("images"));
            Log.e("AppCon", "" + getApplicationContext());
            // Log.e("File",""+"file://" + getIntent().getStringExtra("images"));

            Uri uri = Uri.parse(getIntent().getStringExtra("images"));
            try {
                galleryImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, null);
                Log.e("hepi", "" + galleryImage.getHeight() + galleryImage.getWidth());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            int imageHeight = galleryImage.getHeight();
            int imageWidth = galleryImage.getWidth();
            if (imageHeight > 1000 && imageWidth > 1000 && imageHeight < 2000 && imageWidth < 2000) {
                imageHeight /= 2;
                imageWidth /= 2;
            } else if (imageHeight > 2000 || imageWidth > 2000) {
                if (imageHeight < 3000 || imageWidth < 3000) {
                    imageHeight /= 3;
                    imageWidth /= 3;
                } else if (imageHeight > 3000 || imageWidth > 3000) {
                    if (imageHeight < 4000 || imageWidth < 4000) {
                        imageHeight /= 4;
                        imageWidth /= 4;
                    } else if (imageHeight > 4000 || imageWidth > 4000) {
                        imageHeight /= 5;
                        imageWidth /= 5;
                    }
                }
            } else if (imageHeight > 1000 || imageWidth > 1000) {
                imageHeight = (int) (((double) imageHeight) / 1.6d);
                imageWidth = (int) (((double) imageWidth) / 1.6d);
            }

            // Log.e("hepi",""+  imageHeight+imageWidth);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            layoutParams.gravity = Gravity.CENTER;
            gpuImageView.setLayoutParams(layoutParams);

            setImageUri(uri);

            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(imageWidth, imageHeight);
            ivImage.setLayoutParams(layoutParams1);
            Glide.with(context).load("" + getIntent().getStringExtra("images")).into(ivImage);
            Glide.with(context).load(CommonKeys.frameLists.get(CommonKeys.FrameId1).getFrame()).into(ivframe);

            if (CommonKeys.cameraImage) {
                ivImage.setImageBitmap(CommonKeys.bmp);
            }
            CommonKeys.imageSet = false;
        } else {
            Glide.with(context).load(CommonKeys.frameLists.get(CommonKeys.FrameId1).getFrame()).into(ivframe);
        }
    }


    // Glide.with(context).load("file://" + getIntent().getStringExtra("images")).into(ivImage);


    public void textStickerEditPopUp() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        final View customView = inflater.inflate(R.layout.edit_text_sticker_popup, null);


        mPopupWindowpw = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


//
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindowpw.setElevation(5.0f);
        }
        mPopupWindowpw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindowpw.setOutsideTouchable(false);
        mPopupWindowpw.showAtLocation(rl_main, Gravity.BOTTOM, 0, 0);


        final TextView dialogTitle = (TextView) customView.findViewById(R.id.cp_accent_title);
        final TextView fontStyleTitle = (TextView) customView.findViewById(R.id.fontStyleTitle);
        final TextView fontTitle = (TextView) customView.findViewById(R.id.fontTitle);
        final TextView lineSpacingTitle = (TextView) customView.findViewById(R.id.lineSpacingTitle);
        final TextView txtAlignmentTitle = (TextView) customView.findViewById(R.id.txtAlignmentTitle);
        final TextView letterTitle = (TextView) customView.findViewById(R.id.letterTitle);
        final LinearLayout ll_text_color = (LinearLayout) customView.findViewById(R.id.ll_text_color);
        final LinearLayout ll_text_style = (LinearLayout) customView.findViewById(R.id.ll_text_style);
        final Button btn_Ok = (Button) customView.findViewById(R.id.btn_Ok);

        final TextView tvFontSize = customView.findViewById(R.id.tvFontSize);
        Button btnOk = customView.findViewById(R.id.btnOk);
        SeekBar sbSize = customView.findViewById(R.id.sbSize);
        ImageView iv_alignment_right = customView.findViewById(R.id.iv_alignment_right);
        ImageView iv_alignment_center = customView.findViewById(R.id.iv_alignment_center);
        ImageView iv_alignment_left = customView.findViewById(R.id.iv_alignment_left);

        ImageView iv_line_up = customView.findViewById(R.id.iv_line_up);
        ImageView iv_line_down = customView.findViewById(R.id.iv_line_down);


        ImageView iv_wordspace_far = customView.findViewById(R.id.iv_wordspace_far);
        ImageView iv_wordspace_near = customView.findViewById(R.id.iv_wordspace_near);

        if(sessionManager.loadState()){
            dialogTitle.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            fontStyleTitle.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            fontTitle.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            lineSpacingTitle.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            txtAlignmentTitle.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            letterTitle.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            iv_line_up.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            iv_line_down.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            iv_wordspace_far.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            iv_wordspace_near.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }

        iv_alignment_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_text_sticker.setGravity(Gravity.RIGHT);

            }
        });
        iv_alignment_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_text_sticker.setGravity(Gravity.LEFT);
            }
        });
        iv_alignment_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_text_sticker.setGravity(Gravity.CENTER);
            }
        });


        iv_wordspace_far.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_text_sticker.setLetterSpacing(letterSpace);
                letterSpace = letterSpace + 0.2f;
               /* a++;
                Toast.makeText(context,""+a,Toast.LENGTH_LONG).show();
*/
            }
        });
        iv_wordspace_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (letterSpace >= 0.2f) {
                    letterSpace = letterSpace - 0.2f;
                    et_text_sticker.setLetterSpacing(letterSpace);
                }
            }
        });

        iv_line_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                et_text_sticker.setLineSpacing(lineSpaceAdd, lineSpaceMult);
                lineSpaceMult = lineSpaceMult + 0.2f;

            }
        });

        iv_line_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lineSpaceMult > 1f) {
                    lineSpaceMult = lineSpaceMult - 0.2f;
                    et_text_sticker.setLineSpacing(lineSpaceAdd, lineSpaceMult);
                }

            }
        });


        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                FontSize = (float) progress;
                if (progress < 8) {
                    progress = 8;
                } else {
                    progress = ((int) Math.round(progress / 2)) * 2;
//                    FontSize = (Float.valueOf(progress) / Float.valueOf(100));
                }
                tvFontSize.setText(progress + "");
                FontSize = (float) progress;
                et_text_sticker.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
//                mAutoFitEditText.setMinTextSize((float) progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbSize.setProgress(20);
        tvFontSize.setText("20");


        ll_text_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textStickerColorPopUp();

            }
        });


        ll_text_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFontList();
            }
        });
        dialogTitle.setText("Edit Sticker");


        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {
                    AlertDialogBox.AlertMessage(context, "Please enter text");
                    mPopupWindowpw.dismiss();


                } else {
                    et_text_sticker.setVisibility(View.GONE);
                    txtsticker = new TextSticker(context);
                    txtsticker.setText("");
                    et_text_sticker.post(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_text_sticker.getWindowToken(), 0);

                        }
                    });

                    txtsticker.setLineSpacing(lineSpaceAdd, lineSpaceMult);
                  /*  txtsticker.setMaxTextSize(FontSize);
                    txtsticker.setMinTextSize(FontSize);*/


                    // Toast.makeText(context, et_text_sticker.getText().toString(), Toast.LENGTH_LONG).show();
                    txtsticker.setText(et_text_sticker.getText().toString());
                    txtsticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + CommonKeys.FontStyle));
                    txtsticker.setTextColor(textcolor);
                    textStickerColor = R.color.black;
                    txtsticker.resizeText();
                    sticker_view.addSticker(txtsticker);
                    mPopupWindowpw.dismiss();


                }


            }


        });


    }

    @Override
    public void onBackPressed() {
        final Dialog dialogOnBackPressed = new Dialog(ActivityCreatePhoto.this, R.style.UploadDialog);
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
            ActivityCreatePhoto.this.finish();
            super.onBackPressed();
            finish();
        });
    }

    public void textStickerColorPopUp() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.select_color_popup, null);

        PaletteBar paletteBar = customView.findViewById(R.id.paletteBar);
        paletteBar.setListener(new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int color) {
                et_text_sticker.setTextColor(color);
                textcolor = color;

            }
        });

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);


        final TextView dialogTitle = (TextView) customView.findViewById(R.id.cp_accent_title);
        final Button btnOk = (Button) customView.findViewById(R.id.btnOk);
        final Button btnCancel = (Button) customView.findViewById(R.id.btnCancel);

        dialogTitle.setText("Text Color");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();

            }
        });


        mPopupWindow.showAtLocation(rl_main, Gravity.BOTTOM, 0, 0);
    }


    public void openStickers() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.frame_pop_up, null);

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mPopupWindow.setAnimationStyle(R.style.animation_for_popup);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

        mPopupWindow.setOutsideTouchable(true);


        RecyclerView rvList = customView.findViewById(R.id.rvList);


        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        AdapterStickersList adapterStickersList = new AdapterStickersList(context, R.layout.framelist_raw_item, stickerlist, "popup");
        rvList.setAdapter(adapterStickersList);

        mPopupWindow.showAtLocation(rl_main, Gravity.BOTTOM, 150, 150);
    }


    public void openFontList() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.popup_for_select_font, null);


        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);


        RecyclerView rvList = customView.findViewById(R.id.rvList);
        Button btnOk = customView.findViewById(R.id.btnOk);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(linearLayoutManager);

        AdapterFontList adapterFontList = new AdapterFontList(context, R.layout.font_list_row, arrayList);
        rvList.setAdapter(adapterFontList);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();

            }
        });

        mPopupWindow.showAtLocation(rl_main, Gravity.BOTTOM, 0, 0);

    }


    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    // Method (Photo Rotation)...................
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void setEmoji(int position) {

        sticker_view_emoji.addSticker(new DrawableSticker(getResources().getDrawable(stickerlist[position].getImgId())));
        mPopupWindow.dismiss();
    }

    // Method (Font Style to Text)...................
    public void SetFontToText(String FontName) {

        CommonKeys.ischangetypeface = true;

        et_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + FontName));
        CommonKeys.FontStyle = FontName;
    }


    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void setBorder() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.border_size_popup, null);
        ImageView plusbtn, minusbtn;
        plusbtn = customView.findViewById(R.id.ivPlus);
        minusbtn = customView.findViewById(R.id.ivMinus);
        etcount = customView.findViewById(R.id.tvBordersize);
        PaletteBar paletteBar = customView.findViewById(R.id.paletteBar);
        final LinearLayout ll_selectborder = customView.findViewById(R.id.ll_selectborder);


        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mPopupWindow.setAnimationStyle(R.style.animation_for_popup);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }


        paletteBar.setListener(new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int color) {
                ll_frame_background.setBackgroundColor(color);

            }
        });

        etcount.setText("0");
        // etcount.setCursorVisible(false);

        etcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selectborder.setBackgroundColor(getResources().getColor(R.color.black));
            }
        });

        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iscolor = true;
                ll_selectborder.setBackgroundColor(getResources().getColor(R.color.white));
                if (iscolor) {
                    ll_selectborder.setBackgroundColor(Color.BLACK);
                    count++;

                    if (count <= 25) {
                        ll_selectborder.setBackgroundColor(getResources().getColor(R.color.black));
                        etcount.setText(String.valueOf(count));
                        int margin = Integer.parseInt(etcount.getText().toString());
                        RelativeLayout.LayoutParams parameter = (RelativeLayout.LayoutParams) rl_main.getLayoutParams();
                        parameter.setMargins(margin, margin, margin, margin); // left, top, right, bottom
                        rl_main.setLayoutParams(parameter);
                        abc = etcount.getText().toString();


                    } else {
                        count = 25;
                        ll_selectborder.setBackgroundColor(getResources().getColor(R.color.black));

                    }
                }


            }
        });

        minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iscolor = true;
                ll_selectborder.setBackgroundColor(getResources().getColor(R.color.white));

                if (iscolor) {
                    ll_selectborder.setBackgroundColor(Color.BLACK);
                    count--;
                    if (count >= 0) {
                        ll_selectborder.setBackgroundColor(getResources().getColor(R.color.black));
                        etcount.setText(String.valueOf(count));
                        int margin = Integer.parseInt(etcount.getText().toString());
                        RelativeLayout.LayoutParams parameter = (RelativeLayout.LayoutParams) rl_main.getLayoutParams();
                        parameter.setMargins(margin, margin, margin, margin); // left, top, right, bottom
                        rl_main.setLayoutParams(parameter);
                        abc = etcount.getText().toString();
                    } else {
                        count = 0;
                        ll_selectborder.setBackgroundColor(getResources().getColor(R.color.black));
                    }
                }
            }
        });
        etcount.setText(abc);

        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.showAtLocation(rl_main, Gravity.BOTTOM, 160, 320);
    }

    public void filterList() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.frame_pop_up, null);

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mPopupWindow.setAnimationStyle(R.style.animation_for_popup);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setOutsideTouchable(true);
        RecyclerView rvList = customView.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filterList.clear();
        filterList = DataBinder.fetchFilters();
        FilterFrameAdapter filterAdapter = new FilterFrameAdapter(ActivityCreatePhoto.this, filterList);
        rvList.setAdapter(filterAdapter);

        mPopupWindow.showAtLocation(rl_main, Gravity.BOTTOM, 150, 150);

      /* LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.fliprotation_popup, null);

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mPopupWindow.setAnimationStyle(R.style.animation_for_popup);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        tvHorizonal = customView.findViewById(R.id.tvHorizontal);
        tvVertical = customView.findViewById(R.id.tvVertical);

        tvHorizonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (showingFirst) {
                    ivImage.setRotationY(180f);
                    showingFirst = false;
                } else {
                    ivImage.setRotationY(0f);
                    showingFirst = true;
                }

                mPopupWindow.dismiss();
            }

        });

        tvVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (showingsecond) {
                    ivImage.setRotationX(180f);
                    showingsecond = false;
                } else {
                    ivImage.setRotationX(0f);
                    showingsecond = true;
                }
                mPopupWindow.dismiss();


            }
        });
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.showAtLocation(rl_main, Gravity.BOTTOM, 150, 300);*/
    }

    public void openFrames() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.frame_pop_up, null);

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mPopupWindow.setAnimationStyle(R.style.animation_for_popup);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setOutsideTouchable(true);
        RecyclerView rvList = customView.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvList.setLayoutManager(linearLayoutManager);*/

        AdepterFrameListPopup adepterFrameList = new AdepterFrameListPopup(context, R.layout.framelist_raw_item, CommonKeys.frameLists, "popup");
        rvList.setAdapter(adepterFrameList);
        mPopupWindow.showAtLocation(rl_main, Gravity.BOTTOM, 150, 150);
    }

    public void setFrame(int imgId) {
        CommonKeys.FrameId = imgId;
        Glide.with(context).load(CommonKeys.frameLists.get(imgId).getFrame()).into(ivframe);
        mPopupWindow.dismiss();
    }

    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);
    }

    private Uri saveImage(Context context, Bitmap bitmap, @NonNull String folderName, @NonNull String fileName) throws IOException {
        OutputStream fos = null;
        File imageFile = null;
        Uri imageUri = null;
        imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

        if (!imagesDir.exists())
            imagesDir.mkdir();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
                contentValues.put(
                        MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + folderName);
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                if (imageUri == null)
                    throw new IOException("Failed to create new MediaStore record.");

                fos = resolver.openOutputStream(imageUri);
            } else {
                imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

                if (!imagesDir.exists())
                    imagesDir.mkdir();

                imageFile = new File(imagesDir, fileName + ".jpg");
                fos = new FileOutputStream(imageFile);
            }


            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos))
                throw new IOException("Failed to save bitmap.");
            fos.flush();
        } finally {
            if (fos != null)
                fos.close();
        }

        if (imageFile != null) {//pre Q
            MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, null);
            imageUri = Uri.fromFile(imageFile);
        }
        return imageUri;
    }

    public void EnableRuntimePermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityCreatePhoto.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Log.e("ABC", "1");
            //  cameraPermission();
        }


        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityCreatePhoto.this, permissionsRequired[0])) {
            //Show Information about why you need the permission

            Log.e("ABC", "4");
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCreatePhoto.this);
            builder.setTitle("Need Permissions");
            builder.setMessage("This app needs Write External permissions.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(ActivityCreatePhoto.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.BLACK);
            pbutton.setBackgroundColor(Color.WHITE);
        } else {
            //just request the permission
            Log.e("ABC", "5");
            ActivityCompat.requestPermissions(ActivityCreatePhoto.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK) {
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();

            Bitmap bitmap = null;
            /*Bitmap picture = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
            ivImage.setImageBitmap(picture);
*/
            try {
                Bitmap picture = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
                ivImage.setImageBitmap(picture);
                CommonKeys.cameraImage = true;
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photouri);
                CommonKeys.bmp = bitmap;
                ivImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(ActivityCreateFrame.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    // Toast.makeText(ActivityCreateFrame.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    @Override
    public void FilterMethod(int i) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home);
        Bitmap filteredBitmap = DataBinder.applyFilter(i, this, originalBitmap);
        gpuImageView.setImageBitmap(filteredBitmap);
    }
}
