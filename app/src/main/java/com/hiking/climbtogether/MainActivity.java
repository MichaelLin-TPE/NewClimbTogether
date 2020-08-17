package com.hiking.climbtogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hiking.climbtogether.home_activity.HomePageActivity;
import com.hiking.climbtogether.tool.UserDataManager;
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
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainActivityVu {

    private MainActivityPresenter mainPresenter;

    private UserDataManager userDataManager;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private FirebaseFirestore firestore;

    private int permission;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        initPresenter();
        userDataManager = new UserDataManager(this);

        mainPresenter.onApplyToken();

        //先判斷動態權限
        new Thread(new Runnable() {
            @Override
            public void run() {
                verifyStoragePermissions(MainActivity.this);
            }
        }).start();


    }

    private void verifyStoragePermissions(MainActivity mainActivity) {
        try {
            permission = ActivityCompat.checkSelfPermission(mainActivity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mainActivity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

            }else {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent it = new Intent(MainActivity.this,HomePageActivity.class);
                startActivity(it);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //開啟通知權限
                    NotificationManagerCompat manager = NotificationManagerCompat.from(MainActivity.this);
                    boolean isEnable = manager.areNotificationsEnabled();
                    if (!isEnable) {

                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.permission))
                                .setMessage(getString(R.string.is_open_notification))
                                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, getApplicationInfo().uid);
                                            startActivity(intent);
                                        } else {
                                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                                toSystemConfig();
                                            } else {
                                                try {
                                                    toApplicationInfo();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    toSystemConfig();
                                                }
                                            }
                                        }
                                    }
                                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create();
                        dialog.show();
                    }else {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent it = new Intent(MainActivity.this,HomePageActivity.class);
                        startActivity(it);
                        finish();
                    }
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }
                break;
            default:
                break;
        }
    }

    private void toApplicationInfo() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
    }

    private void toSystemConfig() {
        try {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCurrentUserData() {
        if (user != null && user.getEmail() != null) {
            firestore.collection("users")
                    .document(user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot snapshot = task.getResult();
                                userDataManager.saveUserData((String) snapshot.get("email"), (String) snapshot.get("displayName"), (String) snapshot.get("photoUrl"));
                            }
                        }
                    });

        }
    }


    private void initPresenter() {
        mainPresenter = new MainActivityPresenterImpl(this);
    }

    @Override
    public void applyToken() {
        //申請推播TOKEN
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String token = task.getResult().getToken();

                            Log.i("Michael", "new Token : " + token);
                            userDataManager.saveNotificationToken(token);
                            updateUserToken(token);
                            mainPresenter.onSaveCurrentUserData();
                        }

                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = "default_notification_channel_name";
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW));
        }
    }

    private void updateUserToken(String token) {
        if (user != null && user.getEmail() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            firestore.collection("users").document(user.getEmail())
                    .set(map, SetOptions.merge());
        }
    }

}
