package com.hiking.climbtogether.tool;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public interface FirebaseHandler {

    boolean isLogin();
    
    String getUserEmail();
    
    void getUserData(String documentName , OnConnectFireStoreListener<FirestoreUserData> listener);

    void updateUserToken(String token);


    interface OnConnectFireStoreListener<T>{
        void onSuccess(T data);
        void onFail(String errorCode);
    }

}
