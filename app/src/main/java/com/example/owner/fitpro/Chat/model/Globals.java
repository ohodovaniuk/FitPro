package com.example.owner.fitpro.Chat.model;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import java.util.ArrayList;
import java.util.List;

public class Globals {

    /**
     * These Global variables enable to hold the information of users when they being added
     * Then we will use the information to delete duplicates and store in the contact list
     */

    private static Globals mInstance= null;
    public static String[] UserArray = new String[100000];
    // Holds the users whos added to the chat list
    public static List<String> AddedUserList = new ArrayList<>();
    // Holds the user who are added to the contact list
    public static List<String> AddedUserListContactList = new ArrayList<>();
    public static int number;
    protected Globals(){}

    // Enable to synchronize the instance in save state without creating new instances so we can save the user information
    public static synchronized Globals getInstance(){
        if(null == mInstance){
            mInstance = new Globals();
        }
        return mInstance;
    }
}
