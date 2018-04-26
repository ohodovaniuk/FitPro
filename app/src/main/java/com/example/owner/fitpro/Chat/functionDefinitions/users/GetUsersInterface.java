package com.example.owner.fitpro.Chat.functionDefinitions.users;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import com.example.owner.fitpro.Chat.model.User;

import java.util.List;

public interface GetUsersInterface {

    /**
     * These are function interfaces which gets implemented in GetUserDefinition class
     * Interface View will display msg on the app screen
     * Interface Presenter able to get all users from firebase and display on contact list.
     * Interface Interactor is what updates the chat and contacts.
     * Interface OnGetAllUsersListener display msgs on app screen.
     */

    interface View {
        void onGetAllUsersSuccess(List<User> users);
        void onGetAllUsersFailure(String message);
    }

    interface Presenter {
        void getAllUsers();
        void getChatUsers();
    }

    interface Interactor {
        void getAllUsersFromFirebase();
        void getChatUsersFromFirebase();
    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<User> users);
        void onGetAllUsersFailure(String message);
    }
}
