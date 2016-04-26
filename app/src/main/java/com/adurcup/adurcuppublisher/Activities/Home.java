package com.adurcup.adurcuppublisher.Activities;

/**
 * Created by om on 4/17/2016.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.adurcup.adurcuppublisher.ContentProvider.User;
import com.adurcup.adurcuppublisher.ContentProvider.UserLocalStore;
import com.adurcup.adurcuppublisher.R;

import Fragements.Payments;
import Fragements.Glance;

public class Home extends AppCompatActivity
               implements NavigationView.OnNavigationItemSelectedListener {

    UserLocalStore userLocalStore;
    int backpressed = 0;
    Handler hand = new Handler();
    FragmentManager fm = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

        userLocalStore = new UserLocalStore(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fm.beginTransaction().replace(R.id.Frame_layout, new Glance()).commit();
    }

    @Override
    public void onBackPressed() {
        if (backpressed == 0) {
            fm.beginTransaction().replace(R.id.Frame_layout, new Glance()).commit();
            backpressed = 1;
        } else if (backpressed == 1) {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        backpressed = 0;
        final FragmentManager fm = getFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();

        if (id == R.id.nav_Glance) {
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    fm.beginTransaction().replace(R.id.Frame_layout, new Glance()).commit();

                }
            }, 600);


        } else if (id == R.id.nav_Payments) {
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    fm.beginTransaction().replace(R.id.Frame_layout, new Payments()).commit();
                //    moveTaskToBack (true);
                }
            }, 300);


        } else if (id == R.id.nav_logout) {

            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            startActivity(new Intent(Home.this, Login.class));
            finish();


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}