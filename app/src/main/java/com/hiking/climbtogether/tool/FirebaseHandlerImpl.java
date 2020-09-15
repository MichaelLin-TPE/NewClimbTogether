package com.hiking.climbtogether.tool;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.detail_activity.MountainFavoriteData;
import com.hiking.climbtogether.detail_activity.MountainObject;
import com.hiking.climbtogether.mountain_fragment.MtTopData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHandlerImpl implements FirebaseHandler {

    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore;

    private static final String MT_API = "mt_api";

    private FirebaseUser user;

    private static final String USER = "users";

    private static final String COLLECTION_MOUNTAIN = "collection_mountain";

    private static final String COLLECTION = "collection";

    private static final String FAVORITE = "favorite";

    private MountainObject mountainObject;

    private OnConnectFireStoreSuccessfulListener favoriteDataListener;

    private Gson gson;

    public FirebaseHandlerImpl() {
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        gson = new Gson();
    }

    @Override
    public boolean isLogin() {
        return user != null;
    }

    @Override
    public String getUserEmail() {
        return user.getEmail();
    }

    @Override
    public void getUserData(String documentName, OnConnectFireStoreListener<FirestoreUserData> listener) {

        firebaseFirestore.collection(documentName)
                .document(getUserEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful() || task.getResult() == null) {
                            listener.onFail("FirebaseStore connect failed");
                            return;
                        }
                        DocumentSnapshot snapshot = task.getResult();

                        FirestoreUserData data = snapshot.toObject(FirestoreUserData.class);

                        if (data == null) {
                            listener.onFail("Get Firebase Data Failed");
                        }

                        listener.onSuccess(data);
                    }
                });
    }


    @Override
    public void updateUserToken(String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        firebaseFirestore.collection(USER).document(getUserEmail())
                .set(map, SetOptions.merge());
    }

    @Override
    public void OnCatchTwoCollectionData(String firstCollection, String secondCollection, String email, OnConnectFireStoreListener<Task<QuerySnapshot>> listener) {
        firebaseFirestore.collection(firstCollection)
                .document(email)
                .collection(secondCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.getResult() == null) {
                            listener.onFail("取得資料失敗");
                            return;
                        }
                        if (!task.isSuccessful()) {
                            listener.onFail("取得資料失敗");
                            return;
                        }
                        listener.onSuccess(task);

                    }
                });
    }

    @Override
    public void onSetSwoDocumentData(String firstCollection, String secondCollection, Map<String, Object> map, String mountainName) {
        firebaseFirestore.collection(firstCollection)
                .document(getUserEmail())
                .collection(secondCollection)
                .document(mountainName)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Michael", "Collection : " + firstCollection + " , document : " + getUserEmail() + " 資料新增成功");
                        }
                    }
                });
    }

    @Override
    public void onDeleteDocument(String firstCollection, String secondCollection, String name, OnConnectFireStoreSuccessfulListener listener) {
        firebaseFirestore.collection(firstCollection)
                .document(getUserEmail())
                .collection(secondCollection)
                .document(name)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Michael", "刪除成功");
                            listener.onSuccess();
                        } else {
                            listener.onFail("刪除資料失敗");
                        }
                    }
                });

    }

    @Override
    public void onGetMountainApi(OnConnectFireStoreListener<ArrayList<DataDTO>> onCatchMountainApiListener) {

        if (isLogin()) {

            firebaseFirestore.collection(MT_API)
                    .document(MT_API)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.isSuccessful() || task.getResult() == null) {
                                onCatchMountainApiListener.onFail("取得API資料失敗");
                                return;
                            }

                            DocumentSnapshot snapshot = task.getResult();

                            if (snapshot == null || snapshot.getData() == null) {
                                onCatchMountainApiListener.onFail("取得API資料失敗");
                                return;
                            }
                            String apiJson = (String) snapshot.getData().get("json");
                            ArrayList<DataDTO> mtDataArray = gson.fromJson(apiJson, new TypeToken<ArrayList<DataDTO>>() {
                            }.getType());

                            if (mtDataArray == null) {
                                onCatchMountainApiListener.onFail("取得API資料失敗");
                                return;
                            }
                            checkUserData(mtDataArray, onCatchMountainApiListener);
                        }
                    });
            return;
        }
        firebaseFirestore.collection(MT_API)
                .document(MT_API)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() == null) {
                            onCatchMountainApiListener.onFail("取得API資料失敗 task.getResult == null");
                            return;
                        }
                        DocumentSnapshot snapshot = task.getResult();

                        if (snapshot == null || snapshot.getData() == null) {
                            onCatchMountainApiListener.onFail("取得API資料失敗 snapshot == null");
                            return;
                        }
                        String apiJson = (String) snapshot.getData().get("json");
                        ArrayList<DataDTO> mtDataArray = gson.fromJson(apiJson, new TypeToken<ArrayList<DataDTO>>() {
                        }.getType());

                        if (mtDataArray == null) {
                            onCatchMountainApiListener.onFail("取得API資料失敗 mtDataArray == null");
                            return;
                        }
                        onCatchMountainApiListener.onSuccess(mtDataArray);
                    }
                });
    }

    @Override
    public void onDeleteTopDocument(String mtName) {
        firebaseFirestore.collection(COLLECTION_MOUNTAIN)
                .document(getUserEmail())
                .collection(COLLECTION)
                .document(mtName)
                .delete();
    }

    @Override
    public void onSetTopMountainData(long time, DataDTO dataDTO) {

        Map<String, Object> map = new HashMap<>();
        map.put("name", dataDTO.getName());
        map.put("photoUrl", dataDTO.getphoto());
        map.put("sid", dataDTO.getSid());
        map.put("topTime", dataDTO.getTime());

        firebaseFirestore.collection(COLLECTION_MOUNTAIN)
                .document(getUserEmail())
                .collection(COLLECTION)
                .document(dataDTO.getName())
                .set(map);
    }

    @Override
    public void onDeleteFavoriteData(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("json", jsonStr);
        firebaseFirestore.collection(FAVORITE)
                .document(getUserEmail())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Michael", "更新成功");
                        }
                    }
                });
    }

    @Override
    public void onGetFavoriteData(OnConnectFireStoreListener<MountainObject> onGetFavoriteDataListener) {
        firebaseFirestore.collection(FAVORITE)
                .document(getUserEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (!task.isSuccessful() || task.getResult() == null) {

                            return;
                        }
                        DocumentSnapshot snapshot = task.getResult();
                        String jsonStr = (String) snapshot.get("json");
                        mountainObject = gson.fromJson(jsonStr,MountainObject.class);

                        if (mountainObject == null){
                            Log.i("Michael","mountainObject is null");
                            return;
                        }
                        onGetFavoriteDataListener.onSuccess(mountainObject);

                    }
                });
    }

    @Override
    public void onSetFavorite(DataDTO data, OnConnectFireStoreSuccessfulListener onSetFavoriteDataListener) {
        this.favoriteDataListener = onSetFavoriteDataListener;

        MountainFavoriteData favoriteData = new MountainFavoriteData();
        favoriteData.setAllTitle(data.getAllTitle());
        favoriteData.setContent(data.getContent());
        favoriteData.setDay(data.getDay());
        favoriteData.setDifficulty(data.getDifficulty());
        favoriteData.setHeight(data.getHeight());
        favoriteData.setTime(data.getTime());
        favoriteData.setLocation(data.getLocation());
        favoriteData.setName(data.getName());
        favoriteData.setUserPhoto(data.getphoto());


        if (mountainObject == null){

            mountainObject = new MountainObject();
            ArrayList<MountainFavoriteData> favoriteArray = new ArrayList<>();
            favoriteArray.add(favoriteData);
            mountainObject.setDataArrayList(favoriteArray);
            String json = gson.toJson(mountainObject);
            Map<String,Object> map = new HashMap<>();
            map.put("json",json);
            firebaseFirestore.collection(FAVORITE)
                    .document(getUserEmail())
                    .set(map)
                    .addOnCompleteListener(onFirebaseFavoriteDataListener);
            return;
        }

        mountainObject.getDataArrayList().add(favoriteData);

        String json = gson.toJson(mountainObject);
        Map<String, Object> map = new HashMap<>();
        map.put("json",json);
        firebaseFirestore.collection(FAVORITE)
                .document(getUserEmail())
                .set(map)
                .addOnCompleteListener(onFirebaseFavoriteDataListener);

    }

    private OnCompleteListener<Void> onFirebaseFavoriteDataListener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                favoriteDataListener.onSuccess();
                return;
            }
            favoriteDataListener.onFail("新增我的最愛失敗");
        }
    };

    private void checkUserData(ArrayList<DataDTO> mtDataArray, OnConnectFireStoreListener<ArrayList<DataDTO>> onCatchMountainApiListener) {
        firebaseFirestore.collection(COLLECTION_MOUNTAIN)
                .document(getUserEmail())
                .collection(COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful() || task.getResult() == null) {
                            onCatchMountainApiListener.onSuccess(mtDataArray);
                            return;
                        }
                        for (DataDTO dataDTO : mtDataArray) {
                            for (DocumentSnapshot snapshots : task.getResult()) {
                                MtTopData mtTopData = snapshots.toObject(MtTopData.class);
                                if (mtTopData == null) {
                                    continue;
                                }
                                if (mtTopData.getName().equals(dataDTO.getName())) {
                                    dataDTO.setCheck("true");
                                    dataDTO.setTime(mtTopData.getTopTime());
                                }
                            }
                        }
                        onCatchMountainApiListener.onSuccess(mtDataArray);
                    }
                });
    }
}
