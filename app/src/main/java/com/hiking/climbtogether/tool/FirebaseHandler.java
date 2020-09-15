package com.hiking.climbtogether.tool;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.detail_activity.MountainObject;

import java.util.ArrayList;
import java.util.Map;

public interface FirebaseHandler {

    boolean isLogin();
    
    String getUserEmail();
    
    void getUserData(String documentName , OnConnectFireStoreListener<FirestoreUserData> listener);

    void updateUserToken(String token);

    void OnCatchTwoCollectionData(String collectionMountain, String collection, String email, OnConnectFireStoreListener<Task<QuerySnapshot>> listener );

    void onSetSwoDocumentData(String collectionMountain, String collection, Map<String, Object> map, String mountainName);

    void onDeleteDocument(String collectionMountain, String collection, String name, OnConnectFireStoreSuccessfulListener listener);

    void onGetMountainApi(OnConnectFireStoreListener<ArrayList<DataDTO>> onCatchMountainApiListener);

    void onDeleteTopDocument(String mtName);

    void onSetTopMountainData(long time, DataDTO dataDTO);

    void onDeleteFavoriteData(String jsonStr);

    void onGetFavoriteData(OnConnectFireStoreListener<MountainObject> onGetFavoriteDataListener);

    void onSetFavorite(DataDTO data, OnConnectFireStoreSuccessfulListener onSetFavoriteDataListener);

    void getEquipmentApi(OnConnectFireStoreListener<ArrayList<EquipmentDTO>> onGetEquipmentApiListener);

    void onUpdateAndDeleteEquipmentData(EquipmentDTO equipmentDTO);


    interface OnConnectFireStoreListener<T>{
        void onSuccess(T data);
        void onFail(String errorCode);
    }

    interface OnConnectFireStoreSuccessfulListener{
        void onSuccess();
        void onFail(String errorCode);
    }

}
