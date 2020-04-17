package com.hiking.climbtogether.home_activity;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public interface HomePageVu {
    Context getVuContext();

    void showBottomTabLayout(ArrayList<String> tabTitleArray, ArrayList<Drawable> notPressIconArray, ArrayList<Drawable> pressedIconArray);

    void showViewPager();

    void intentToMemberActivity();

    void showQuestionDialog();

    void contactMe();

    void checkGooglePlayAccount();


    void showDonateDialog();
}
