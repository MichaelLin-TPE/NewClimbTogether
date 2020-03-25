package com.hiking.climbtogether.personal_chat_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hiking.climbtogether.MyFirbaseMessagingService;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.ChatRoomDTO;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.hiking.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenter;
import com.hiking.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenterImpl;
import com.hiking.climbtogether.photo_activity.PhotoActivity;
import com.hiking.climbtogether.share_activity.DialogViewPagerAdapter;
import com.hiking.climbtogether.share_activity.ShareActivity;
import com.hiking.climbtogether.tool.GlideEngine;
import com.hiking.climbtogether.tool.UserDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalChatActivity extends AppCompatActivity implements PersonalChatVu {

    private String displayName, friendEmail, friendPhotoUrl;

    private PersonalChatPresenter presenter;

    private FirebaseUser user;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private RecyclerView recyclerView;

    private EditText edMessage;

    private ImageView ivSend;

    private PersonalPresenter personalPresenter;

    private static final String CHAT_DATA = "chat_data";

    private static final String CHAT_ROOM = "chat_room";

    private PersonalAdapter adapter;

    private String testPath;

    private UserDataManager userDataManager;

    private StorageReference storage;

    private ImageView ivSendPhoto,ivCamera;

    private ArrayList<Bitmap> bitmapArrayList;

    private ArrayList<byte[]> photoBytesArray;

    private int uploadPhotoCount;

    private ArrayList<String> downloadUrlArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
        userDataManager = new UserDataManager(this);
        initPresenter();
        initFirebase();
        initBundle();
        initView();
        //新方法
        searchChatPath();
        if (testPath != null){
            DocumentReference reference = firestore.collection(CHAT_DATA)
                    .document(testPath);
            reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        System.err.println("Listen failed: " + e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()){
                        Log.i("Michael","該更新了");
                        String jsonStr = (String) snapshot.get("json");
                        presenter.onCatchChatJson(jsonStr);
                    }
                }
            });
        }


    }
    private void searchChatPath() {
        if (testPath != null && !testPath.isEmpty()){
            searchNewChatData(testPath);
        }else {
            firestore.collection(CHAT_ROOM)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                ArrayList<ChatRoomDTO> pathArray = new ArrayList<>();

                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    ChatRoomDTO data = new ChatRoomDTO();
                                    data.setDocument(snapshot.getId());
                                    data.setUser1((String) snapshot.get("user1"));
                                    data.setUser2((String) snapshot.get("user2"));
                                    pathArray.add(data);
                                }
                                if (pathArray.size() != 0) {
                                    for (ChatRoomDTO data : pathArray) {
                                        if (data.getUser1().equals(user.getEmail()) && data.getUser2().equals(friendEmail)) {
                                            testPath = data.getDocument();
                                            searchNewChatData(testPath);
                                            Log.i("Michael","有房間");
                                            break;
                                        }else if (data.getUser1().equals(friendEmail) && data.getUser2().equals(user.getEmail())){
                                            testPath = data.getDocument();
                                            searchNewChatData(testPath);
                                            Log.i("Michael","有房間");
                                            break;
                                        }
                                    }

                                }
                            }
                        }
                    });
        }

    }

    private void searchNewChatData(String documentPath) {
        firestore.collection(CHAT_DATA)
                .document(documentPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            DocumentSnapshot snapshot = task.getResult();
                            String jsonStr = (String)snapshot.get("json");
                            if (jsonStr != null && !jsonStr.isEmpty()){
                                presenter.onCatchChatJson(jsonStr);
                            }else {
                                Log.i("Michael","無聊天資料 待處理");
                            }
                        }
                    }
                });
    }

    private void initView() {
        ivSendPhoto = findViewById(R.id.personal_iv_send_photo);
        ivCamera = findViewById(R.id.personal_iv_send_camera);
        Toolbar toolbar = findViewById(R.id.personal_chat_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(displayName);
        getSupportActionBar().setTitle(displayName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBackButtonClickListener();
            }
        });
        recyclerView = findViewById(R.id.personal_chat_recycler_view);
        edMessage = findViewById(R.id.personal_chat_edit_message);
        ivSend = findViewById(R.id.personal_chat_btn_send);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edMessage.getText().toString();
                if (message.isEmpty()) {
                    message = getString(R.string.empty_message);
                    presenter.onShowErrorToast(message);
                    return;
                }
                edMessage.setText("");
                long time = System.currentTimeMillis();
                presenter.onSendMessageButtonClickListener(message, time);
                presenter.onSendNotificationToFriend(friendEmail, message, displayName);
            }
        });


        //傳送照片
        ivSendPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendPhotoButtonClickListener();
            }
        });

        //拍照傳送照片
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCameraButtonClickListener();
            }
        });
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        user = mAuth.getCurrentUser();
    }

    private void initBundle() {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if (bundle != null) {
            displayName = bundle.getString("displayName", "");
            friendEmail = bundle.getString("mail", "");
            friendPhotoUrl = bundle.getString("photoUrl", "");
            testPath = bundle.getString("path","");
            Log.i("Michael", "綽號 : " + displayName + " , EMAIL : " + friendEmail + " , photoUrl : " + friendPhotoUrl);
        }
    }

    private void initPresenter() {
        presenter = new PersonalChatPresenterImpl(this);

        personalPresenter = new PersonalPresenterImpl();
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void setRecyclerView(ArrayList<PersonalChatData> chatArrayList) {

        if (adapter != null){
            personalPresenter.setData(chatArrayList);
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(chatArrayList.size()-1);
            return;
        }

        if (user != null && user.getEmail() != null) {
            personalPresenter.setCurrentUserEmail(user.getEmail());
            personalPresenter.setFriendData(displayName, friendPhotoUrl);
        }

        personalPresenter.setData(chatArrayList);

        adapter = new PersonalAdapter(this, personalPresenter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(chatArrayList.size() - 1);

        adapter.setOnPhotoClickListenr(new PersonalChatLeftViewHolder.OnPhotoClickListenr() {
            @Override
            public void onClick(String downLoadUrl) {
                presenter.onPhotoClickListener(downLoadUrl);
            }
        });

    }

    @Override
    public void changeRecyclerView(ArrayList<PersonalChatData> chatArrayList) {
        personalPresenter.setData(chatArrayList);
        if (adapter == null) {
            presenter.onCatchChatData(chatArrayList);
        }
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatArrayList.size() - 1);
    }

    @Override
    public void searchFriendData(String friendEmail, final String message, final String displayName) {
        firestore.collection("users").document(friendEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot snapshot = task.getResult();
                            String token = (String) snapshot.get("token");
                            if (token != null) {
                                presenter.onPostFcmToFriend(token, message, displayName);
                            }
                        }
                    }
                });
    }

    @Override
    public String getDisplayName() {
        return new UserDataManager(this).getDisplayName();
    }

    @Override
    public void setDataToFireStore(String message, long time) {
        if (testPath != null){
            catchDocument(message,time);
        }
    }

    @Override
    public String getEmail() {
        return user != null ? user.getEmail() : "";
    }

    @Override
    public void setChatDataToFireStore(String jsonStr) {
        Map<String,Object> map = new HashMap<>();
        map.put("json",jsonStr);

        firestore.collection(CHAT_DATA)
                .document(testPath)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("Michael","上傳成功");
                        }
                    }
                });
    }

    @Override
    public void showPhotoPage() {
        PictureSelector.create(PersonalChatActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(2)
                .compress(true)
                .enableCrop(true)
                .hideBottomControls(false)
                .showCropFrame(false)
                .freeStyleCropEnabled(true)
                .forResult(new OnResultCallbackListener() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        bitmapArrayList = new ArrayList<>();
                        photoBytesArray = new ArrayList<>();
                        for (int i = 0; i < result.size(); i++) {
                            File file = new File(result.get(i).getCutPath());
                            Uri uri = Uri.fromFile(file);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                bitmapArrayList.add(bitmap);
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmapArrayList.get(i).getByteCount());
                                bitmapArrayList.get(i).compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
                                photoBytesArray.add(outputStream.toByteArray());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (photoBytesArray.size() != 0){
                            Log.i("Michael","照片有 : "+photoBytesArray.size()+" 張");
                            uploadPhotoCount = 0;
                            presenter.onCatchAllPhoto(photoBytesArray);
                        }
                    }
                });
    }
    @Override
    public void showCamera() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .compress(true)
                .forResult(new OnResultCallbackListener() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        bitmapArrayList = new ArrayList<>();
                        photoBytesArray = new ArrayList<>();
                        for (int i = 0; i < result.size(); i++) {

                            File file = new File(result.get(i).getCompressPath());
                            Uri uri = Uri.fromFile(file);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                bitmapArrayList.add(bitmap);
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmapArrayList.get(i).getByteCount());
                                bitmapArrayList.get(i).compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
                                photoBytesArray.add(outputStream.toByteArray());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (photoBytesArray.size() != 0){
                            Log.i("Michael","照片有 : "+photoBytesArray.size()+" 張");
                            uploadPhotoCount = 0;
                            presenter.onCatchAllPhoto(photoBytesArray);
                        }
                    }
                });
    }

    @Override
    public void uploadPhoto(ArrayList<byte[]> photoBytesArray) {
        downloadUrlArray = new ArrayList<>();
        presenter.onShowProgressMessage(getString(R.string.uploading));
        upLoadPhotoToStorage();

    }

    @Override
    public void showErrorCode(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void intentToPhotoActivity(String downLoadUrl) {
        Intent it = new Intent(this, PhotoActivity.class);
        it.putExtra("photoUrl",downLoadUrl);
        startActivity(it);
    }



    private void upLoadPhotoToStorage() {
        if (uploadPhotoCount < photoBytesArray.size()){
            long randomNumber = (long) Math.floor(Math.random()*100000);
            StorageReference river = storage.child(userDataManager.getEmail()+"/"+"PERSONAL_CHAT"+"/"+randomNumber+".jpg");
            UploadTask task = river.putBytes(photoBytesArray.get(uploadPhotoCount));
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    river.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrlArray.add(uri.toString());
                                    uploadPhotoCount ++;
                                    upLoadPhotoToStorage();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    presenter.onCatchUploadError(e.toString());
                }
            });
        }else {
            long time = System.currentTimeMillis();
            presenter.onCatchAllPhotoUrl("",time,testPath,userDataManager.getPhotoUrl(),userDataManager.getDisplayName(),friendEmail,displayName,friendPhotoUrl,downloadUrlArray);
            PictureFileUtils.deleteAllCacheDirFile(this);
        }
    }


    private void catchDocument(String message, long time) {
        if (testPath != null){
            presenter.sendMessage(message,time,testPath,userDataManager.getPhotoUrl(),userDataManager.getDisplayName(),friendEmail,displayName,friendPhotoUrl);
        }
    }
}
