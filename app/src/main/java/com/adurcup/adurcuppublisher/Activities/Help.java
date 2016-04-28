package com.adurcup.adurcuppublisher.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adurcup.adurcuppublisher.R;

/**
 * Created by om on 4/27/2016.
 */
public class Help extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        getActivity().setTitle("Help");
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();



        // Internet Connection is Present
        // make HTTP requests



        return rootView;


    }

}
