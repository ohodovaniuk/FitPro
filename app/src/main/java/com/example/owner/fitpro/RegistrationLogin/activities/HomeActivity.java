package com.example.owner.fitpro.RegistrationLogin.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.fitpro.R;
import com.example.owner.fitpro.RegistrationLogin.apis.APIs;
import com.example.owner.fitpro.RegistrationLogin.fragments.HomeFragment;
import com.example.owner.fitpro.RegistrationLogin.fragments.ProfileFragment;
import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.RegistrationLogin.services.AuthServices;

import java.util.function.Function;



public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    View hView;
    ImageView hProfileImageView;
    TextView hNameTV, hEmailTV;
    APIs apis;
    AuthServices authServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        this.hView = (View) navigationView.getHeaderView(0);
        this.hProfileImageView = (ImageView) this.hView.findViewById(R.id.hProfileImageView);
        this.hNameTV = (TextView) this.hView.findViewById(R.id.hNameTV);
        this.hEmailTV = (TextView) this.hView.findViewById(R.id.hEmailTV);

        this.apis = new APIs();
        this.authServices = new AuthServices();

        this.apis.getUsersAPI().observeCurrentUser(
                new Function<User, Void>() {
                    @Override
                    public Void apply(User user) {
                        Glide
                                .with(getApplicationContext())
                                .load(user.photoURL)
                                .into(hProfileImageView);
                        hNameTV.setText(user.name);
                        hEmailTV.setText(user.email);
                        if ( user.weight == null ) {
                            startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                        }
                        return null;
                    }
                },
                new Function<String, Void>() {
                    @Override
                    public Void apply(String s) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        return null;
                    }
                }
        );

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.homeConstraintLayout, new HomeFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_) {
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction()
                    .replace(R.id.homeConstraintLayout, new HomeFragment())
                    .commit();
        } else if (id == R.id.nav_profile) {
            // Handle the camera action
            fragmentManager.beginTransaction()
                    .replace(R.id.homeConstraintLayout, new ProfileFragment())
                    .commit();
        } else if (id == R.id.nav_logout) {
            this.authServices.logout(
                    new Function<Void, Void>() {
                        @Override
                        public Void apply(Void aVoid) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                            return null;
                        }
                    }, new Function<String, Void>() {
                        @Override
                        public Void apply(String s) {
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                            return null;
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
