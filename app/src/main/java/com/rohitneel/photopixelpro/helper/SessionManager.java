package com.rohitneel.photopixelpro.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.rohitneel.photopixelpro.activities.LauncherActivity;

import java.util.HashMap;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String name){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // commit changes
        editor.commit();
    }



    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LauncherActivity.class);

            // Closing all the activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserName(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // return user
        return user;
    }

    /**
     * Clear session detailsX
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LauncherActivity.class);

        // Closing all the activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     *  Storing current theme state in editor.
     */
    public void saveState(boolean state) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences("ABHOPositive", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }


    /**
     *  Loading current theme state in editor.
     */
    public boolean loadState() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences("ABHOPositive", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("NightMode", false);
    }

    public void saveFullScreenState(boolean state){
        SharedPreferences sharedPreferences = _context.getSharedPreferences("FullScreen", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("FullScreenMode", state);
        editor.apply();
    }

    public boolean loadFullScreenState() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences("FullScreen", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("FullScreenMode", false);
    }

    public void saveAppPermissionState(boolean state){
        SharedPreferences sharedPreferences = _context.getSharedPreferences("AppPermission", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("Permission", state);
        editor.apply();
    }

    public boolean loadAppPermissionState() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences("AppPermission", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("Permission", false);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}


