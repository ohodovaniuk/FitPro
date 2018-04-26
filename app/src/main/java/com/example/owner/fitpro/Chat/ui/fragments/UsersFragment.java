package com.example.owner.fitpro.Chat.ui.fragments;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.fitpro.Chat.ChatDatabase.DatabaseHelperMarked;
import com.example.owner.fitpro.Chat.Contacts.activities.ContactDetailsActivity;
import com.example.owner.fitpro.Chat.functionDefinitions.users.GetUsersDefinition;
import com.example.owner.fitpro.Chat.functionDefinitions.users.GetUsersInterface;
import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.Chat.ui.activities.ChatActivity;
import com.example.owner.fitpro.Chat.ui.activities.ContactListActivity;
import com.example.owner.fitpro.Chat.ui.adapters.UserListingRecyclerAdapter;
import com.example.owner.fitpro.Chat.utility.Constants;
import com.example.owner.fitpro.Chat.utility.ItemClickOptions;
import com.example.owner.fitpro.R;

import java.util.List;

import static com.example.owner.fitpro.Chat.model.Globals.AddedUserList;

public class UsersFragment extends Fragment implements GetUsersInterface.View, ItemClickOptions.OnItemClickListener,ItemClickOptions.OnItemLongClickListener ,SwipeRefreshLayout.OnRefreshListener {

    // Creating private views here to an easy access across this class
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllUserListing;
    private UserListingRecyclerAdapter mUserListingRecyclerAdapter;
    private GetUsersDefinition mGetUsersDefinition;
    private ImageButton DialogAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_users, container, false);
        bindViews(fragmentView);
        // When user taps on the button below of the fragment it will redirect them to the
        // ContactListActivity which from there the user may add another user to the list.
        DialogAdd = (ImageButton) fragmentView.findViewById(R.id.addNewUserDialog);
        DialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                Intent i = new Intent(getContext(), ContactListActivity.class);
                dialog.dismiss();
                startActivity(i);
            }
        });
        return fragmentView;
    }

    // Bind (sets) the views with the XML ids
    private void bindViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_user_listing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    // Initialize the views, adapters, refresh
    private void init() {
        mGetUsersDefinition = new GetUsersDefinition(this);
        getUsers();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        ItemClickOptions.addTo(mRecyclerViewAllUserListing).setOnItemClickListener(this);
        ItemClickOptions.addTo(mRecyclerViewAllUserListing).setOnItemLongClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    // Upon refresh the fragment call the get User function
    @Override
    public void onRefresh() {
        getUsers();
    }

    private void getUsers() {
        // Opening the database to store the user email then calls the getalluser which in that function
        // there are validation for the AddedUserList array which then will display/add the user
        DatabaseHelperMarked mDatabaseHelper =   new DatabaseHelperMarked(getActivity());
        Cursor data = mDatabaseHelper.getAllIdData(); //get the id associated with that name
        while(data.moveToNext()){
            String t = data.getString(data.getColumnIndex("ID"));
            AddedUserList.add(t);
        }
        data.close();
        mDatabaseHelper.close();
        errorMsg();
        mGetUsersDefinition.getAllUsers();
    }

    // Display the error msg on screen if chat list is empty
    @SuppressLint("ResourceAsColor")
    void errorMsg(){
        TextView emptyUserList;
        emptyUserList = (TextView) getView().findViewById(R.id.no_users);
        if(!AddedUserList.isEmpty()){
            emptyUserList.setText("");
            emptyUserList.setBackgroundColor(getResources().getColor(R.color.primary));

        }else{
            emptyUserList.setText("You don't have any contacts, tap on the (+) sign below to add from the list.");
        }
    }

    // This function override the onItemlongClick of ItemClickOption java class
    // The user must hold the item (long click) then it will pass the other user to the contactDetailActivity
    // From there we will get the values from this fragment to get the user detail
    @Override
    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
        Intent myIntent = new Intent(v.getContext(), ContactDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_USERNAME",mUserListingRecyclerAdapter.getUser(position).name.toString());
        extras.putString("EXTRA_PHOTO", mUserListingRecyclerAdapter.getUser(position).photoURL.toString());
        extras.putString("EXTRA_PHONE",mUserListingRecyclerAdapter.getUser(position).phone.toString());
        extras.putString("EXTRA_EMAIL", mUserListingRecyclerAdapter.getUser(position).email.toString());
        extras.putString("EXTRA_UID", mUserListingRecyclerAdapter.getUser(position).uid.toString());
        myIntent.putExtras(extras);
        startActivity(myIntent);
        return true;
    }

    // This will start the chat when you as the user press on the item, it will pass the photo string
    // In order to set the other user profile picture
    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Intent myIntent = new Intent(v.getContext(), ChatActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_PHOTO", mUserListingRecyclerAdapter.getUser(position).photoURL.toString());
        myIntent.putExtras(extras);
        Constants.ARG_FIREBASE_PHOTO =  mUserListingRecyclerAdapter.getUser(position).photoURL.toString();
        startActivity(myIntent);
        ChatActivity.startActivity(getActivity(),
                mUserListingRecyclerAdapter.getUser(position).email,
                mUserListingRecyclerAdapter.getUser(position).uid,
                mUserListingRecyclerAdapter.getUser(position).firebaseToken,
                mUserListingRecyclerAdapter.getUser(position).photoURL);
    }

    // Sets the user adapter if successful
    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    // Display a msg if something goes wrong when trying to get users
    @Override
    public void onGetAllUsersFailure(String message) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }


}
