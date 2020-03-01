package com.example.climbtogether.mountain_collection_activity;

import android.content.Context;

import com.example.climbtogether.db_modle.DataDTO;

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

    void showItemAlertDialog(DataDTO dataDTO,int position);

    void selectPhoto(String mtName);

    void showToast(String message);

    void saveViewType(int position);

    void removeData(DataDTO dataDTO);

    void showDatePickerDialog(DataDTO dataDTO);

    void modifyFirestoreData(DataDTO data, long pickTime);

    void changeRecyclerView(int position);
}
