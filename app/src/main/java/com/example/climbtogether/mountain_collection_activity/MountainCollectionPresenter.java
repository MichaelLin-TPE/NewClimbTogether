package com.example.climbtogether.mountain_collection_activity;

import java.util.ArrayList;

public interface MountainCollectionPresenter {
    void onToolbarNavigationIconClickListener();

    void onBackPressedListener();

    void onUserExist();

    void onUserNotExist();

    void onBtnLoginClickListener();

    void onSearchNoDataFromFirebase();

    void onSearchDataSuccessFromFirebase(ArrayList<String> mtNameArray , ArrayList<Long> timeArray);

    void onPrepareSpinnerData();

    void onSpinnerItemSelectedListener(int position);
}
