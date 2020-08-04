package com.reidius.lawrenceafriyie.overwatchmap.services;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "Sharedpref";
    private static final String KEY_ACCESS_TOKEN = "deviceToken";
    private static Context mCtx;
    private static SharedPrefManager mInstance;

    private  SharedPrefManager(Context context)
    {
        mCtx = context;
    }
 public static synchronized SharedPrefManager getmInstance(Context context)
 {
     if(mInstance == null)
         mInstance = new SharedPrefManager(context);
     return  mInstance;
 }
 // stores the token to an editor
 public boolean storeToken(String token)
 {
     SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
     SharedPreferences.Editor editor = sharedPreferences.edit();
     editor.putString(KEY_ACCESS_TOKEN,token);
     editor.apply();;
     return true;
 }
        //gets the token from editor
    public String getKeyAccessToken() {
        SharedPreferences sharedPreferences =mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }
}
