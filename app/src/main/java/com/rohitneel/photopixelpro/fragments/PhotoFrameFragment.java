package com.rohitneel.photopixelpro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.helper.SessionManager;
import com.rohitneel.photopixelpro.photoframe.activities.PhotoFrameActivity;

import static com.rohitneel.photopixelpro.constant.CommonKeys.mFrameKey;

public class PhotoFrameFragment extends Fragment {

    private CardView cardBirthdayFrame;
    private CardView cardFlowerFrame;
    private CardView cardLoveAnniversaryFrame;
    private CardView cardWallFrame;
    private SessionManager mSession;
    private TextView txtBirthdayFrame, txtFlowersFrame, txtLoveFrame, txtWallFrame;
    private LinearLayout linearFrameLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_frame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardBirthdayFrame = view.findViewById(R.id.cardBirthdayFrame);
        cardFlowerFrame = view.findViewById(R.id.cardFlowerFrame);
        cardLoveAnniversaryFrame = view.findViewById(R.id.cardLoveFrame);
        cardWallFrame = view.findViewById(R.id.cardWallFrame);
        txtBirthdayFrame = view.findViewById(R.id.txtBirthday);
        txtFlowersFrame = view.findViewById(R.id.txtFlowers);
        txtLoveFrame =  view.findViewById(R.id.txtLove);
        txtWallFrame = view.findViewById(R.id.txtWallFrame);
        linearFrameLayout = view.findViewById(R.id.fragmentFrame);
        mSession = new SessionManager(getContext());
        if (mSession.loadState()){
            txtBirthdayFrame.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            txtFlowersFrame.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            txtLoveFrame.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            txtWallFrame.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            linearFrameLayout.setBackgroundResource(R.color.background_color);
        }
        addListeners();
    }

    private void addListeners() {

        cardBirthdayFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frameIntent = new Intent(getContext(), PhotoFrameActivity.class);
                frameIntent.putExtra(mFrameKey, "birthday");
                startActivity(frameIntent);
            }
        });

        cardFlowerFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frameIntent = new Intent(getContext(), PhotoFrameActivity.class);
                frameIntent.putExtra(mFrameKey, "flowers");
                startActivity(frameIntent);
            }
        });

        cardLoveAnniversaryFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frameIntent = new Intent(getContext(), PhotoFrameActivity.class);
                frameIntent.putExtra(mFrameKey, "loveAnniversary");
                startActivity(frameIntent);
            }
        });

        cardWallFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frameIntent = new Intent(getContext(), PhotoFrameActivity.class);
                frameIntent.putExtra(mFrameKey, "wall");
                startActivity(frameIntent);
            }
        });

    }
}
