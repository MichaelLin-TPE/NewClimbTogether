package com.example.climbtogether.detail_activity.mt_presenter;

import com.example.climbtogether.db_modle.DataDTO;
import com.example.climbtogether.detail_activity.view.PhotoViewHolder;

public class MtPresenterImpl implements MtPresenter {

    public static final int PHOTO = 0;
    public static final int WEATHER = 1;
    public static final int INTRODUCE = 2;

    private DataDTO data;

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return PHOTO;
        }
        if (position == 1){
            return WEATHER;
        }
        if (position == 2){
            return INTRODUCE;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void setData(DataDTO data) {
        this.data = data;
    }

    @Override
    public void onBindPhotoViewHolder(PhotoViewHolder holder, int position) {
        holder.setData(data.getPhoto());
    }
}
