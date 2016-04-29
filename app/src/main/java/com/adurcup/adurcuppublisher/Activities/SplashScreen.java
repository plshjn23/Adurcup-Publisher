package com.adurcup.adurcuppublisher.Activities;

/**
 * Created by om on 4/17/2016.
 */
import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.adurcup.adurcuppublisher.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kshivang on 08/04/16.
 */
public class SplashScreen extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        // Connection detector class

        ConnectionDetector cd;
        cd = new ConnectionDetector(SplashScreen.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            new Timer().schedule(new TimerTask() {
                public void run() {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                }
            }, 2000);

        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Internet is not connected!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (isInternetPresent) {
                                // Internet Connection is Present
                                // make HTTP requests
                                startActivity(new Intent(SplashScreen.this, Login.class));

                            } else {
                                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "No internet Connection", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }

                        }

                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }





    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

}











