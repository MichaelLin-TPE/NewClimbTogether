package com.hiking.climbtogether.home_activity;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.android.billingclient.api.SkuDetails;

import java.util.ArrayList;
import java.util.List;

public interface HomePageVu {
    Context getVuContext();

    void showBottomTabLayout(ArrayList<String> tabTitleArray, ArrayList<Drawable> notPressIconArray, ArrayList<Drawable> pressedIconArray);

    void showViewPager();

    void intentToMemberActivity();

    void showQuestionDialog();

    void contactMe();

    void checkGooglePlayAccount();


    void showSingleDonateDialog();

    void showLoopDonateDialog();

    void checkGoogleUpdate();

    void showUpdateDialog();

    void intentToGooglePlay();

    void printOrderList(List<SkuDetails> list);

    void showToast(String errorMessage);
}
