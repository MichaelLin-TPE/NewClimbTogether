package com.hiking.climbtogether.tool;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHandlerImpl implements FirebaseHandler {

    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore;

    private FirebaseUser user;

    private static final String USER = "users";

    public FirebaseHandlerImpl(){
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
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
                        if (!task.isSuccessful() || task.getResult() == null){
                            listener.onFail("FirebaseStore connect failed");
                            return;
                        }
                        DocumentSnapshot snapshot = task.getResult();

                        FirestoreUserData data = snapshot.toObject(FirestoreUserData.class);

                        if (data == null){
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
}
