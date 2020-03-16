package com.hiking.climbtogether.detail_activity;

import com.hiking.climbtogether.db_modle.DataDTO;

public interface DetailActivityVu {
    void closePage();

    void setToolbarTitle(String name);

    void setRecyclerView(DataDTO data);
}
