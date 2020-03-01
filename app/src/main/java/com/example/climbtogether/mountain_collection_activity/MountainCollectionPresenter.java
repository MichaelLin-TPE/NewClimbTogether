package com.example.climbtogether.mountain_collection_activity;

import com.example.climbtogether.db_modle.DataDTO;

import java.util.ArrayList;

public interface MountainCollectionPresenter {
    void onToolbarNavigationIconClickListener();

    void onBackPressedListener();

    void onUserExist();

    void onUserNotExist();

    void onBtnLoginClickListener();

    void onSearchNoDataFromFirebase();

    void onSearchDataSuccessFromFirebase(ArrayList<String> mtNameArray , ArrayList<Long> timeArray,ArrayList<String> downloadUrlArray);

    void onPrepareSpinnerData();

    void onSpinnerItemSelectedListener(int position);

    void onItemClickListener(DataDTO dataDTO,int position);

    void onItemDialogClickListener(int itemPosition,DataDTO dataDTO,int position);

    void onShowProgressToast(String message);

    void onDatePickerDialogClickListener(DataDTO dataDTO, long pickTime);
}
