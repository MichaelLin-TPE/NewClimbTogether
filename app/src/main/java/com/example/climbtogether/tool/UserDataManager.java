package com.example.climbtogether.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.storage.StorageReference;

public class UserDataManager {

    private SharedPreferences sharedPreferences;

    private Context context;

    public UserDataManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
    }

    public void saveCollectionViewStyle(int viewType){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("viewType",viewType);
        editor.apply();
    }

    public int getCollectionViewStyle(){
        return sharedPreferences.getInt("viewType",0);
    }

    public void saveNotificationToken(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",token);
        editor.apply();
    }
    public String getToken(){
        return sharedPreferences.getString("token","");
    }

    public void saveUserData(String email,String displayName,String photoUrl){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("displayName",displayName);
        editor.putString("photoUrl",photoUrl);
        editor.apply();
    }
    public String getEmail(){
        return sharedPreferences.getString("email","");
    }
    public String getDisplayName(){
        return sharedPreferences.getString("displayName","");
    }
    public String getPhotoUrl(){
        return sharedPreferences.getString("photoUrl","");
    }
}
