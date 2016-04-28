package com.adurcup.adurcuppublisher.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adurcup.adurcuppublisher.ContentProvider.User;
import com.adurcup.adurcuppublisher.ContentProvider.UserLocalStore;
import com.adurcup.adurcuppublisher.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by om on 4/27/2016.
 */
public class FeedBack extends Fragment implements AdapterView.OnItemSelectedListener {


    UserLocalStore userLocalStore;
    Spinner spinnerOsversions;
    TextView selVersion;
    private String[] state = { "Select Subject", "Subject-1", "Subject-2", "Subject-3",
            "Subject-4", "Subject-5", "Subject-6", "Subject-7",
            "Other" };


    EditText msg,subject;
    Button btn_send;
    ProgressBar progressBar;
    RequestQueue requestQueue;
    String feedbackUrl,subject_feed,message_feed;
    String gma;
    String selState;
    int type_feedback = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        getActivity().setTitle("FeedBack");
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        spinnerOsversions = (Spinner) rootView.findViewById(R.id.osversions);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOsversions.setAdapter(adapter_state);
        spinnerOsversions.setOnItemSelectedListener(this);
        feedbackUrl = "http://api.adurcup.com/publisher/me/feedback";
        msg = (EditText) rootView.findViewById(R.id.feed_msg);

        subject = (EditText) rootView.findViewById(R.id.feed_sub);
        subject.setVisibility(View.GONE);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_feedback);
        progressBar.setVisibility(View.GONE);
        userLocalStore = new UserLocalStore(getActivity());
        User user = userLocalStore.getLoggedInUser();
        gma = user.apiKey;
        Log.d("hello", gma);
         // Internet Connection is Present
        // make HTTP requests
        btn_send = (Button) rootView.findViewById(R.id.sendfeedback);



        return rootView;


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        spinnerOsversions.setSelection(position);
        selState = (String) spinnerOsversions.getSelectedItem();

        if(selState == "Other"){
            subject.setVisibility(View.VISIBLE);
       type_feedback = 2;
            return;
        }
        else {
            subject_feed = selState;
        }


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message_feed = msg.getText().toString();


                if(type_feedback == 2){

                    Log.d("subject-else2",subject_feed);
                    Log.d("message-else2",message_feed);
                    progressBar.setVisibility(View.VISIBLE);
                    subject_feed = subject.getText().toString();
                    new sendfeedback_feed().execute();
                }
else{
                    Log.d("subject-else",subject_feed);
                    Log.d("message-else",message_feed);
                    progressBar.setVisibility(View.VISIBLE);
                    new sendfeedback().execute();}

            }
        });




    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public class sendfeedback extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, feedbackUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String error = object.getString("error");

                        switch (error) {
                            case "true":
                                String message = object.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case "false":
                                message = object.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "Please fill all information", Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((error.networkResponse != null) && (error.networkResponse.statusCode == 400)) {
                        Toast.makeText(getActivity(), "Some fields missing or incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Check network connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    Log.d("hello_", gma);
                    headers.put("Authorization", gma);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("subject", subject_feed);
                    parameters.put("message", message_feed);
                    Log.d("Volley", subject_feed);
                    Log.d("message",message_feed);
                    return parameters;
                }
            };
            request.setShouldCache(false);
            requestQueue.add(request);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }
    public class sendfeedback_feed extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {



            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, feedbackUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String error = object.getString("error");

                        switch (error) {
                            case "true":
                                String message = object.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case "false":
                                message = object.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "Please fill all information", Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((error.networkResponse != null) && (error.networkResponse.statusCode == 400)) {
                        Toast.makeText(getActivity(), "Some fields missing or incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Check network connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    Log.d("hello_", gma);
                    headers.put("Authorization", gma);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("subject", subject_feed);
                    parameters.put("message", message_feed);
                    Log.d("Volley", subject_feed);
                    Log.d("message",message_feed);
                    return parameters;
                }
            };
            request.setShouldCache(false);
            requestQueue.add(request);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }
}
