package com.example.owner.fitpro.Chat.ui.activities;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.owner.fitpro.Chat.ui.fragments.ChatFragment;
import com.example.owner.fitpro.Chat.utility.Constants;
import com.example.owner.fitpro.MainActivity;
import com.example.owner.fitpro.R;

public class ChatActivity extends AppCompatActivity {

    private TextView name;

    // Back arrow button on the left corner.
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        // Redirect to the home page to the Chat Tab
        myIntent.putExtra("tabSelected", "2");
        startActivity(myIntent);
        return true;
    }

    // Override the android back button.
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.putExtra("tabSelected", "2");  // Redirect to the home page to the Chat Tab
        startActivity(myIntent);

    }

    // Start the chat Activity and get the values
    public static void startActivity(Context context,
                                     String receiver,
                                     String receiverUid,
                                     String firebaseToken,
                                     String PhotoURL) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.putExtra(Constants.ARG_FIREBASE_PHOTO, PhotoURL);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bindViews();
        init();
    }

    // Set the TextView
    private void bindViews() {
        name = (TextView) findViewById(R.id.chat_username);
    }

    private void init() {
        // Creating New Variable to remove @example.com
        String title;
        title = getIntent().getExtras().getString(Constants.ARG_RECEIVER);
        String cap = title.substring(0, 1).toUpperCase() + title.substring(1);
        name.setText(cap.replace("@example.com", ""));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);

        //Setting toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set the chat screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                ChatFragment.newInstance(getIntent().getExtras().getString(Constants.ARG_RECEIVER),
                        getIntent().getExtras().getString(Constants.ARG_RECEIVER_UID),
                        getIntent().getExtras().getString(Constants.ARG_FIREBASE_TOKEN)),
                ChatFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}