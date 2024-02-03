package com.rohitneel.photopixelpro.backgroundremover;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rohitneel.photopixelpro.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EraserActivity extends AppCompatActivity implements OnClickListener {
	private ContentResolver mContentResolver;
	private Bitmap mBitmap;

	HoverView mHoverView;
	double mDensity;
	
	int viewWidth;
	int viewHeight;
	int bmWidth;
	int bmHeight;
	
	int actionBarHeight;
	int bottombarHeight;
	double bmRatio;
	double viewRatio;

	ImageView eraserSubButton, unEraserSubButton;
	ImageView brushSize1Button, brushSize2Button, brushSize3Button, brushSize4Button;
	ImageView magicRemoveButton, magicRestoreButton;
	ImageView  undoButton, redoButton;
	LinearLayout colorButton;
	ImageView backgroundEraserBack;
	private ImageView eraserImage, closeImage, backgroundImage, mirrorImage, magicImage;
	private TextView eraserText, closeText, backgroundText, mirrorText, magicText;

	// TextView
	public TextView textViewCancel;
	public TextView textViewDiscard;
	SeekBar magicSeekbar;
	RelativeLayout eraserLayout, magicLayout;
	RelativeLayout mLayout;

	LinearLayout magicButton, eraserButton, mirrorImageButton,closeButton;
	private String imageFilePath;
	private TextView saveButton;


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
		setContentView(R.layout.activity_eraser);
		mContentResolver = getContentResolver();
		backgroundEraserBack = findViewById(R.id.bgScreenBack);
		eraserImage = findViewById(R.id.eraser_img);
		magicImage = findViewById(R.id.magic_img);
		mirrorImage = findViewById(R.id.mirror_img);
		closeImage = findViewById(R.id.close_img);
		backgroundImage = findViewById(R.id.color_lens_img);
		eraserText = findViewById(R.id.eraser_txt);
		magicText = findViewById(R.id.magic_txt);
		mirrorText = findViewById(R.id.mirror_txt);
		closeText = findViewById(R.id.close_txt);
		backgroundText = findViewById(R.id.color_txt);

		eraserImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		magicImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		mirrorImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		closeImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		backgroundImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));

		ActionBar bar = this.getSupportActionBar();
		if (bar != null) {
			bar.hide();
		}

		if (Build.VERSION.SDK_INT >= 23 ) {
			if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		}


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			imageFilePath = extras.getString("imagePath");
		}

		mBitmap = BitmapFactory.decodeFile(imageFilePath);

		mLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mDensity = getResources().getDisplayMetrics().density;
        actionBarHeight = (int)(110*mDensity);
        bottombarHeight = (int)(60*mDensity);

		backgroundEraserBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

        viewWidth = getResources().getDisplayMetrics().widthPixels;
        viewHeight = getResources().getDisplayMetrics().heightPixels - actionBarHeight - bottombarHeight;
        viewRatio = (double) viewHeight/ (double) viewWidth;

		bmRatio = (double) mBitmap.getHeight()/ (double) mBitmap.getWidth();
		if(bmRatio < viewRatio) {
			bmWidth = viewWidth;
			bmHeight = (int)(((double)viewWidth)*((double)(mBitmap.getHeight())/(double)(mBitmap.getWidth())));
		} else {
			bmHeight = viewHeight;
			bmWidth = (int)(((double)viewHeight)*((double)(mBitmap.getWidth())/(double)(mBitmap.getHeight())));
		}


		mBitmap = Bitmap.createScaledBitmap(mBitmap, bmWidth, bmHeight, false);

		File fdelete = new File(imageFilePath);
		if (fdelete.exists()) {
			fdelete.delete();
		}

		mHoverView = new HoverView(this, mBitmap, bmWidth, bmHeight, viewWidth, viewHeight);
		mHoverView.setLayoutParams(new LayoutParams(viewWidth, viewHeight));

		mLayout.addView(mHoverView);

		initButton();

	}


	public void initButton() {
		eraserButton = findViewById(R.id.eraserBtn);
		eraserButton.setOnClickListener(this);
		magicButton = findViewById(R.id.magicBtn);
		magicButton.setOnClickListener(this);
		mirrorImageButton = findViewById(R.id.mirrorBtn);
		mirrorImageButton.setOnClickListener(this);
		closeButton = findViewById(R.id.closeBtn);
		closeButton.setOnClickListener(this);
		
		eraserSubButton = (ImageView) findViewById(R.id.erase_sub_button);
		eraserSubButton.setOnClickListener(this);
		unEraserSubButton = (ImageView) findViewById(R.id.unerase_sub_button);
		unEraserSubButton.setOnClickListener(this);
		
		brushSize1Button = (ImageView) findViewById(R.id.brush_size_1_button);
		brushSize1Button.setOnClickListener(this);
		
		brushSize2Button = (ImageView) findViewById(R.id.brush_size_2_button);
		brushSize2Button.setOnClickListener(this);
		
		brushSize3Button = (ImageView) findViewById(R.id.brush_size_3_button);
		brushSize3Button.setOnClickListener(this);
		
		brushSize4Button = (ImageView) findViewById(R.id.brush_size_4_button);
		brushSize4Button.setOnClickListener(this);
		

		magicSeekbar = (SeekBar) findViewById(R.id.magic_seekbar);
		magicSeekbar.setProgress(15);
		magicSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mHoverView.setMagicThreshold(seekBar.getProgress());
				if(mHoverView.getMode() == mHoverView.MAGIC_MODE)
					mHoverView.magicEraseBitmap();
				else if(mHoverView.getMode() == mHoverView.MAGIC_MODE_RESTORE)
					mHoverView.magicRestoreBitmap();
				mHoverView.invalidateView();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					/*mHoverView.setMagicThreshold(progress);
					if(mHoverView.getMode() == mHoverView.MAGIC_MODE)
						mHoverView.magicEraseBitmap();
					else if(mHoverView.getMode() == mHoverView.MAGIC_MODE_RESTORE)
						mHoverView.magicRestoreBitmap();
					mHoverView.invalidateView();*/
			}
		});
		
		magicRemoveButton = (ImageView) findViewById(R.id.magic_remove_button);
		magicRemoveButton.setOnClickListener(this);
		magicRestoreButton = (ImageView) findViewById(R.id.magic_restore_button);
		magicRestoreButton.setOnClickListener(this);
		
		saveButton = findViewById(R.id.txtEraserSave);
		saveButton.setOnClickListener(this);
		
		undoButton = (ImageView) findViewById(R.id.undoButton);
		undoButton.setOnClickListener(this);
		
		redoButton = (ImageView) findViewById(R.id.redoButton);
		redoButton.setOnClickListener(this);
		updateRedoButton();
		
		eraserLayout = (RelativeLayout) findViewById(R.id.eraser_layout);
		magicLayout = (RelativeLayout) findViewById(R.id.magicWand_layout);
		eraserButton.setSelected(true);
		
		colorButton = findViewById(R.id.colorButton);
		colorButton.setOnClickListener(this);
	}

	private Bitmap getBitmap(String path) {
		Uri uri = getImageUri(path);
		InputStream in = null;
		try {
			//final int IMAGE_MAX_SIZE = maxWidth;
			in = mContentResolver.openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			/*if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(2, (int) Math
						.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
			}*/

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			in = mContentResolver.openInputStream(uri);
			Bitmap b = BitmapFactory.decodeStream(in, null, o2);
			in.close();

			return b;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void resetSeekBar() {
		magicSeekbar.setProgress(0);
		mHoverView.setMagicThreshold(0);
	}

	private Uri getImageUri(String path) {
		return Uri.fromFile(new File(path));
	}

	private Matrix getOrientationMatrix(String path) {
		Matrix matrix = new Matrix();
		ExifInterface exif;
		try {
			exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		       
			switch (orientation) {
		    case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
		        matrix.setScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_180:
		        matrix.setRotate(180);
		        break;
		    case ExifInterface.ORIENTATION_FLIP_VERTICAL:
		        matrix.setRotate(180);
		        matrix.postScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_TRANSPOSE:
		        matrix.setRotate(90);
		        matrix.postScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_90:
		        matrix.setRotate(90);
		        break;
		    case ExifInterface.ORIENTATION_TRANSVERSE:
		        matrix.setRotate(-90);
		        matrix.postScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_270:
		        matrix.setRotate(-90);
		        break;
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	       
       return matrix;
	}
	
	int currentColor = 0;
	
	public void setBackGroundColor(int color) {
		switch (color) {
		case 0:
			mLayout.setBackgroundResource(R.drawable.background_eraser_screen);
			//colorButton.setBackgroundResource(R.drawable.white_drawable);
			break;
		case 1:
			mLayout.setBackgroundColor(Color.WHITE);
			//colorButton.setBackgroundResource(R.drawable.black_drawable);
			break;
		case 2:
			mLayout.setBackgroundColor(Color.BLACK);
			//colorButton.setBackgroundResource(R.drawable.transparent_drawable);
			break;

		default:
			break;
		}
		
		currentColor = color;
	}
	
	public void resetMainButtonState() {
		eraserButton.setSelected(false);
		magicButton.setSelected(false);
		mirrorImageButton.setSelected(false);
		closeButton.setSelected(false);
	}
	
	public void resetSubEraserButtonState() {
		eraserSubButton.setSelected(false);
		unEraserSubButton.setSelected(false);
	}
	
	public void resetSubMagicButtonState() {
		magicRemoveButton.setSelected(false);
		magicRestoreButton.setSelected(false);
	}
	
	public void resetBrushButtonState() {
		brushSize1Button.setSelected(false);
		brushSize2Button.setSelected(false);
		brushSize3Button.setSelected(false);
		brushSize4Button.setSelected(false);
	}
	
	public void updateUndoButton() {
		if(mHoverView.checkUndoEnable()) {
			undoButton.setEnabled(true);
			undoButton.setAlpha(1.0f);
		}
		else {
			undoButton.setEnabled(false);
			undoButton.setAlpha(0.3f);
		}
	}
	
	public void updateRedoButton() {
		if(mHoverView.checkRedoEnable()) {
			redoButton.setEnabled(true);
			redoButton.setAlpha(1.0f);
		}
		else {
			redoButton.setEnabled(false);
			redoButton.setAlpha(0.3f);
		}
	}

	@Override
	public void onClick(View v) {
		updateUndoButton();
		updateRedoButton();

		eraserImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		eraserText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
		magicImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		magicText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
		mirrorImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		mirrorText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
		closeImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		closeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
		backgroundImage.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
		backgroundText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

		switch (v.getId()) {
			case R.id.eraserBtn:
				mHoverView.switchMode(mHoverView.ERASE_MODE);
				if(eraserLayout.getVisibility() == View.VISIBLE) {
					eraserLayout.setVisibility(View.GONE);
				} else {
					eraserLayout.setVisibility(View.VISIBLE);
				}
				magicLayout.setVisibility(View.GONE);
				resetMainButtonState();
				resetSubEraserButtonState();
				eraserSubButton.setSelected(true);
				eraserButton.setSelected(true);
				eraserImage.setColorFilter(Color.RED);
				eraserText.setTextColor(Color.RED);
				break;
			case R.id.magicBtn:
				mHoverView.switchMode(HoverView.MAGIC_MODE);
				if(magicLayout.getVisibility() == View.VISIBLE) {
					magicLayout.setVisibility(View.GONE);
				} else {
					magicLayout.setVisibility(View.VISIBLE);
				}
				eraserLayout.setVisibility(View.GONE);
				resetMainButtonState();
				resetSubMagicButtonState();
				resetSeekBar();
				magicRemoveButton.setSelected(true);
				magicButton.setSelected(true);
				magicImage.setColorFilter(Color.RED);
				magicText.setTextColor(Color.RED);
				break;
				
			case R.id.mirrorBtn:
				findViewById(R.id.eraser_layout).setVisibility(View.GONE);
				findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
				mHoverView.mirrorImage();
				mirrorImage.setColorFilter(Color.RED);
				mirrorText.setTextColor(Color.RED);
				break;
			case R.id.closeBtn:
				//eraserText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColor));
				mHoverView.switchMode(HoverView.MOVING_MODE);
				findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
				findViewById(R.id.eraser_layout).setVisibility(View.GONE);
				resetMainButtonState();
				closeButton.setSelected(true);
				closeImage.setColorFilter(Color.RED);
				closeText.setTextColor(Color.RED);
				break;
			case R.id.erase_sub_button:
				mHoverView.switchMode(HoverView.ERASE_MODE);
				resetSubEraserButtonState();
				eraserSubButton.setSelected(true);
				eraserImage.setColorFilter(Color.RED);
				eraserText.setTextColor(Color.RED);
				break;
			case R.id.unerase_sub_button:
				mHoverView.switchMode(HoverView.UNERASE_MODE);
				resetSubEraserButtonState();
				unEraserSubButton.setSelected(true);
				eraserImage.setColorFilter(Color.RED);
				eraserText.setTextColor(Color.RED);
				break;
				
			case R.id.brush_size_1_button:
				resetBrushButtonState();
				mHoverView.setEraseOffset(40);
				brushSize1Button.setSelected(true);
				eraserImage.setColorFilter(Color.RED);
				eraserText.setTextColor(Color.RED);
				break;
				
			case R.id.brush_size_2_button:
				resetBrushButtonState();
				mHoverView.setEraseOffset(60);
				brushSize2Button.setSelected(true);
				eraserImage.setColorFilter(Color.RED);
				eraserText.setTextColor(Color.RED);
				break;
				
			case R.id.brush_size_3_button:
				resetBrushButtonState();
				mHoverView.setEraseOffset(80);
				brushSize3Button.setSelected(true);
				eraserImage.setColorFilter(Color.RED);
				eraserText.setTextColor(Color.RED);
				break;
				
			case R.id.brush_size_4_button:
				resetBrushButtonState();
				mHoverView.setEraseOffset(100);
				brushSize4Button.setSelected(true);
				eraserImage.setColorFilter(Color.RED);
				eraserText.setTextColor(Color.RED);
				break;
				
			case R.id.magic_remove_button:
				resetSubMagicButtonState();
				magicRemoveButton.setSelected(true);
				mHoverView.switchMode(HoverView.MAGIC_MODE);
				resetSeekBar();
				magicImage.setColorFilter(Color.RED);
				magicText.setTextColor(Color.RED);
				break;
				
			case R.id.magic_restore_button:
				resetSubMagicButtonState();
				magicRestoreButton.setSelected(true);
				mHoverView.switchMode(HoverView.MAGIC_MODE_RESTORE);
				resetSeekBar();
				magicImage.setColorFilter(Color.RED);
				magicText.setTextColor(Color.RED);
				break;
			case R.id.txtEraserSave:
				/*Intent intent = new Intent(getApplicationContext(), PositionActivity.class);
				intent.putExtra("imagePath", mHoverView.save());
				startActivity(intent);*/
				mHoverView.save();
				break;
			case R.id.colorButton:
				setBackGroundColor((currentColor+1)%3);
				backgroundImage.setColorFilter(Color.RED);
				backgroundText.setTextColor(Color.RED);
				break;
			case R.id.undoButton:
				findViewById(R.id.eraser_layout).setVisibility(View.GONE);
				findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
				mHoverView.undo();
				if(mHoverView.checkUndoEnable()) {
					undoButton.setEnabled(true);
					undoButton.setAlpha(1.0f);
				}
				else {
					undoButton.setEnabled(false);
					undoButton.setAlpha(0.3f);
				}
				updateRedoButton();
				break;
			case R.id.redoButton:
				findViewById(R.id.eraser_layout).setVisibility(View.GONE);
				findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
				mHoverView.redo();
				updateUndoButton();
				updateRedoButton();
				break;
		}
	}

	@Override
	public void onBackPressed() {
		final Dialog dialogOnBackPressed = new Dialog(EraserActivity.this, R.style.UploadDialog);
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
			EraserActivity.this.finish();
			super.onBackPressed();
			finish();
		});
	}
}
