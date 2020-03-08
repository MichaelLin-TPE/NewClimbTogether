package com.example.climbtogether.detail_activity.mt_presenter;

import com.example.climbtogether.db_modle.DataDTO;
import com.example.climbtogether.detail_activity.view.PhotoViewHolder;

public interface MtPresenter {
    int getItemViewType(int position);

    int getItemCount();

    void setData(DataDTO data);

    void onBindPhotoViewHolder(PhotoViewHolder holder, int position);
}
