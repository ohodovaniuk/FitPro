package com.example.owner.fitpro.Chat.functionDefinitions.users;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import com.example.owner.fitpro.Chat.model.User;

import java.util.List;



public class GetUsersDefinition implements GetUsersInterface.Presenter, GetUsersInterface.OnGetAllUsersListener {

    /**
     * This Class implements the GetUserInterface
     * The use  of the function is to display error msg, send and receive a msg from the firebase
     */

    private GetUsersInterface.View mView;
    private GetUsersFB mGetUsersFB;

    // Constructor set the view and firebase
    public GetUsersDefinition(GetUsersInterface.View view) {
        this.mView = view;
        mGetUsersFB = new GetUsersFB(this);
    }

    // This displays and get all the users that are available from firebase ChatList Activity
    @Override
    public void getAllUsers() {
        mGetUsersFB.getAllUsersFromFirebase();
    }

    // This displays and get all the users that are available from firebase into ContactListActivity
    @Override
    public void getChatUsers() {
        mGetUsersFB.getChatUsersFromFirebase();
    }

    // Display a msg if all users has been retrieved successfully.
    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        mView.onGetAllUsersSuccess(users);
    }

    // Display a msg if all users has not been retrieved successfully.
    @Override
    public void onGetAllUsersFailure(String message) {
        mView.onGetAllUsersFailure(message);
    }
}
