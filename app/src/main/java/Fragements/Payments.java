package Fragements;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.adurcup.adurcuppublisher.Activities.ConnectionDetector;
import com.adurcup.adurcuppublisher.ContentProvider.User;
import com.adurcup.adurcuppublisher.ContentProvider.UserLocalStore;
import com.adurcup.adurcuppublisher.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by om on 4/23/2016.
 */
public class Payments extends Fragment {
    UserLocalStore userLocalStore;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    static String ImageRecycle = "http://api.adurcup.com/publisher/me/images";
    RequestQueue requestQueue;
    String gma;
    private ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_payments, container, false);
        getActivity().setTitle("Payments");
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        userLocalStore = new UserLocalStore(getActivity());
        User user = userLocalStore.getLoggedInUser();

listView = (ListView)rootView.findViewById(R.id.listView);
            // Internet Connection is Present
            // make HTTP requests
            new GetImagesRecycler().execute();


        gma = user.apiKey;
        Log.d("hello", gma);

        return rootView;


    }

    public class GetImagesRecycler extends AsyncTask<Void, Void, Void> {




        @Override
        protected Void doInBackground(Void... params) {

            requestQueue = Volley.newRequestQueue(getActivity());

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, ImageRecycle, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("credit", response.toString());
                            String example = response.toString();
                            showJSON(example);
                            Log.d("example",example);
                           // example = example.substring(example.lastIndexOf(":") + 1);
                           // String str = example.substring(0, example.length() - 1);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Internet is not working properly Please try again later.", Toast.LENGTH_LONG).show();

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    Log.d("hello_", gma);
                    headers.put("Authorization", gma);
                    return headers;
                }


            };

            jsObjRequest.setShouldCache(false);
            requestQueue.add(jsObjRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void showJSON(String json) {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON();
        CustomList cl = new CustomList(getActivity(), ParseJSON.approves,ParseJSON.added_credits,ParseJSON.types,ParseJSON.image_srcs,ParseJSON.time_stamps);
        listView.setAdapter(cl);
    }
}