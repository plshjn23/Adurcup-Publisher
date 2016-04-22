package com.adurcup.adurcuppublisher.Activities;

/**
 * Created by om on 4/17/2016.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import org.json.JSONArray;
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
 * Created by kshivang on 08/04/16.
 */
public class Home extends Activity {
    UserLocalStore userLocalStore;
    final Context context = this;
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    ImageView imgView, cameramorning, cameraevening;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath, address_loc = "null";
    double latitude, longitude;
    TextView tvupload, tvv, tvcredit, tv, tv_morning, tv_evening;
    private ProgressDialog loading;
    private ProgressBar spinner;
    int photo_bill = 0;
    Date d = null;
    String gma,lime;
    String imageFileName,typ="3";
    String dt;
    RequestQueue requestQueue;
    String new_lat,new_long;
    String split_one;
    String id = "1";
    Cursor c;
    int new_button = 0;
    String upload_date,current_date;

    static String Imagecredit = "http://api.adurcup.com/publisher/me/credits";
    static String Imageuploadurl = "http://api.adurcup.com/publisher/me/images";
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUpload = "http://www.learnhtml.provisor.in/android/uploadFile.php";

    static String strSDCardPathNamemorning = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUploadmorning = "http://www.learnhtml.provisor.in/android/uploadFilemorning.php";

    static String strSDCardPathNameevening = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUploadevening = "http://www.learnhtml.provisor.in/android/uploadFileevening.php";
    DBAdapter db = new DBAdapter(Home.this);

String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cameramorning = (ImageView) findViewById(R.id.morning_button);
        cameraevening = (ImageView) findViewById(R.id.imageViewevening);
        tv_morning = (TextView) findViewById(R.id.tvmorning);
        tv_evening = (TextView) findViewById(R.id.textView);
        tvv = (TextView)findViewById(R.id.tvv);
        tvupload = (TextView)findViewById(R.id.uploadingtask);
        userLocalStore = new UserLocalStore(this);

        User user = userLocalStore.getLoggedInUser();

        timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
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
//     Toast.makeText(getApplicationContext(), d.toString(), Toast.LENGTH_LONG).show();

                                        dt = d.toLocaleString();
                                        Log.d("timestamp", dt);
                                        String[] splited = dt.split("\\s+");

                                        split_one=splited[0];

                                        Log.d("Splited_String", split_one);
                                        dbmethod();
                                        return;

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });


        tv = (TextView) findViewById(R.id.textView2);
        tv.setText(user.contact);

        gma = user.apiKey;
        Log.d("hello", gma);
        tv.setVisibility(View.GONE);
        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());


        tvcredit = (TextView) findViewById(R.id.credit_total);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);



        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            //  getData();
            //   new getData(Home.this).execute();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet

            cameramorning.setVisibility(View.GONE);
            cameraevening.setVisibility(View.GONE);
            Toast.makeText(Home.this, "No Internet Connection, Please enable your internet connection", Toast.LENGTH_LONG).show();


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
        imgView = (ImageView) findViewById(R.id.imgview123);
        imgView.setVisibility(View.GONE);
        ImageView camera = (ImageView) findViewById(R.id.camera123);

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            GPSService mGPSService = new GPSService(Home.this);
            mGPSService.getLocation();

            if (mGPSService.isLocationAvailable == false) {

                // Here you can ask the user to try again, using return; for that
                Toast.makeText(Home.this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
                return;

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
            Toast.makeText(getBaseContext(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

        }



        cameraevening.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (address_loc != "null") {
                    photo_bill = 3;
                    typ="1";
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                    Toast.makeText(getBaseContext(), "Unable to get your location because of internet issue.", Toast.LENGTH_LONG).show();
                }


            }


        });


        cameramorning.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (address_loc != "null") {
                    photo_bill = 2;
                    typ = "2";
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                    Toast.makeText(getBaseContext(), "Unable to get your location because of internet issue.", Toast.LENGTH_LONG).show();
                }


            }


        });


        Button logout = (Button) findViewById(R.id.buttonlogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(Home.this, Login.class));
                finish();
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
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                    Toast.makeText(getBaseContext(), "Unable to get your location because of internet issue.", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private void dbmethod() {

        db.open();
        c = db.getContact(Integer.parseInt
                (id));
        if (c.moveToFirst()) {
            DisplayContact(c);
        }
        else {
            Toast.makeText(getBaseContext(), "No contact found",
                    Toast.LENGTH_LONG).show();

            //---Insert Contact---
            String date = "0";
            db.open();
            db.insertContact(date,split_one);
            db.close();
            Toast.makeText(getBaseContext(), "Inserted",
                    Toast.LENGTH_SHORT).show();
            Log.d("update time", date);
            Log.d("current date", split_one);

            db.close();

        }

        db.open();
        if (db.updateContact
                (Integer.parseInt(id), split_one)){

            Toast.makeText(getBaseContext(), "Update successful.", Toast.LENGTH_LONG).show();}
        else{
            Toast.makeText(getBaseContext(), "Update failed.",
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
            Log.d("campare123",current_date);
            Log.d("campare2",upload_date);


        }
        else{
            cameramorning.setVisibility(View.VISIBLE);
            Log.d("campare",current_date);
            Log.d("campare2", upload_date);
        }
    }

    private void DisplayContact(Cursor c) {
        // TODO Auto-generated method stub
        Toast.makeText(getBaseContext(),"id: " + c.getString(0) +
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
        Log.d("evening after after",imageFileName);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imgView.setImageBitmap(bitmap);

            isInternetPresent = cd.isConnectingToInternet();

            if (photo_bill == 1) {
                // check for Internet status
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsync(Home.this).execute();
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getBaseContext(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

                }
            } else if (photo_bill == 2) {
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsyncmorning(Home.this).execute();

                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getBaseContext(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

                }
            } else if (photo_bill == 3) {
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsyncevening(Home.this).execute();

                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getBaseContext(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

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


        // ProgressDialog
        //  private ProgressDialog mProgressDialog;


        public UploadAsync(Home activity) {

            //    mProgressDialog = new ProgressDialog(activity);

            // mProgressDialog.setMessage("Uploading please wait.....");

            //          mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            //        mProgressDialog.setCancelable(false);


        }


        protected void onPreExecute() {

            super.onPreExecute();

            //  mProgressDialog.show();

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

                //   mProgressDialog.dismiss();
                spinner.setVisibility(View.GONE);
                new ImageUpload().execute();



            } else {
                Toast.makeText(getBaseContext(), "Failed.", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
            }


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
            outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
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


        // ProgressDialog
        //  private ProgressDialog mProgressDialog;


        public UploadAsyncmorning(Home activity) {

            //    mProgressDialog = new ProgressDialog(activity);
            //    mProgressDialog.setMessage("Uploading please wait.....");
            //    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //    mProgressDialog.setCancelable(false);

        }


        protected void onPreExecute() {

            super.onPreExecute();

            //  mProgressDialog.show();

            spinner.setVisibility(View.VISIBLE);


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

                //   mProgressDialog.dismiss();
                spinner.setVisibility(View.GONE);
                new ImageUpload().execute();
                visibilityofbutton();

            } else {
                Toast.makeText(getBaseContext(), "Failed.", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
            }


        }

        private void visibilityofbutton() {

            db.open();
            if (db.updateContact1
                    (Integer.parseInt(id), split_one))

                Toast.makeText(getBaseContext(), "Update successful.", Toast.LENGTH_LONG).show();
            // Log.d("curent date after upload",);
            db.open();
            c = db.getContact(Integer.parseInt
                    (id));
            if (c.moveToFirst())
                DisplayContact(c);
            else
                Toast.makeText(getBaseContext(), "Update failed.",
                        Toast.LENGTH_LONG).show();
            db.close();
            cameramorning.setVisibility(View.GONE);

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
            outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
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
        // ProgressDialog
        //  private ProgressDialog mProgressDialog;
        public UploadAsyncevening(Home activity) {

            // mProgressDialog = new ProgressDialog(activity);
            // mProgressDialog.setMessage("Uploading please wait.....");
            // mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // mProgressDialog.setCancelable(false);
        }

        protected void onPreExecute() {

            super.onPreExecute();

            //  mProgressDialog.show();

            spinner.setVisibility(View.VISIBLE);


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
                spinner.setVisibility(View.GONE);

            } else {
                Toast.makeText(getBaseContext(), "Failed.", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
            }

        }

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
            outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
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

            requestQueue = Volley.newRequestQueue(getApplicationContext());

            StringRequest request = new StringRequest(Request.Method.POST, Imageuploadurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String error = object.getString("error");

                        switch (error) {
                            case "true":
                                String message = object.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case "false":
                                message = object.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Please fill all information", Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((error.networkResponse != null) && (error.networkResponse.statusCode == 400)) {
                        Toast.makeText(getApplicationContext(), "Some fields missing or incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Check network connection", Toast.LENGTH_SHORT).show();
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

            requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                            Toast.makeText(Home.this, "Internet is not working properly Please try again later.", Toast.LENGTH_LONG).show();

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

    public class getData extends AsyncTask<String, Void, Void> {



        // ProgressDialog
        //  private ProgressDialog mProgressDialog;


        public getData(Home activity) {

            loading = new ProgressDialog(activity);
            loading.setMessage("Fetching Data please wait.....");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.setCancelable(false);


        }


        protected void onPreExecute() {

            super.onPreExecute();


            loading.show();

        }


        @Override

        protected Void doInBackground(String... par) {


            String url = Config.DATA_URL;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    showJSON(response);
                }

                private void showJSON(String response) {
                    String name = "";
                    String address = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                        JSONObject collegeData = result.getJSONObject(0);
                        name = collegeData.getString(Config.KEY_NAME);
                        address = collegeData.getString(Config.KEY_ADDRESS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    int y_morning = Integer.parseInt(name);

                    int y_evening = Integer.parseInt(address);

                    if (y_morning == 0) {

                        cameramorning.setVisibility(View.GONE);
                        tv_morning.setVisibility(View.VISIBLE);


                    }
                    else if (y_morning == 1) {
                        cameramorning.setVisibility(View.VISIBLE);
                        tv_morning.setVisibility(View.GONE);
                        tvupload.setText("Upload morning bill image");
                    }

                    if (y_evening == 0) {

                        cameraevening.setVisibility(View.GONE);
                        tv_evening.setVisibility(View.VISIBLE);
                    }
                    else {
                        cameraevening.setVisibility(View.VISIBLE);
                        tv_evening.setVisibility(View.GONE);
                        tvupload.setText("Upload evening bill image");
                    }
                    new GetCredits().execute();
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Home.this, "Please check you net connection it's not working.", Toast.LENGTH_LONG).show();
                            cameramorning.setVisibility(View.GONE);
                            cameraevening.setVisibility(View.GONE);
                            loading.dismiss();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(Home.this);

            requestQueue.add(stringRequest);
            return null;
        }


        protected void onPostExecute(Void unused) {



            isInternetPresent = cd.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {
                loading.dismiss();
            } else {
                Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_LONG).show();
            }


        }


    }
}
