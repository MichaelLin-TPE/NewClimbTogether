package com.hiking.climbtogether;

import com.google.firebase.firestore.DocumentSnapshot;
import com.hiking.climbtogether.tool.FirebaseHandler;
import com.hiking.climbtogether.tool.FirebaseHandlerImpl;
import com.hiking.climbtogether.tool.FirestoreUserData;

public class MainActivityPresenterImpl implements MainActivityPresenter {

    private MainActivityVu mView;

    private FirebaseHandler firebaseHandler;

    private static final String USER = "users";


    public MainActivityPresenterImpl(MainActivityVu mView){
        this.mView = mView;
        firebaseHandler = new FirebaseHandlerImpl();
    }

    @Override
    public void onApplyToken() {
        mView.applyToken();
    }

    @Override
    public void onSaveCurrentUserData() {

        if (firebaseHandler.isLogin()){
            firebaseHandler.getUserData(USER, new FirebaseHandler.OnConnectFireStoreListener<FirestoreUserData>() {
                @Override
                public void onSuccess(FirestoreUserData data) {
                    mView.saveCurrentUserData(data);
                }

                @Override
                public void onFail(String errorCode) {
                    mView.showErrorDialog(errorCode);
                }
            });
        }


    }

    @Override
    public void onUpdateUserToken(String token) {
        if (firebaseHandler.isLogin()){
            firebaseHandler.updateUserToken(token);
        }

    }
}
