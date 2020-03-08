package com.example.climbtogether.detail_activity;

import com.example.climbtogether.db_modle.DataDTO;

public interface DetailActivityVu {
    void closePage();

    void setToolbarTitle(String name);

    void setRecyclerView(DataDTO data);
}
