package com.hiking.climbtogether.mountain_fragment;

import com.hiking.climbtogether.db_modle.DataDTO;

import java.util.ArrayList;

public interface MountainFragmentPresenter {
    void onFilterTextViewBackgroundChangeListener(int textType,String levelType);

    void onWatchMoreClickListener();

    void onPrepareData();

    void onTopIconChange(String isShow, String topTime, DataDTO data, int itemPosition);


    void onModifyDataFromFirestore(ArrayList<String> firestoreData,ArrayList<Long> timeArray, ArrayList<DataDTO> allInformation);

    void initDbData();

    void onPrepareSpinnerData();

    void onSpinnerSelectListener(int position);

    void onMountainItemClick(DataDTO data);

    void onShowSpinnerDialog(ArrayList<String> spinnerData);

    void onSearchMtListener(CharSequence searchContent);

    void onShowErrorCode(String errorCode);

    void onActivityCreated();

    void onMtListItemIconClickListener(DataDTO data, int itemPosition);
}
