package Fragements;



import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adurcup.adurcuppublisher.Activities.ConnectionDetector;
import com.adurcup.adurcuppublisher.Activities.DBAdapter;
import com.adurcup.adurcuppublisher.Activities.DBAdapterevening;
import com.adurcup.adurcuppublisher.Activities.GPSService;
import com.adurcup.adurcuppublisher.Activities.Login;
import com.adurcup.adurcuppublisher.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import java.util.Date;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.adurcup.adurcuppublisher.ContentProvider.User;
import com.adurcup.adurcuppublisher.ContentProvider.UserLocalStore;
import com.adurcup.adurcuppublisher.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by om on 4/23/2016.
 */
public class Glance extends Fragment {
    UserLocalStore userLocalStore;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    ImageView imgView, cameramorning, cameraevening,tbtn;
    static final int REQUEST_TAKE_PHOTO = 1;
    double latitude, longitude;
    TextView tvupload, tvv, tvcredit, tv, tv_morning, tv_evening;
    private ProgressBar spinner,spinnermorning,spinnerevening;
    int photo_bill = 0;
    Date d = null;
    String gma,lime,imageFileName,typ="3",dt,new_lat,new_long,split_one,id = "1",upload_date,current_date,upload_dateevening,current_dateevening,mCurrentPhotoPath, address_loc = "null";
    RequestQueue requestQueue;
    Cursor c,c1;
    private ProgressDialog mProgressDialog;
    int new_button = 0;
    static String Imagecredit = "http://api.adurcup.com/publisher/me/credits";
    static String Imageuploadurl = "http://api.adurcup.com/publisher/me/images";
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUpload = "http://www.learnhtml.provisor.in/android/uploadFile.php";
    static String strSDCardPathNamemorning = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUploadmorning = "http://www.learnhtml.provisor.in/android/uploadFilemorning.php";
    static String strSDCardPathNameevening = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUploadevening = "http://www.learnhtml.provisor.in/android/uploadFileevening.php";





    @Nullable
    public static int position;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.activity_main_glance,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        final DrawerLayout drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        cameramorning = (ImageView)rootView.findViewById(R.id.morning_button);
        tbtn = (ImageView)rootView.findViewById(R.id.togglebtn);
        cameramorning.setVisibility(View.GONE);
        cameraevening = (ImageView)rootView.findViewById(R.id.imageViewevening);
        cameraevening.setVisibility(View.GONE);
        tv_morning = (TextView)rootView.findViewById(R.id.tvmorning);
        tv_morning.setVisibility(View.GONE);
        tv_evening = (TextView)rootView.findViewById(R.id.textView);
        tv_evening.setVisibility(View.GONE);
        tvv = (TextView)rootView.findViewById(R.id.tvv);
        tvupload = (TextView)rootView.findViewById(R.id.uploadingtask);
        userLocalStore = new UserLocalStore(getActivity());
        User user = userLocalStore.getLoggedInUser();
        Log.d("mobile",user.contact);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Demo");
        query2.getInBackground("QNGkwKjpjt", new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    gameScore.put("Demo", "Demo");
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Demo");
                            query1.getInBackground("QNGkwKjpjt", new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {

                                    if (e == null) {
                                        d = object.getUpdatedAt();
                                    }
                                    if (d != null) {
                                        dt = d.toLocaleString();
                                        Log.d("timestamp", dt);
                                        String[] splited = dt.split("2016");

                                        split_one=splited[0];

                                        Log.d("Splited_String", split_one);
                                        dbmethod();
                                        dbmethodevening();
                                        return;
                                    } else {
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });


        tv = (TextView) rootView.findViewById(R.id.textView2);
        tv.setText(user.contact);

        gma = user.apiKey;
        Log.d("hello", gma);
        tv.setVisibility(View.GONE);
        // creating connection detector class instance
        cd = new ConnectionDetector(getActivity());


        tvcredit = (TextView) rootView.findViewById(R.id.credit_total);
        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        spinnermorning = (ProgressBar)rootView.findViewById(R.id.progressBarFirstBill);
        spinnerevening = (ProgressBar)rootView.findViewById(R.id.progressBarLastBill);

        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            new GetCredits().execute();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Toast.makeText(getActivity(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

        }

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // *** Create Folder
        createFolder();
        createFoldermorning();
        createFolderevening();
        // *** ImageView
        imgView = (ImageView)rootView.findViewById(R.id.imgview123);
        imgView.setVisibility(View.GONE);
        ImageView camera = (ImageView)rootView.findViewById(R.id.camera123);

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            GPSService mGPSService = new GPSService(getActivity());
            mGPSService.getLocation();

            if (mGPSService.isLocationAvailable == false) {
                // Here you can ask the user to try again, using return; for that
                Toast.makeText(getActivity(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
                // return;
                // Or you can continue without getting the location, remove the return; above and uncomment the line given below
                // address = "Location not available";
            } else {

                // Getting location co-ordinates
                latitude = mGPSService.getLatitude();
                longitude = mGPSService.getLongitude();
                address_loc = mGPSService.getLocationAddress();
                new_lat =  Double.toString(latitude);
                new_long =  Double.toString(longitude);
            }



            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Toast.makeText(getActivity(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

        }



        cameraevening.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (address_loc != "null") {
                    photo_bill = 3;
                    typ = "1";
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFileevening();
                        } catch (IOException ex) {
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to get your location because of internet issue.", Toast.LENGTH_LONG).show();
                }


            }


        });

        tbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);

            }


        });


        cameramorning.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (address_loc != "null") {
                    photo_bill = 2;
                    typ = "2";
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFilemorning();
                        } catch (IOException ex) {
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                        }
                    }


                } else {
                    Toast.makeText(getActivity(), "Unable to get your location because of internet issue.", Toast.LENGTH_LONG).show();
                }


            }


        });


        // *** Take Photo
        // Perform action on click
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (address_loc != "null") {
                    photo_bill = 1;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to get your location because of internet issue.", Toast.LENGTH_LONG).show();
                }
            }

        });



        return rootView;

    }

    private void dbmethodevening() {
        DBAdapterevening db1 = new DBAdapterevening(getActivity());
        db1.open();
        c1 = db1.getContactevening(Integer.parseInt
                (id));
        if (c1.moveToFirst()) {
            DisplayContactevening(c1);
        }
        else {
            Toast.makeText(getActivity(), "No contact found",
                    Toast.LENGTH_LONG).show();

            //---Insert Contact---
            String date1 = "0";
            db1.open();
            db1.insertContactevening(date1, split_one);
            db1.close();
            Toast.makeText(getActivity(), "Inserted",
                    Toast.LENGTH_SHORT).show();
            Log.d("update date evening", date1);
            Log.d("current date evening", split_one);

            db1.close();

        }

        db1.open();
        if (db1.updateContactevening
                (Integer.parseInt(id), split_one)){

            Toast.makeText(getActivity(), "Update successful.", Toast.LENGTH_LONG).show();}
        else{
            Toast.makeText(getActivity(), "Update failed.",
                    Toast.LENGTH_LONG).show();}
        db1.close();


        db1.open();
        c1 = db1.getContactevening(Integer.parseInt
                (id));
        if (c1.moveToFirst()) {
            DisplayContactevening(c1);
        }



        if(current_dateevening.equals(upload_dateevening)){
            cameraevening.setVisibility(View.GONE);
            tv_evening.setVisibility(View.VISIBLE);
            spinnerevening.setVisibility(View.GONE);
            Log.d("campare123evening",current_dateevening);
            Log.d("campare2evening",upload_dateevening);


        }
        else{
            cameraevening.setVisibility(View.VISIBLE);
            spinnerevening.setVisibility(View.GONE);
            tv_evening.setVisibility(View.GONE);
            Log.d("campareevening",current_dateevening);
            Log.d("campare2evening", upload_dateevening);
        }
    }

    private void dbmethod() {
        DBAdapter db = new DBAdapter(getActivity());
        db.open();
        c = db.getContact(Integer.parseInt
                (id));
        if (c.moveToFirst()) {
            DisplayContact(c);
        }
        else {
            Toast.makeText(getActivity(), "No contact found",
                    Toast.LENGTH_LONG).show();

            //---Insert Contact---
            String date = "0";
            db.open();
            db.insertContact(date,split_one);
            db.close();
            Toast.makeText(getActivity(), "Inserted",
                    Toast.LENGTH_SHORT).show();
            Log.d("update time", date);
            Log.d("current date", split_one);

            db.close();

        }

        db.open();
        if (db.updateContact
                (Integer.parseInt(id), split_one)){

            Toast.makeText(getActivity(), "Update successful.", Toast.LENGTH_LONG).show();}
        else{
            Toast.makeText(getActivity(), "Update failed.",
                    Toast.LENGTH_LONG).show();}
        db.close();


        db.open();
        c = db.getContact(Integer.parseInt
                (id));
        if (c.moveToFirst()) {
            DisplayContact(c);
        }



        if(current_date.equals(upload_date)){
            cameramorning.setVisibility(View.GONE);
            tv_morning.setVisibility(View.VISIBLE);
            spinnermorning.setVisibility(View.GONE);
            Log.d("campare123",current_date);
            Log.d("campare2",upload_date);


        }
        else{
            cameramorning.setVisibility(View.VISIBLE);
            spinnermorning.setVisibility(View.GONE);
            tv_morning.setVisibility(View.GONE);
            Log.d("campare",current_date);
            Log.d("campare2", upload_date);
        }
    }

    private void DisplayContact(Cursor c) {
        // TODO Auto-generated method stub
        Toast.makeText(getActivity(), "id: " + c.getString(0) +
                        "\n" +"Uploading Date: " + c.getString(1) + "\n" +
                        "Current Date: " + c.getString(2),
                Toast.LENGTH_LONG).show();
        upload_date = c.getString(1);
        current_date = c.getString(2);
        Log.d("upload",upload_date);
        Log.d("current",current_date);
        new_button = 1;

        if(new_button == 1){
            cameramorning.setVisibility(View.GONE);
        }


    }


    private File createImageFileevening() throws IOException {

        // Create an image file name
        String user123 = tv.getText().toString();
        imageFileName = d+"lat"+latitude+"lon"+longitude+address_loc+user123+"even";
        Log.d("evening",imageFileName);
        File storageDir = new File(strSDCardPathNamemorning);
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */

                storageDir /* directory */


        );

        Log.d("after evening",imageFileName);
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("evening after after", imageFileName);
        String example = mCurrentPhotoPath;
        lime = (example.substring(example.lastIndexOf("/") + 1));
        Log.d("new path new",lime);
        return image;

    }


    private void createFolderevening() {

        File folder = new File(strSDCardPathNameevening);
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }
    }

    private File createImageFilemorning() throws IOException {

        // Create an image file name
        String user123 = tv.getText().toString();
        imageFileName = d+"lat"+latitude+"lon"+longitude+address_loc+user123+"morn";

        File storageDir = new File(strSDCardPathNamemorning);
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */

                storageDir /* directory */
        );


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("new path",mCurrentPhotoPath);
        String example = mCurrentPhotoPath;
        lime = (example.substring(example.lastIndexOf("/") + 1));
        Log.d("new path new",lime);
        return image;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imgView.setImageBitmap(bitmap);

            isInternetPresent = cd.isConnectingToInternet();

            if (photo_bill == 1) {
                // check for Internet status
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsync(Glance.this).execute();

                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getActivity(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

                }
            } else if (photo_bill == 2) {
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsyncmorning(Glance.this).execute();

                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getActivity(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

                }
            } else if (photo_bill == 3) {
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsyncevening(Glance.this).execute();

                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getActivity(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

                }
            }


        }
    }



    private void createFoldermorning() {
        File folder = new File(strSDCardPathNamemorning);
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }
    }

    public static void clearFoldermorning() {
        File dir = new File(strSDCardPathNamemorning);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int n = 0; n < children.length; n++) {
                new File(dir, children[n]).delete();
            }
        }
    }




    // Upload Image in Background
    public class UploadAsync extends AsyncTask<String, Void, Void> {
        public UploadAsync(Glance activity) {

        }



        protected void onPreExecute() {
            showProgressBar();
            super.onPreExecute();





        }

        private void showProgressBar() {

            mProgressDialog = ProgressDialog.show(getActivity(), "Please Wait","Uploading Image ...");
            spinner.setVisibility(View.VISIBLE);
        }


        @Override

        protected Void doInBackground(String... par) {




            // *** Upload all file to Server

            File file = new File(strSDCardPathName);

            File[] files = file.listFiles();

            for (File sfil : files) {

                if (sfil.isFile()) {

                    uploadFiletoServer(sfil.getAbsolutePath(), strURLUpload);

                }

            }


            //*** Clear Folder

            clearFolder();


            return null;

        }


        protected void onPostExecute(Void unused) {


            isInternetPresent = cd.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {


                new ImageUpload().execute();

                spinner.setVisibility(View.GONE);
                mProgressDialog.dismiss();

            } else {

                Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
                mProgressDialog.dismiss();
            }
        }

        private void hideProgrssbar() {


        }
    }


    private File createImageFile() throws IOException {

        // Create an image file name
        String user123 = tv.getText().toString();
        imageFileName = d+"lat"+latitude+"lon"+longitude+address_loc+user123;
        File storageDir = new File(strSDCardPathName);
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        String example = mCurrentPhotoPath;
        lime = (example.substring(example.lastIndexOf("/") + 1));
        Log.d("new path new",lime);
        return image;
    }

    public static boolean uploadFiletoServer(String strSDPath, String strUrlServer) {
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int resCode = 0;
        String resMessage = "";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            File file = new File(strSDPath);
            if (!file.exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));
            URL url = new URL(strUrlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // Response Code and Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();
                resMessage = new String(result);
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception ex) {
            // Exception handling
            return false;
        }
    }

    public static void createFolder() {
        File folder = new File(strSDCardPathName);
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }
    }

    public static void clearFolder() {
        File dir = new File(strSDCardPathName);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int n = 0; n < children.length; n++) {
                new File(dir, children[n]).delete();
            }
        }
    }

    public class UploadAsyncmorning extends AsyncTask<String, Void, Void> {
        public UploadAsyncmorning(Glance activity) {
        }
        protected void onPreExecute() {
            showprogressbarmorning();
            super.onPreExecute();

        }

        private void showprogressbarmorning() {
            mProgressDialog = ProgressDialog.show(getActivity(), "Please Wait","Uploading Image ...");
            spinnermorning.setVisibility(View.VISIBLE);
            cameramorning.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(String... par) {
            // *** Upload all file to Server
            File file = new File(strSDCardPathNamemorning);
            File[] files = file.listFiles();
            for (File sfil : files) {
                if (sfil.isFile()) {
                    uploadFiletoServermorning(sfil.getAbsolutePath(), strURLUploadmorning);
                }
            }
            //*** Clear Folder
            clearFoldermorning();
            return null;
        }

        protected void onPostExecute(Void unused) {
            isInternetPresent = cd.isConnectingToInternet();
            // check for Internet status
            if (isInternetPresent) {
                new ImageUpload().execute();
                visibilityofbutton();
                hideprogressbarmorning();

            } else {
                Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
                spinnermorning.setVisibility(View.GONE);
                mProgressDialog.dismiss();
                cameramorning.setVisibility(View.VISIBLE);
            }
        }

        private void hideprogressbarmorning() {
            mProgressDialog.dismiss();
            spinnermorning.setVisibility(View.GONE);
        }

        private void visibilityofbutton() {
            DBAdapter db = new DBAdapter(getActivity());
            db.open();
            if (db.updateContact1
                    (Integer.parseInt(id), split_one))
                Toast.makeText(getActivity(), "Update successful.", Toast.LENGTH_LONG).show();
            db.open();
            c = db.getContact(Integer.parseInt(id));
            if (c.moveToFirst()){
                DisplayContact(c);}
            else{
                Toast.makeText(getActivity(), "Update failed.", Toast.LENGTH_LONG).show();
                cameramorning.setVisibility(View.VISIBLE);}
            db.close();
            cameramorning.setVisibility(View.GONE);
            tv_morning.setVisibility(View.VISIBLE);
        }
    }



    public static boolean uploadFiletoServermorning(String strSDPath, String strUrlServer) {
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int resCode = 0;
        String resMessage = "";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            File file = new File(strSDPath);
            if (!file.exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));
            URL url = new URL(strUrlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // Response Code and Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();
                resMessage = new String(result);
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception ex) {
            // Exception handling
            return false;
        }
    }

    public class UploadAsyncevening extends AsyncTask<String, Void, Void> {
        public UploadAsyncevening(Glance activity) {
        }
        protected void onPreExecute() {
            showprogressbarevening();
            super.onPreExecute();

        }

        private void showprogressbarevening() {
            mProgressDialog = ProgressDialog.show(getActivity(), "Please Wait","Uploading Image ...");
            spinnerevening.setVisibility(View.VISIBLE);
            cameraevening.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(String... par) {
            // *** Upload all file to Server
            File file = new File(strSDCardPathNameevening);
            File[] files = file.listFiles();
            for (File sfil : files) {
                if (sfil.isFile()) {
                    uploadFiletoServerevening(sfil.getAbsolutePath(), strURLUploadevening);
                }
            }
            //*** Clear Folder
            clearFolderevening();
            return null;
        }
        protected void onPostExecute(Void unused) {
            isInternetPresent = cd.isConnectingToInternet();
            // check for Internet status
            if (isInternetPresent) {
                new ImageUpload().execute();
                //   mProgressDialog.dismiss();
                visibilityofbuttonevening();
                hideprogressbarevening();

            } else {
                Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();
                spinnerevening.setVisibility(View.GONE);
                cameraevening.setVisibility(View.VISIBLE);
            }
        }

        private void hideprogressbarevening() {
            mProgressDialog.dismiss();
            spinnerevening.setVisibility(View.GONE);
            tv_evening.setVisibility(View.VISIBLE);
        }


    }

    private void visibilityofbuttonevening() {
        DBAdapterevening db1 = new DBAdapterevening(getActivity());
        db1.open();
        if (db1.updateContact1evening(Integer.parseInt(id), split_one))
            Toast.makeText(getActivity(), "Update successful.", Toast.LENGTH_LONG).show();
        db1.open();
        c1 = db1.getContactevening(Integer.parseInt
                (id));
        if (c1.moveToFirst()){
            DisplayContactevening(c1);}
        else{
            Toast.makeText(getActivity(), "Update failed.",
                    Toast.LENGTH_LONG).show();
            cameraevening.setVisibility(View.VISIBLE);
        }
        db1.close();
        cameraevening.setVisibility(View.GONE);
    }

    private void DisplayContactevening(Cursor c1) {
        Toast.makeText(getActivity(), "id: " + c1.getString(0) + "\n" +"Uploading Date Evening: " + c1.getString(1) + "\n" + "Current Date evening: " + c1.getString(2), Toast.LENGTH_LONG).show();
        upload_dateevening = c1.getString(1);
        current_dateevening = c1.getString(2);
        Log.d("upload_evening",upload_date);
        Log.d("current_evening",current_date);
        cameraevening.setVisibility(View.GONE);
    }


    public static boolean uploadFiletoServerevening(String strSDPath, String strUrlServer) {
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int resCode = 0;
        String resMessage = "";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            File file = new File(strSDPath);
            if (!file.exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));
            URL url = new URL(strUrlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // Response Code and Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();
                resMessage = new String(result);
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception ex) {
            // Exception handling
            return false;
        }
    }

    private void clearFolderevening() {
        File dir = new File(strSDCardPathNameevening);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int n = 0; n < children.length; n++) {
                new File(dir, children[n]).delete();
            }
        }
    }
    public class ImageUpload extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, Imageuploadurl, new Response.Listener<String>() {
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
                    parameters.put("image_src", lime);
                    parameters.put("type", typ);
                    parameters.put("time_stamp", dt);
                    parameters.put("location", address_loc);
                    parameters.put("latitude", new_lat);
                    parameters.put("longitude", new_long);
                    Log.d("Volley" , lime);
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
        }
    }

    public class GetCredits extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            requestQueue = Volley.newRequestQueue(getActivity());

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET,Imagecredit , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("credit",response.toString());
                            String example = response.toString();
                            example = example.substring(example.lastIndexOf(":") + 1);
                            String  str = example.substring(0, example.length() - 1);
                            tvv.setText(str);
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


}