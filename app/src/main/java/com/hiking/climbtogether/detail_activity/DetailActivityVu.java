package com.hiking.climbtogether.detail_activity;

import com.hiking.climbtogether.db_modle.DataDTO;

public interface DetailActivityVu {
    void closePage();

    void setToolbarTitle(String name);

    void setRecyclerView(DataDTO data);

    void showFavoriteStatus(boolean isCheck);

    void uploadMtPhoto(byte[] photo);

    void showIsFavorite(boolean isFavorite);

    void saveFavoriteMt(String jsonStr);

    void IntentToLoginActivity();
}
