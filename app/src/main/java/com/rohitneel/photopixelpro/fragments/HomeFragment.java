package com.rohitneel.photopixelpro.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.rohitneel.photopixelpro.gallery.CollageGallery;
import com.rohitneel.photopixelpro.helper.SessionManager;
import com.rohitneel.photopixelpro.photocollage.activities.GridPickerActivity;
import com.rohitneel.photopixelpro.photocollage.dialog.DetailsDialog;
import com.rohitneel.photopixelpro.photoeditor.MediaActivity;
import com.rohitneel.photopixelpro.tutorials.TutorialActivity;

import java.util.HashMap;
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
    private String userNameLetter;

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
        HashMap<String, String> user = sessionManager.getUserName();
        emailAddress = user.get(SessionManager.KEY_NAME);
        if (emailAddress != null) {
            char name = emailAddress.charAt(0);
            userNameLetter = Character.toString(name);
        }
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
                /*intent.putExtra("Email",emailAddress);
                intent.putExtra("Home", "HomeFragment");
                intent.putExtra("nameLetter",userNameLetter);*/
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

        Dexter.withContext(getContext()).withPermissions("android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    Intent intent = new Intent(getContext(), GridPickerActivity.class);
                    intent.putExtra(GridPickerActivity.KEY_LIMIT_MAX_IMAGE, 9);
                    intent.putExtra(GridPickerActivity.KEY_LIMIT_MIN_IMAGE, 2);
                    startActivityForResult(intent, 1001);
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    DetailsDialog.showDetailsDialog(getActivity());
                }
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(dexterError -> Toast.makeText(getContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

    }

}
