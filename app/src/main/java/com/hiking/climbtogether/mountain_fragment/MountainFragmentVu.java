package com.hiking.climbtogether.mountain_fragment;

import android.content.Context;

import com.hiking.climbtogether.db_modle.DataDTO;

import java.util.ArrayList;

public interface MountainFragmentVu {
    void showAllInformation();

    void showTextViewBackgroundChange(int textType);

    Context getVuContext();

    void setRecyclerView(ArrayList<DataDTO> allInformation);

    void showSearchNoDataInformation(boolean isShow);



    void showDatePick(DataDTO data, int itemPosition);



    void intentToLoginActivity();



    void readyToProvideData();

    void showProgressbar(boolean isShow);

    void setSpinner(ArrayList<String> spinnerData);


    void intentToMtDetailActivity(DataDTO data);

    void showSpinnerDialog(ArrayList<String> spinnerData);

    void saveSortType(int position);

    void showErrorCode(String errorCode);

    void updateRecyclerView(ArrayList<DataDTO> dataArrayList);

    String getSortType();
}
