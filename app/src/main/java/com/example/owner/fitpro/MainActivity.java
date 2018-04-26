package com.example.owner.fitpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.Chat.ui.fragments.UsersFragment;
import com.example.owner.fitpro.RegistrationLogin.ProfileActivity_;
import com.example.owner.fitpro.RegistrationLogin.activities.EditProfileActivity;
import com.example.owner.fitpro.RegistrationLogin.activities.LoginActivity;
import com.example.owner.fitpro.RegistrationLogin.apis.APIs;
import com.example.owner.fitpro.RegistrationLogin.services.AuthServices;
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.ExerciseCategories;
import com.example.owner.fitpro.WorkoutAndExercise.Workout.WorkoutCategories;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    View hView;
    ImageView hProfileImageView;
    TextView hNameTV, hEmailTV;
    APIs apis;
    AuthServices authServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Return to specific tab
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String data;

        if(bundle != null){
            data = bundle.getString("tabSelected");
            Log.d("TAB", data);


            if (data.contains("0")) { // Workouts
                viewPager.setCurrentItem(0, true);
            }

            if (data.contains("1")) { // Exercice
                viewPager.setCurrentItem(1, true);
            }

            if (data.contains("2")) { //Chat
                viewPager.setCurrentItem(2, true);
            }
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
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
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fragment) {
            super(fragment);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }





    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {



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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
       adapter.addFragment(new WorkoutCategories(), "Workouts");
       adapter.addFragment(new ExerciseCategories(), "Exercises");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            adapter.addFragment(new UsersFragment(), "Chat");
            Log.d("STATE", "USER IS LOGGED");
        }else {
            // otherwise redirect the user to login activity
            //LoginActivity.startIntent(RegisterActivity.this);
            Log.d("STATE", "USER IS NOT! LOGGED");

        }


        viewPager.setAdapter(adapter);
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("STATE", String.valueOf(id));

        //android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home) {
            Log.d("STATE", "CLICKED PROFILE");

            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            myIntent.putExtra("tabSelected", "0");  // pass your values and retrieve them in the other Activity using keyName
            startActivity(myIntent);


            mDrawerLayout.closeDrawers();


        } else if (id == R.id.nav_profile) {


            Intent myIntent = new Intent(getApplicationContext(), ProfileActivity_.class);
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            mDrawerLayout.closeDrawers();

            startActivity(myIntent);

        }else if (id == R.id.nav_logout) {
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


        return true;
    }

}
