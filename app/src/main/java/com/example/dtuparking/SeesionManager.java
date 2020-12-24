package com.example.dtuparking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SeesionManager {
    private static String TAG = SeesionManager.class.getName();
    SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;

    private static final String Name = "android_demo";
    private static final String Key_Login = "isLogin";

    public SeesionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(Name,MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void SetLogin(boolean isLogin){
        editor.putBoolean(Key_Login,isLogin);
        editor.commit();
    }
    public boolean check(){
        return preferences.getBoolean(Key_Login,false);
    }
}
