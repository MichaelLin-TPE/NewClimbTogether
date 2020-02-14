package com.example.climbtogether.mountain_collection_activity;

import android.graphics.Bitmap;

import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

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

    void onItemClickListener(DataDTO dataDTO);

    void onItemDialogClickListener(int itemPosition,DataDTO dataDTO);

    void onShowProgressToast(String message);

    void onDatePickerDialogClickListener(DataDTO dataDTO, long pickTime);
}
