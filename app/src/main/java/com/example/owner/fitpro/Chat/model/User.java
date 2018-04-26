package com.example.owner.fitpro.Chat.model;

/**
 * FitPro - Group #05
 * Authors: Dan Epstein, Iliyas Mohammed, Steven Hongcheng
 */


import com.google.firebase.database.IgnoreExtraProperties;

    /**
     * Model class this will hold all the data for Users
     */

@IgnoreExtraProperties
public class User {
    public String uid;
    public String name;
    public String email;
    public String photoURL;
    public String phone;
    public String age;
    public String height;
    public String weight;
    public String exp;
    public String firebaseToken;
    public String bodyType;
    public String goal;
    public User() {}

    public User(String uid, String email, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }

    public User(String uid, String photoURL, String name, String email, String phone) {
        this.uid = uid;
        this.photoURL = photoURL;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}