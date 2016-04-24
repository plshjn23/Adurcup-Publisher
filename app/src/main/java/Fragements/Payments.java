package Fragements;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adurcup.adurcuppublisher.R;

/**
 * Created by om on 4/23/2016.
 */
public class Payments extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_payments, container, false);
        getActivity().setTitle("Accomodation");





        return rootView;


    }

}
