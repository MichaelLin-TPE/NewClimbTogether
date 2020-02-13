package com.example.climbtogether.mountain_collection_activity;

import android.content.Context;

import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

import java.util.ArrayList;

public interface MountainCollectionVu {
    void closePage();

    void setViewMaintain(boolean isShow);

    void intentToLoginActivity();

    void searchDataFromFirebase();

    void showSearchNoDataView();

    Context getVuContext();

    void setRecyclerViewData(ArrayList<DataDTO> dataArrayList);

    void showProgressbar(boolean isShow);

    void setSpinner(ArrayList<String> listArray);

    void setViewType(int position);
}
