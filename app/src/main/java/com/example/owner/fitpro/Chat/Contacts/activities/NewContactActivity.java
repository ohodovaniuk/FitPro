package com.example.owner.fitpro.Chat.Contacts.activities;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.owner.fitpro.Chat.ChatDatabase.DatabaseHelper;
import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.Chat.ui.activities.ContactListActivity;
import com.example.owner.fitpro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.owner.fitpro.Chat.model.Globals.AddedUserList;

public class NewContactActivity extends AppCompatActivity {

    private Button submit;
    private EditText username,phoneNumber,email,details;
    private List<String> users;
    String tvUser,detail;

    // This function will redirect to the ContactListActivity when the user click on the back arrow button.
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ContactListActivity.class);
        startActivity(myIntent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        // Call SetViews function to set the views with the XML IDs.
        SetViews();
        // Setting the toolbar without title.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submit.setOnClickListener(v -> {
            // Get the user email
            tvUser = username.getText().toString();
            // Get the user Detail
            detail = details.getText().toString();

            // Accessing the database helper to do some data CRUD functionality.
            DatabaseHelper mDatabaseHelper =   new DatabaseHelper(this);
            // This is the most important part as we accessing the FB and loop trough the users to get the available users who are registered.
            FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                    // Set new arraylist instance.
                    users = new ArrayList<>();

                    // Here we are looping and adding all the user email into the users arraylist.
                    while (dataSnapshots.hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshots.next();
                        User user = dataSnapshotChild.getValue(User.class);
                        // This will add all the user except the current user that is logged on aka yourself.
                        if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            // Add the user email to the arraylist
                            users.add(user.email);
                        }
                    }

                    // Get the logged user to validation
                    String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                            //Validates Email using the built in email address match regex
                            if (Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches() ) {

                                // we check if the user tries to add them self and display an error msg.
                                if(username.getText().toString().equals(firebaseUser)) {
                                    Toast.makeText(getApplicationContext(),"You can't add yourself to Contact List!",
                                            Toast.LENGTH_SHORT).show();

                                    // If above passes and the user ArrayList finds an email address from the edit EditTextView we can proceed
                                } else if(users.contains(tvUser)  ){

                                    // Open the database cursor and loop trough the rows to find the user
                                    Cursor data = mDatabaseHelper.getID(tvUser); //get the id associated with that name
                                    while(data.moveToNext()){
                                        int index = data.getColumnIndex("ID");
                                        AddedUserList.add(data.getString(index));
                                    }
                                    //Close database & Cursor
                                    data.close();
                                    mDatabaseHelper.close();

                                    // If the user is not in the AddedUserList array then that means the user is already added to your contact list
                                    if(!AddedUserList.contains(tvUser)){

                                        //Insert Data into the database if all validation passes
                                        mDatabaseHelper.addData(tvUser,detail);
                                        // Set the intent to the ContactListActivity
                                        Intent myIntent = new Intent(getApplicationContext(), ContactListActivity.class);
                                        //Redirect to the ContactListActivity where it will show the newly added user
                                        startActivity(myIntent);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "The User is already in your friends list!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                               }else{
                                    // Display error msg
                                    Toast.makeText(getApplicationContext(),"This User Does Not Exist.", Toast.LENGTH_SHORT).show();
                               }
                                 }else{
                                // Display error msg
                                Toast.makeText(getApplicationContext(),"Please use a valid Email Address",
                                        Toast.LENGTH_SHORT).show();

                                // Set the state to empty of the two edit text fields
                                username.setText("");
                                details.setText("");
                            }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        });
    }
    // Set the variables with the XML views
    private void SetViews() {
        //imBackButton = (ImageButton) findViewById(R.id.btReturnHome);
        username = (EditText) findViewById(R.id.edit_text_username);
        phoneNumber = (EditText) findViewById(R.id.edit_text_phone_number);
        email = (EditText) findViewById(R.id.edit_text_email);
        details = (EditText) findViewById(R.id.edit_text_details);
        submit = (Button) findViewById(R.id.btSaveContact);
    }
}