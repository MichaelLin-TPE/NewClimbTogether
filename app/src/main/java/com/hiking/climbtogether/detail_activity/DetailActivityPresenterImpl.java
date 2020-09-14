package com.hiking.climbtogether.detail_activity;

import com.google.firestore.v1.MapValueOrBuilder;
import com.google.gson.Gson;
import com.hiking.climbtogether.db_modle.DataDTO;

import java.util.ArrayList;

public class DetailActivityPresenterImpl implements DetailActivityPresenter {

    private DetailActivityVu mView;

    private Gson gson;

    private DataDTO data;

    private ArrayList<MountainFavoriteData> favArrayList;

    public DetailActivityPresenterImpl(DetailActivityVu mView) {
        this.mView = mView;
    }

    @Override
    public void onBackButtonClickListener() {
        mView.closePage();
    }

    @Override
    public void onPrepareData(DataDTO data) {
        this.data = data;
        mView.setToolbarTitle(data.getName());
        mView.setRecyclerView(data);
        gson = new Gson();
    }

    @Override
    public void onFavoriteItemClickListener(boolean isCheck) {
        if (isCheck){
            //資料新增

//            mView.uploadMtPhoto(data.getPhoto());

            mView.showFavoriteStatus(true);

        }else {
            //資料刪除
            int index = 0;
            for (MountainFavoriteData favData : favArrayList){
                if (favData.getName().equals(data.getName())){
                    favArrayList.remove(index);
                    break;
                }
                index++;
            }

            MountainObject object = new MountainObject();
            object.setDataArrayList(favArrayList);

            String jsonStr = gson.toJson(object);

            mView.saveFavoriteMt(jsonStr);



            mView.showFavoriteStatus(false);
        }
    }

    @Override
    public void onUploadPhotoSuccessful(String url) {
        if (favArrayList != null){
            MountainObject object = new MountainObject();
            MountainFavoriteData mtData = new MountainFavoriteData();
            mtData.setAllTitle(data.getAllTitle());
            mtData.setContent(data.getContent());
            mtData.setDay(data.getDay());
            mtData.setDifficulty(data.getDifficulty());
            mtData.setHeight(data.getHeight());
            mtData.setLocation(data.getLocation());
            mtData.setPhoto(url);
            mtData.setName(data.getName());
            mtData.setTime(data.getTime());
            favArrayList.add(mtData);
            object.setDataArrayList(favArrayList);
            String jsonStr = gson.toJson(object);
            mView.saveFavoriteMt(jsonStr);


        }else {
            MountainObject object = new MountainObject();
            ArrayList<MountainFavoriteData> dataArrayList = new ArrayList<>();
            MountainFavoriteData mtData = new MountainFavoriteData();
            mtData.setAllTitle(data.getAllTitle());
            mtData.setContent(data.getContent());
            mtData.setDay(data.getDay());
            mtData.setDifficulty(data.getDifficulty());
            mtData.setHeight(data.getHeight());
            mtData.setLocation(data.getLocation());
            mtData.setPhoto(url);
            mtData.setName(data.getName());
            mtData.setTime(data.getTime());
            dataArrayList.add(mtData);
            object.setDataArrayList(dataArrayList);
            String jsonStr = gson.toJson(object);
            mView.saveFavoriteMt(jsonStr);
        }

    }

    @Override
    public void onCatchJson(String jsonStr) {
        boolean isFavorite = false;
        if (jsonStr != null){
            MountainObject object = gson.fromJson(jsonStr, MountainObject.class);
            favArrayList = object.getDataArrayList();
            for (MountainFavoriteData favData : favArrayList){
                if (favData.getName().equals(data.getName())){
                    isFavorite = true;
                    mView.showIsFavorite(isFavorite);
                    break;
                }
            }
            if (!isFavorite){
                mView.showIsFavorite(isFavorite);
            }
        }else {
            mView.showIsFavorite(isFavorite);
        }
    }

    @Override
    public void onNoUserEvent() {
        mView.IntentToLoginActivity();
    }
}
