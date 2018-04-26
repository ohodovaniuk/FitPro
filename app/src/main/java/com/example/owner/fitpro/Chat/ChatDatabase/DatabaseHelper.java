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
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
    * These are the Variables to interact with the database functions
    * We create table called user_details
    * It will holds the details for each user respectively
    * This Database is accessed to get the user details on the chat contact list
    * */

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "user_details";
    private static final String EMAIL = "ID";
    private static final String DETAILS = "Details";
    private static final String MARKEDON = "Marked";


    // The default constructor to create the version of the database table name.
    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    // This function creates a table if it does not exist.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " ("+
                EMAIL + " TEXT PRIMARY KEY, " +
                MARKEDON + " TEXT, NULLABLE, " +
                DETAILS +" TEXT)";
        db.execSQL(createTable);
    }

    // Empty This wont do anything because there is no need to.
    // But it must be included here because we are extending the SQLiteOpenHelper class.
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    // This Function add a user email to the tablespace.
    public boolean addData(String email, String detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL, email);
        contentValues.put(MARKEDON, "NONE");
        contentValues.put(DETAILS, detail);

        //Log.d(TAG, "addData: Adding " + email + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
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

    // Returns only the Detail that matches the name passed in.
    public Cursor getItemDetail(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DETAILS + " FROM " + TABLE_NAME +
                " WHERE " + EMAIL + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // Returns only the Email that matches the name passed in.
    public Cursor getID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + EMAIL + " FROM " + TABLE_NAME +
                " WHERE " + EMAIL + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // This function will set the detail with the value that is passed in with the matching ID (email).
    public void updateDetails(String newName, String id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + DETAILS +
                " = '" + newName + "' WHERE " + EMAIL + " = '" + id + "'" +
                " AND " + DETAILS + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    // Delete a user from database (email).
    public void deleteName(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + EMAIL + " = '" + id + "'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
}