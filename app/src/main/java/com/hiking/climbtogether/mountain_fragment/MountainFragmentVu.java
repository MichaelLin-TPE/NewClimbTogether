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

    void setDataChange(ArrayList<DataDTO> dataDTO,String isShow);

    void showDatePick(int sid);

    void setFirestore(int sid, long topTime,DataDTO data);

    void intentToLoginActivity();

    void deleteFavorite(int sid, DataDTO dataDTO);

    void searchDataFromDb(String email, ArrayList<DataDTO> allInformation);

    void readyToProvideData();

    void showProgressbar(boolean isShow);

    void setSpinner(ArrayList<String> spinnerData);

    void changeRecyclerViewSor(ArrayList<DataDTO> allInformation);

    void intentToMtDetailActivity(DataDTO data);
}
