package com.example.climbtogether.detail_activity;

import com.example.climbtogether.db_modle.DataDTO;

public interface DetailActivityPresenter {
    void onBackButtonClickListener();

    void onPrepareData(DataDTO data);
}
