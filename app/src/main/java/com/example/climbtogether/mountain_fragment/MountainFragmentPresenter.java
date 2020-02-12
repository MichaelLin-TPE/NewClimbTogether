package com.example.climbtogether.mountain_fragment;

import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

import java.util.ArrayList;

public interface MountainFragmentPresenter {
    void onFilterTextViewBackgroundChangeListener(int textType,String levelType);

    void onWatchMoreClickListener();

    void onPrepareData();

    void onTopIconChange(int sid,String isShow,String topTime);

    void onShowDatePicker(int sid);

    void onCreateDocumentInFirestore(int sid, String topTime);

    void onLoginEvent();

    void onSearchDbData(String email);

    void onModifyDataFromFirestore(ArrayList<String> firestoreData,ArrayList<String> timeArray, ArrayList<DataDTO> allInformation);

    void initDbData();

    void onPrepareSpinnerData();
}
