package com.example.climbtogether.detail_activity;

import com.example.climbtogether.db_modle.DataDTO;

public class DetailActivityPresenterImpl implements DetailActivityPresenter {

    private DetailActivityVu mView;

    public DetailActivityPresenterImpl(DetailActivityVu mView) {
        this.mView = mView;
    }

    @Override
    public void onBackButtonClickListener() {
        mView.closePage();
    }

    @Override
    public void onPrepareData(DataDTO data) {
        mView.setToolbarTitle(data.getName());
        mView.setRecyclerView(data);
    }
}
