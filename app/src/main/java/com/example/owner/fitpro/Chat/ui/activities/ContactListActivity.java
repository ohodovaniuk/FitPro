package com.example.owner.fitpro.Chat.ui.activities;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.owner.fitpro.Chat.ChatDatabase.DatabaseHelper;
import com.example.owner.fitpro.Chat.Contacts.activities.NewContactActivity;
import com.example.owner.fitpro.Chat.functionDefinitions.users.GetUsersDefinition;
import com.example.owner.fitpro.Chat.functionDefinitions.users.GetUsersInterface;
import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.Chat.ui.adapters.UserListingRecyclerAdapterCheckBox;
import com.example.owner.fitpro.Chat.utility.ItemClickOptions;
import com.example.owner.fitpro.MainActivity;
import com.example.owner.fitpro.R;

import java.util.List;

import static com.example.owner.fitpro.Chat.model.Globals.AddedUserListContactList;

public class ContactListActivity extends AppCompatActivity implements GetUsersInterface.View, ItemClickOptions.OnItemClickListener ,SwipeRefreshLayout.OnRefreshListener  {

    final Context context = this;
    private RecyclerView mRecyclerViewAllUserListing;
    private UserListingRecyclerAdapterCheckBox mUserListingRecyclerAdapter;
    private GetUsersDefinition mGetUsersDefinition;
    private ImageButton DialogAdd;
    private CheckBox mCheckBox;

    // Override the back button of the android navigation
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.putExtra("tabSelected", "2");  // pass your values and retrieve them in the other Activity using keyName
        startActivity(myIntent);
    }

    // Back arrow button on the left corner.
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.putExtra("tabSelected", "2");
        startActivity(myIntent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_list_checkbox);
        // Call the functions
        DialogButtons();
        BarManager();
        init();
    }

    void BarManager(){
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        //Setting toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Calling Window class to get the status bar color to green.
        Window window = this.getWindow();
        //Sets following layouts to enable to set transparent background color to green as below state
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // Sets the top bar to the primary green color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_green));
    }

    // this function redirect the user to the NewContactActivity activity when clicked on the dialogAdd button
    void DialogButtons(){
        DialogAdd = (ImageButton) this.findViewById(R.id.addNewUserDialog);
        DialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NewContactActivity.class);
                startActivity(i);
            }
        });
    }

    private void init() {
        // Initialize the views and also get users to the contactList
        mRecyclerViewAllUserListing = (RecyclerView) this.findViewById(R.id.recycler_view_all_user_listing);
        mCheckBox = (CheckBox) this.findViewById(R.id.checkBox);
        mGetUsersDefinition = new GetUsersDefinition(this);
        getUsers();
    }

    @Override
    public void onRefresh() {
        // On refresh the view, this updates any new users if added
        getUsers();
    }

    private void getUsers() {
        // Here we are opening the database and accessing the user added fields
        // The database obtains the data when the user actually added a new user from the add activity
        // Then using arraylist we add the user email to there just the email.
        DatabaseHelper mDatabaseHelper =   new DatabaseHelper(getApplicationContext());
        Cursor data = mDatabaseHelper.getAllIdData(); //get the id associated with that name
        while(data.moveToNext()){
            String  t = data.getString(data.getColumnIndex("ID"));
            AddedUserListContactList.add(t);
        }
        data.close();
        mDatabaseHelper.close();
        errorMsg();
        mGetUsersDefinition.getChatUsers(); // Calling the GetChatUser in order to add the users to the list, it will use the AddedUserListContactList Array.
    }
    // Display a msg that there isn't any users added
    void errorMsg(){
        TextView nousers;
        nousers = (TextView) this.findViewById(R.id.no_users);
        if(!AddedUserListContactList.isEmpty()){
            nousers.setText("");
        }else{
            nousers.setText("Your contact list is empty, you can add new user by pressing on the button below.");
        }
    }

    // Not using this function anymore the on item click will be accessed from the UserListingRecyclerAdapter<---
    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {}

    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        // Setting the adapter to display the item list of users
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapterCheckBox(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetAllUsersFailure(String message) {
        // Error msg aka no network connection etc
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
