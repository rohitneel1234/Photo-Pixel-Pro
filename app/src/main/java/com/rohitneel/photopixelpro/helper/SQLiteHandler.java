/**
 * Author: Rohit Neel
 * updated by 02-05-2021
 * */
package com.rohitneel.photopixelpro.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;


    // Database Name
    private static final String DATABASE_NAME = "OFFLINE_USER_DB";

    // Login table name
    private static final String TABLE_USER = "USER";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE="mobile";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," +  KEY_MOBILE + " TEXT"
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_MOBILE, mobile); // Mobile
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    public void updateUser(String name, String email, String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_MOBILE, mobile);
        // updating row
        db.update(TABLE_USER, values, KEY_EMAIL + " = ?",
                new String[]{email});
        db.close();
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(String email) {
        HashMap<String, String> user = new HashMap<String, String>();
        //String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
       // Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor1 = db.query(TABLE_USER,null,"email=?",new String[]{email},null,null,null,null);
        // Move to first row
        if (cursor1.moveToFirst()) {
                user.put("name", cursor1.getString(1));
                user.put("email", cursor1.getString(2));
                user.put("mobile", cursor1.getString(3));
        }
        cursor1.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public Cursor execute(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(s,null);
    }

    /**
     * This method to check user exist or not
     * @param email - user email address
     * @return true/false
     */
    public boolean checkUserExist(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = KEY_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                null,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * This method to check user exist or not
     * @param mobile - user mobile number
     * @return true/false
     */
    public boolean checkUserExistWithMobileNumber(String mobile) {

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = KEY_MOBILE + " = ?";

        // selection argument
        String[] selectionArgs = {mobile};

        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                null,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }



}
