package com.example.owner.fitpro.Chat.ChatDatabase;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelperMarked extends SQLiteOpenHelper {

    /**
     * These are the Variables to interact with the database functions
     * We create table called user_marked
     * It checks if the user is marked in order to add to their friend list
     * */

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "user_marked";
    private static final String EMAIL = "ID";
    private static final String DETAILS = "Details";
    private static final String MARKEDON = "Marked";
    private static final String MSG = "Msg";

    // The default constructor to create the version of the database table name.
    public DatabaseHelperMarked(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    // This function creates a table if it does not exist.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " ("+
                EMAIL + " TEXT PRIMARY KEY, " +
                MARKEDON + " TEXT , " +
                MSG + " TEXT , " +
                DETAILS +" TEXT)";
        db.execSQL(createTable);
    }

    // Empty This wont do anything because there is no need to.
    // But it must be included here because we are extending the SQLiteOpenHelper class.
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    // This Function set the selected user to as marked so it can be show on the contact list
    public boolean addDataEmailMarked(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL, email);
        contentValues.put(MARKEDON, "MARKED");
        contentValues.put(MSG, "No Messages");

        //Log.d(TAG, "addData: Adding " + email + " to " + TABLE_NAME);

        long data = db.insert(TABLE_NAME, null, contentValues);

        //if email as inserted incorrectly it will return -1
        if (data == -1) {
            return false;
        } else {
            return true;
        }
    }


    // Returns all ID (which is email) from the database.
    public Cursor getAllIdData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT ID FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // Returns the mark data on the selected user email
    public Cursor getUserMarkedStatus(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + MARKEDON + " FROM " + TABLE_NAME +
                " WHERE " + EMAIL + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // Return the email on the selected parameter name
    public Cursor getID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + EMAIL + " FROM " + TABLE_NAME +
                " WHERE " + EMAIL + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // Returns the msg from the selected user
    public Cursor getMsg(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + MSG + " FROM " + TABLE_NAME +
                " WHERE " + EMAIL + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // This functions update the msg with new value where email is equal to the giving parameter.
    public void updateMsg(String newName, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + MSG +
                " = '" + newName + "' WHERE " + EMAIL + " = '" + id + "'";
        //Log.d(TAG, "updateName: query: " + query);
        //Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    // This function delete the user from the database
    public void deleteName(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + EMAIL + " = '" + id + "'";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
}