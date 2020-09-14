package com.hiking.climbtogether.tool;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class DataProvider {

    private Context context = ClimbTogetherApplication.getInstance().getContext();

    private static DataProvider instance;

    public static DataProvider getInstance(){
        if (instance == null){
            instance = new DataProvider();
            return instance;
        }
        return instance;
    }
    public ArrayList<String> getTabTitleArray(){
        ArrayList<String> tabTitleArray = new ArrayList<>();
        tabTitleArray.add(context.getString(R.string.home));
        tabTitleArray.add(context.getResources().getString(R.string.mountain));
        tabTitleArray.add(context.getResources().getString(R.string.equipment));
        tabTitleArray.add(context.getResources().getString(R.string.discuss));
        tabTitleArray.add(context.getString(R.string.personal_chat));
        return tabTitleArray;
    }
    
    public ArrayList<Drawable> getNotPressIconArray(){
        ArrayList<Drawable> notPressIconArray = new ArrayList<>();
        notPressIconArray.add(ContextCompat.getDrawable(context,R.drawable.home_pressed));
        notPressIconArray.add(ContextCompat.getDrawable(context,R.drawable.mt_not_press));
        notPressIconArray.add(ContextCompat.getDrawable(context,R.drawable.equipment_not_press));
        notPressIconArray.add(ContextCompat.getDrawable(context,R.drawable.chat_not_press));
        notPressIconArray.add(ContextCompat.getDrawable(context,R.drawable.personal_chat_not_pressed));
        return notPressIconArray;
    }

    public ArrayList<Drawable> getPressedIconArray(){
        ArrayList<Drawable> pressedIconArray = new ArrayList<>();
        pressedIconArray.add(ContextCompat.getDrawable(context,R.drawable.home_pressed));
        pressedIconArray.add(ContextCompat.getDrawable(context,R.drawable.mt_pressed));
        pressedIconArray.add(ContextCompat.getDrawable(context,R.drawable.equipment_pressed));
        pressedIconArray.add(ContextCompat.getDrawable(context,R.drawable.chat_pressed));
        pressedIconArray.add(ContextCompat.getDrawable(context,R.drawable.personal_chat_pressed));
        return pressedIconArray;
    }
    
    public ArrayList<String> getSpinnerDataArray(){
        ArrayList<String> spinnerData = new ArrayList<>();
        spinnerData.add(context.getString(R.string.no_sort));
        spinnerData.add(context.getString(R.string.order_by_time));
        return spinnerData;
    }

    

}
