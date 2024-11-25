package com.rohitneel.photopixelpro.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.constant.SelectorSettings;
import com.rohitneel.photopixelpro.gallery.CollageGallery;
import com.rohitneel.photopixelpro.helper.SessionManager;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoCollageActivity;
import com.rohitneel.photopixelpro.photocollage.activities.PickerActivity;
import com.rohitneel.photopixelpro.photocollage.dialog.DetailsDialog;
import com.rohitneel.photopixelpro.photoeditor.MediaActivity;
import com.rohitneel.photopixelpro.tutorials.TutorialActivity;
import java.util.ArrayList;
import java.util.List;

import static com.rohitneel.photopixelpro.constant.CommonKeys.mHomeKey;

public class HomeFragment extends Fragment {

    private CardView cardTutorialView;
    private CardView cardPersonalDataView;
    private CardView cardPhotoCollage;
    private CardView cardCollageGallery;
    private SessionManager sessionManager;
    private RelativeLayout relativeLayout;
    private String emailAddress;
    private static final int REQUEST_CODE = 732;
    public ArrayList<String> mResults = new ArrayList<>();
    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardTutorialView = view.findViewById(R.id.cardTutorial);
        cardPersonalDataView = view.findViewById(R.id.cardPersonalData);
        cardPhotoCollage = view.findViewById(R.id.cardPhotoCollage);
        cardCollageGallery = view.findViewById(R.id.cardCollageGallery);
        relativeLayout = view.findViewById(R.id.rlCardView);
        sessionManager = new SessionManager(getContext());
        if (sessionManager.loadState()) {
            relativeLayout.setBackgroundResource(R.drawable.background_image_night);
        }
        else {
            relativeLayout.setBackgroundResource(R.drawable.background_image);
        }
        addListeners();
    }

    private void addListeners() {
        cardTutorialView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getContext(), TutorialActivity.class);
               intent.putExtra(mHomeKey,"home");
               startActivity(intent);
            }
        });

        cardPersonalDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MediaActivity.class);
                startActivity(intent);
            }
        });

        cardPhotoCollage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCollage();
            }
        });

        cardCollageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getContext(), CollageGallery.class));
            }
        });
    }

    private void goToCollage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] arrPermissionsGrid = {"android.permission.READ_MEDIA_IMAGES"};
            Dexter.withContext(getContext()).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(getContext(), PickerActivity.class);
                        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
                        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 2);
                        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {DetailsDialog.showDetailsDialog(getActivity());                    }
                }
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError ->Toast.makeText(getContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        } else {
            String[] arrPermissionsGrid = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (Build.VERSION.SDK_INT >= 29) arrPermissionsGrid=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
            Dexter.withContext(getContext()).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(getContext(), PickerActivity.class);
                        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
                        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 2);
                        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {DetailsDialog.showDetailsDialog(getActivity());}
                }
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {permissionToken.continuePermissionRequest();}
            }).withErrorListener(dexterError ->Toast.makeText(getContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (data != null) {
            ArrayList<String> listExtra = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
            this.mResults = listExtra;
            if (listExtra!= null && listExtra.isEmpty()) {
                // Toast.makeText(this, getResources().getString(R.string.txt_select_picture), Toast.LENGTH_SHORT).show();
            } else if (this.mResults.size() == 1) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), PhotoCollageActivity.class);
                intent.putStringArrayListExtra(KEY_DATA_RESULT, mResults);
                startActivity(intent);
            }
        }
    }
}
