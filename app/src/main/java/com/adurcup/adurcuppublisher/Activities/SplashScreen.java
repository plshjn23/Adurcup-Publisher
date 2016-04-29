package com.adurcup.adurcuppublisher.Activities;

/**
 * Created by om on 4/17/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.adurcup.adurcuppublisher.R;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kshivang on 08/04/16.
 */
public class SplashScreen extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(SplashScreen.this);
        // Connection detector class

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
                    .make(coordinatorLayout, "Internet is not connected!", Snackbar.LENGTH_INDEFINITE);
            {

                snackbar.setActionTextColor(Color.RED);
                snackbar.show();

            }
            }
        }

    }
















