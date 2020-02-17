package com.example.climbtogether.tool;

import android.content.Context;
import android.content.SharedPreferences;

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
}
