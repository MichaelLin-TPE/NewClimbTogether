package com.example.climbtogether.tool;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FireStoreManager{

    private FirebaseFirestore firestore;

    private String firstCollection,secondCollection;

    private String firstDocument,secondDocument;

    private Map<String,Object> map;

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public FireStoreManager(){
        firestore = FirebaseFirestore.getInstance();
    }

    public void setFirstCollection(String firstCollection){
        this.firstCollection = firstCollection;
    }

    public void setSecondCollection(String secondCollection) {
        this.secondCollection = secondCollection;
    }

    public void setFirstDocument(String firstDocument) {
        this.firstDocument = firstDocument;
    }

    public void setSecondDocument(String secondDocument) {
        this.secondDocument = secondDocument;
    }


    public void catchOneDocumentData(OnFirebaseDocumentListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                firestore.collection(firstCollection)
                        .document(firstDocument)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null){
                                    listener.onSuccess(task);
                                }
                            }
                        });
            }
        }).start();
    }

    public void catchTwoCollectionData(OnConnectingFirebaseListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                firestore.collection(firstCollection)
                        .document(firstDocument)
                        .collection(secondCollection)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null){
                                    listener.onSuccess(task);
                                }
                            }
                        });
            }
        }).start();
    }

    public void setDocumentData(OnFirebaseSetDocumentListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                firestore.collection(firstCollection)
                        .document(firstDocument)
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.i("Michael","Collection : "+firstCollection + " , document : "+firstDocument+" 資料新增成功");
                                    listener.onSuccessful();
                                }else {
                                    listener.onFailure();
                                }
                            }
                        });
            }
        }).start();
    }

    public void catchOneCollectionData(OnConnectingFirebaseListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                firestore.collection(firstCollection)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null){
                                    listener.onSuccess(task);
                                }
                            }
                        });
            }
        }).start();
    }

    public void deleteDocument(OnFirebaseDeleteListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                firestore.collection(firstCollection)
                        .document(firstDocument)
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    listener.onSuccessful();
                                }else {
                                    listener.onFailure();
                                }
                            }
                        });
            }
        }).start();
    }

    public interface OnFirebaseDocumentListener{
        void onSuccess(Task<DocumentSnapshot> task);
    }

    public interface OnFirebaseSetDocumentListener{
        void onSuccessful();

        void onFailure();
    }

    public interface OnFirebaseDeleteListener{
        void onSuccessful();
        void onFailure();
    }

    public interface OnConnectingFirebaseListener{
        void onSuccess(Task<QuerySnapshot> task);
    }
}
