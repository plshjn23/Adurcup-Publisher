package com.adurcup.adurcuppublisher.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adurcup.adurcuppublisher.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by om on 4/26/2016.
 */
public class ImageThreeFragment extends Fragment {


    Button _btn1234;

    public ImageThreeFragment() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_image_three, container, false);


    }


}
