package com.example.project.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "user_email";

    public SessionManager(){}

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String email){
        editor.putString(SESSION_KEY, email).commit();
    }

    //to get session
    public String getSession(){
        //should return the stored email
        //other return null
        return sharedPreferences.getString(SESSION_KEY, null);
    }

    public void removeSession(){
        editor.putString(SESSION_KEY, null).commit();
    }


}
