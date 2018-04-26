package com.example.owner.fitpro.Chat.Contacts.activities;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.fitpro.Chat.ChatDatabase.DatabaseHelper;
import com.example.owner.fitpro.Chat.ui.activities.ChatActivity;
import com.example.owner.fitpro.MainActivity;
import com.example.owner.fitpro.R;
import com.squareup.picasso.Picasso;

public class ContactDetailsActivity extends AppCompatActivity {


    // Creating the data and View to hold the XML attributes
    private ImageView userPicture;
    private Button send_msg;
    private TextView name, phone, email, details;
    private ImageButton DialogAdd;
    final Context context = this;
    String itemID = null;


    // This function will redirect to the home page of the app to the second tab which is CHAT
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.putExtra("tabSelected", "2");
        startActivity(myIntent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        //Setting Views
        send_msg = (Button) findViewById(R.id.send_messege);
        userPicture = (ImageView) findViewById(R.id.ProfilePhoto);
        name = (TextView) findViewById(R.id.text_view_username);
        phone = (TextView) findViewById(R.id.text_view_user_phone);
        email = (TextView) findViewById(R.id.text_view_user_email);
        details = (TextView) findViewById(R.id.text_view_user_details);
        DialogAdd = (ImageButton) this.findViewById(R.id.pEditIV);

        //Getting Values
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String FullName = extras.getString("EXTRA_USERNAME");
        final String photo_ = extras.getString("EXTRA_PHOTO");
        String phone_ = extras.getString("EXTRA_PHONE");
        final String email_ = extras.getString("EXTRA_EMAIL");
        final String uid_ = extras.getString("EXTRA_UID");

        //Setting all the Attributes
        Uri uri = Uri.parse(photo_);
        Picasso.with(this).load(uri).into(userPicture);
        name.setText(FullName);
        phone.setText(phone_);
        email.setText(email_);

        // This will redirect to the user chat when clicked
        send_msg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ChatActivity.startActivity(getApplicationContext(),
                        email_,
                        uid_,
                        "1",
                        photo_);
            }
        });

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        DatabaseHelper mDatabaseHelper =   new DatabaseHelper(this);

        //get the id associated with that name
        Cursor data = mDatabaseHelper.getItemDetail(email_);
        while(data.moveToNext()){
            int index = data.getColumnIndex("Details");
            itemID = data.getString(index);
        }
        data.close();
        mDatabaseHelper.close();
        //Log.d("DATA",itemID.toString());

        // Set the detail that itemID have obtained from the database which corresponds to the user that has been clicked
        details.setText(String.valueOf(itemID.toString()));

        //Setting the toolbar to empty
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // This function ask the user edit the contact detail of a specific contact
        DialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we declare a static dialog using the view contact details so we can show the dialog later
                final Dialog dialog = new Dialog(context);
                // We assign the content view with the appropriate XML dialog
                dialog.setContentView(R.layout.update_user_detail_dialog);
                // Assign the XML values to the variables
                Button btCancel = (Button)dialog.findViewById(R.id.cancelButtonDetail);
                Button btNext = (Button)dialog.findViewById(R.id.nextButtonDetail);
                EditText newDetail = (EditText) dialog.findViewById(R.id.userDetailTI);

                // This is the cancel button if clicked the dialog will dismiss/
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                // This is the next button and it will show as "CHANGE" in the
                btNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // When the user tap on the CHANGE button it will cal the database helper and will instantly update the contact detail with the newer data
                        mDatabaseHelper.updateDetails(newDetail.getText().toString(),email_,itemID.toString()); //get the id associated with that name
                        // Close the database instance and cursor
                        data.close();
                        mDatabaseHelper.close();
                        //Log.d("DATA",newDetail.getText().toString());
                        finish();
                        dialog.dismiss();
                        // Refresh the activity displaying the new dialog
                        startActivity(getIntent());
                    }
                });
                // Display the XML dialog
                dialog.show();
            }
        });
    }
}
