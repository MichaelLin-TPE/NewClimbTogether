package com.hiking.climbtogether.detail_activity;

import com.hiking.climbtogether.db_modle.DataDTO;

public interface DetailActivityPresenter {
    void onBackButtonClickListener();

    void onPrepareData(DataDTO data);

    void onFavoriteItemClickListener(boolean isCheck);
}
