package com.example.climbtogether;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.climbtogether.home_activity.HomePageActivity;
import com.example.climbtogether.tool.UserDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainActivityVu {

    private MainActivityPresenter mainPresenter;

    private Toolbar toolbar;

    private UserDataManager userDataManager;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        initPresenter();
        initActionBar();
        Button btnTest = findViewById(R.id.main_test_btn);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.onBtnTestClickListener();
            }
        });

        userDataManager = new UserDataManager(this);

        //申請推播TOKEN
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            String token = task.getResult().getToken();

                            Log.i("Michael","new Token : "+token);
                            userDataManager.saveNotificationToken(token);
                            updateUserToken(token);
                        }

                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = "default_notification_channel_name";
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_LOW));
        }


        saveCurrentUserData();


    }

    private void updateUserToken(String token) {
        if (user != null && user.getEmail() != null){
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            firestore.collection("users").document(user.getEmail())
                    .set(map, SetOptions.merge());
        }
    }

    private void saveCurrentUserData() {


        if (user != null && user.getEmail() != null){
            firestore.collection("users")
                    .document(user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                DocumentSnapshot snapshot = task.getResult();
                                userDataManager.saveUserData((String)snapshot.get("mail"),(String)snapshot.get("displayName"),(String)snapshot.get("photoUrl"));
                            }
                        }
                    });
        }
    }

    private void initActionBar() {
        toolbar = findViewById(R.id.first_page_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initPresenter() {
        mainPresenter = new MainActivityPresenterImpl(this);
    }

    @Override
    public void intentToHomePageActivity() {
        Intent it = new Intent(this, HomePageActivity.class);
        startActivity(it);
    }
}
