package com.rohitneel.photopixelpro.photoframe.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.helper.SessionManager;
import com.rohitneel.photopixelpro.photoframe.adapters.AdapterMyCreations;
import com.rohitneel.photopixelpro.photoframe.model.ModelclassDownloadedImages;
import com.rohitneel.photopixelpro.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentCreationsList extends Fragment {
    View view;
    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout rl_Creation_list_empty_list;
    TextView txtImageNotFound;
    private SessionManager mSession;

    ArrayList<ModelclassDownloadedImages> filenames = new ArrayList<ModelclassDownloadedImages>();
    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Photo Pixel Pro";
    AdapterMyCreations adapterMyCreations;
    ArrayList<ModelclassDownloadedImages> dataCombined = null; //You are passing this null


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_creations_list, container, false);
        context = container.getContext();
        recyclerView = view.findViewById(R.id.rv_downloadimages);
        rl_Creation_list_empty_list = view.findViewById(R.id.rl_Creation_list_empty_list);
        txtImageNotFound = view.findViewById(R.id.tv1);

        mSession = new SessionManager(getContext());

        if(mSession.loadState()){
            txtImageNotFound.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        getMyVideoList();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getMyVideoList() {
        filenames.clear();
        if(path!=null) {
            File directory = new File(path);
            //File[] files = directory.listFiles();
            ArrayList<ModelclassDownloadedImages> dataCombined = new ArrayList<ModelclassDownloadedImages>();
            if (directory.exists()) {
                for (File filePath : Objects.requireNonNull(directory.listFiles())) {
                    if (filePath.getPath().contains("PhotoFrame_")) {
                        String file_name = filePath.getName();
                        String file_path = filePath.getPath();
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file_path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                        // you can store name to arraylist and use it later
                        filenames.add(new ModelclassDownloadedImages(file_name, file_path, thumb));
                    }
                }
                dataCombined.addAll(filenames);

                if (dataCombined.size() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapterMyCreations = new AdapterMyCreations(context, dataCombined);
                    recyclerView.setAdapter(adapterMyCreations);
                } else {
                    rl_Creation_list_empty_list.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            } else {
                rl_Creation_list_empty_list.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }


}
