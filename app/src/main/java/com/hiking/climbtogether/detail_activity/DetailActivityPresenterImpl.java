package com.hiking.climbtogether.detail_activity;

import android.util.Log;

import com.google.firestore.v1.MapValueOrBuilder;
import com.google.gson.Gson;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.tool.FirebaseHandler;
import com.hiking.climbtogether.tool.FirebaseHandlerImpl;

import java.util.ArrayList;

public class DetailActivityPresenterImpl implements DetailActivityPresenter {

    private DetailActivityVu mView;

    private Gson gson;

    private DataDTO data;

    private ArrayList<MountainFavoriteData> favArrayList;

    private FirebaseHandler firebaseHandler;

    public DetailActivityPresenterImpl(DetailActivityVu mView) {
        this.mView = mView;
        firebaseHandler = new FirebaseHandlerImpl();
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
        if (!firebaseHandler.isLogin()){
            mView.showFavoriteStatus(false);
            return;
        }
        firebaseHandler.onGetFavoriteData(onGetFavoriteDataListener);

    }

    private FirebaseHandler.OnConnectFireStoreListener<MountainObject> onGetFavoriteDataListener = new FirebaseHandler.OnConnectFireStoreListener<MountainObject>() {
        @Override
        public void onSuccess(MountainObject mountainObject) {
            favArrayList = mountainObject.getDataArrayList();

            if (favArrayList == null || favArrayList.isEmpty()){
                mView.showErrorCode("我的最愛資料取得失敗,請稍後再試.");
                return;
            }
            mView.showFavoriteStatus(false);
            for (MountainFavoriteData favData : favArrayList) {
                if (favData.getName().equals(data.getName())) {
                    mView.showFavoriteStatus(true);
                    break;
                }
            }
        }

        @Override
        public void onFail(String errorCode) {
            mView.showErrorCode(errorCode);
        }
    };

    @Override
    public void onFavoriteItemClickListener(boolean isCheck) {
        Log.i("Michael","是否按了 : "+isCheck);

        if (!firebaseHandler.isLogin()){
            mView.intentToLoginActivity();
            return;
        }

        if (isCheck) {
            //資料新增

//            mView.uploadMtPhoto(data.getPhoto());
            firebaseHandler.onSetFavorite(data,onSetFavoriteDataListener);

            mView.showFavoriteStatus(true);
            return;

        }
        //資料刪除
        int index = 0;
        for (MountainFavoriteData favData : favArrayList) {
            if (favData.getName().equals(data.getName())) {
                favArrayList.remove(index);
                break;
            }
            index++;
        }

        MountainObject object = new MountainObject();
        object.setDataArrayList(favArrayList);

        String jsonStr = gson.toJson(object);


        firebaseHandler.onDeleteFavoriteData(jsonStr);


        mView.showFavoriteStatus(false);

    }

    private FirebaseHandler.OnConnectFireStoreSuccessfulListener onSetFavoriteDataListener = new FirebaseHandler.OnConnectFireStoreSuccessfulListener() {
        @Override
        public void onSuccess() {
            mView.showToast("我的最愛新增成功");
        }

        @Override
        public void onFail(String errorCode) {
            mView.showErrorCode(errorCode);
        }
    };

}
