package com.hiking.climbtogether.chat_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.chat_activity.chat_view_presenter.ViewPresenter;
import com.hiking.climbtogether.chat_activity.chat_view_presenter.ViewPresenterImpl;
import com.hiking.climbtogether.friend_manager_activity.ChatRoomDTO;
import com.hiking.climbtogether.personal_chat_activity.PersonalChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.hiking.climbtogether.tool.UserDataManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements ChatActivityVu {

    private String listName;

    private ChatActivityPresenter presenter;

    private EditText editMessage;

    private RecyclerView recyclerView;

    private ChatAdapter adapter;

    private FirebaseUser user;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private FirebaseFirestore firestore;

    private ViewPresenter viewPresenter;

    private static final String DISCUSSION = "discussion";

    private String displayName;

    private String friendPhotoUrl = "";

    private static final String FRIENDSHIP = "friendship";

    private static final String FRIEND = "friend";

    private static final String INVITE_FRIEND = "invite_friend";

    private static final String INVITE = "invite";

    private TextView tvInviteProcess;
    private ImageView ivAddFriend, ivFriend;

    private RoundedImageView ivPhoto;
    private TextView tvDisplayName;
    private TextView tvEmail;
    private LinearLayout chatClickArea;

    private ProgressBar dialogProgressbar;

    private boolean isInvited;
    private boolean isFriend;

    private String lastMessage;

    private UserDataManager userDataManager;

    private ArrayList<ChatRoomDTO> chatRoomArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userDataManager = new UserDataManager(this);
        initPresenter();
        initFirebase();
        searchChatRoom();
        initBundle();
        initView();
        user = mAuth.getCurrentUser();

        checkChatData();

        DocumentReference docRef = firestore.collection(DISCUSSION)
                .document("登山即時討論區");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    System.err.println("Listen failed : "+e);
                    return;
                }
                if (snapshot != null && snapshot.exists()){
                    String jsonStr = (String) snapshot.get("json");
                    presenter.onCatchNewChatData(jsonStr);
                    Log.i("Michael","每次更新 : "+jsonStr);
                }else {
                    Log.i("Michael","沒資料");

                }
            }
        });

        //在家測試
    }

    private void searchChatRoom() {
        firestore.collection("chat_room")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        chatRoomArrayList = new ArrayList<>();
                        if (task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                ChatRoomDTO data = new ChatRoomDTO();
                                data.setUser1((String)snapshot.get("user1"));
                                data.setUser2((String)snapshot.get("user2"));
                                data.setChatPath(snapshot.getId());
                                chatRoomArrayList.add(data);
                            }
                        }
                    }
                });
    }

    private void checkChatData() {
        firestore.collection(DISCUSSION)
                .document("登山即時討論區")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            DocumentSnapshot snapshot = task.getResult();
                            String jsonStr = (String)snapshot.get("json");
                            if (jsonStr != null){
                                Log.i("Michael","有聊天紀錄");
                                presenter.onCatchNewChatData(jsonStr);
                            }else {
                                Log.i("Michael","沒聊天紀錄");
                                presenter.onCatchNoChatData();
                            }
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void initView() {
        progressBar = findViewById(R.id.chat_progressbar);
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        toolbar.setTitle(listName);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editMessage = findViewById(R.id.chat_edit_message);
        ImageView btnSend = findViewById(R.id.chat_btn_send);
        recyclerView = findViewById(R.id.chat_recycler_view);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString();
                long currentTime = System.currentTimeMillis();
                presenter.onBtnSendClickListener(message, currentTime,userDataManager);
                editMessage.setText("");
            }
        });
    }

    private void initPresenter() {
        presenter = new ChatActivityPresenterImpl(this);
        viewPresenter = new ViewPresenterImpl();
    }

    private void initBundle() {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if (bundle != null) {
            listName = bundle.getString("listName", "");
        }
    }

    @Override
    public void catchDataFormFirestore(String email) {
        firestore.collection(DISCUSSION)
                .document("登山即時討論區")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            DocumentSnapshot snapshot = task.getResult();
                            String jsonStr = (String)snapshot.get("json");
                            if (jsonStr != null){
                                Log.i("Michael","有聊天紀錄");
                                presenter.onCatchNewChatData(jsonStr);
                            }else {
                                Log.i("Michael","沒聊天紀錄");
                                presenter.onCatchNoChatData();
                            }
                        }
                    }
                });
    }

    @Override
    public void reShowRecyclerView(ArrayList<ChatData> chatDataArrayList) {
        viewPresenter.setData(chatDataArrayList, user.getEmail());
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatDataArrayList.size() - 1);
    }

    @Override
    public void searchInfoFromFirebase(final String mail) {
        firestore.collection("users").document(mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> map = document.getData();
                                if (map != null) {
                                    displayName = (String) map.get("displayName");
                                    friendPhotoUrl = (String) map.get("photoUrl");
                                }
                                //查詢朋友狀況
                                if (user != null) {
                                    if (user.getEmail() != null) {
                                        presenter.onSearchFriendShip(user.getEmail(), mail);
                                    }
                                }
                            }
                        }
                    }
                });


    }

    @Override
    public void searchFriendShip(String email, final String strangeEmail) {
        firestore.collection(FRIENDSHIP).document(email).collection(FRIEND).document(strangeEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> map = document.getData();
                                if (map != null) {
                                    isFriend = true;
                                    Log.i("Michael", "是朋友");
                                } else {
                                    isFriend = false;
                                    Log.i("Michael", "不是朋友");
                                }
                                presenter.onSearchInvite(strangeEmail);
                            }
                        }
                    }
                });
    }

    @Override
    public void searchFriendInvite(final String strangeEmail) {
        Log.i("Michael", "查詢是否邀請過");
        firestore.collection(INVITE_FRIEND).document(strangeEmail).collection(INVITE).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (user != null && user.getEmail() != null) {

                                    if (user.getEmail().equals(document.getId())) {
                                        isInvited = true;
                                        Log.i("Michael", "曾經邀請過");
                                        break;
                                    }

                                }
                            }
                            presenter.onCatchUserData(displayName, friendPhotoUrl, strangeEmail, isFriend);
                        }
                    }
                });
    }

    @Override
    public void intentToPersonalChatActivity(String displayName, String mail, String photoUrl) {
        String path = "";
        for (ChatRoomDTO data : chatRoomArrayList){
            if (data.getUser1().equals(mail) && data.getUser2().equals(user.getEmail())){
                path = data.getChatPath();
                break;
            }else if (data.getUser1().equals(user.getEmail()) && data.getUser2().equals(mail)){
                path = data.getChatPath();
                break;
            }
        }
        Intent it = new Intent(this, PersonalChatActivity.class);
        it.putExtra("displayName", displayName);
        it.putExtra("mail", mail);
        it.putExtra("photoUrl", photoUrl);
        it.putExtra("path",path);
        startActivity(it);
    }


    @Override
    public void showUserDialog(final String displayName, final String photoUrl, final String mail, boolean isFriend) {
        dialogProgressbar.setVisibility(View.GONE);
        Log.i("Michael","isFriend : "+isFriend+" , isInvite : "+isInvited);
        chatClickArea.setVisibility(isFriend ? View.VISIBLE : View.GONE);

        tvInviteProcess.setVisibility(isInvited ? View.VISIBLE : View.GONE);

        if (isFriend){
            ivAddFriend.setVisibility(View.GONE);
        }else if (isInvited){
            ivAddFriend.setVisibility(View.GONE);
        }else {
            ivAddFriend.setVisibility(View.VISIBLE);
        }

//        ivAddFriend.setVisibility(isFriend & !isInvited ? View.GONE : View.VISIBLE);
//
//        if (isInvited){
//            ivAddFriend.setVisibility(View.GONE);
//        }

        ivFriend.setVisibility(isFriend ? View.VISIBLE : View.GONE);

        if (photoUrl == null || photoUrl.isEmpty()) {
            ivPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivPhoto.setImageResource(R.drawable.empty_photo);
        } else {
            ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            NewImageLoaderManager.getInstance(this).setPhotoUrl(photoUrl,ivPhoto);
        }
        tvDisplayName.setText(displayName);
        tvEmail.setText(mail);

        ivAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAddFriendClickListener(mail, user.getEmail());
            }
        });
        isInvited = false;

        chatClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onChatButtonClickListener(displayName, mail, photoUrl);
            }
        });


    }

    @Override
    public void setUserDialogViewChange(boolean isShow) {
        ivAddFriend.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvInviteProcess.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void sendInviteToStranger(String strangerEmail, String userEmail) {

        Map<String, Object> map = new HashMap<>();
        map.put("answer", "");
        firestore.collection(INVITE_FRIEND).document(strangerEmail).collection(INVITE).document(userEmail)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            presenter.onSendInviteSuccessful();
                        }
                    }
                });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void showProgressbar(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRecyclerView(ArrayList<ChatData> chatDataArrayList) {

        if (adapter != null){
            int messageIndex = chatDataArrayList.size() -1;
            if (!chatDataArrayList.get(messageIndex).getMessage().equals(lastMessage)){
                viewPresenter.setData(chatDataArrayList, user.getEmail());
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chatDataArrayList.size()-1);
                lastMessage = chatDataArrayList.get(messageIndex).getMessage();
            }
            return;
        }
        lastMessage = chatDataArrayList.get(chatDataArrayList.size() -1).getMessage();
        viewPresenter.setData(chatDataArrayList, user.getEmail());
        adapter = new ChatAdapter(this, viewPresenter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(chatDataArrayList.size() - 1);

        adapter.setOnUserPhotoClickListener(new ChatLeftViewHolder.OnUserPhotoClickListener() {
            @Override
            public void onClick(String mail) {
                Log.i("Michael", "點擊到了Item : " + mail);
                presenter.onUserPhotoClickListener(mail);
            }
        });
    }

    @Override
    public void showUserDialog() {
        View view = View.inflate(this, R.layout.user_dialog_custom_view, null);
        ivPhoto = view.findViewById(R.id.user_dialog_photo);
        tvDisplayName = view.findViewById(R.id.user_dialog_display_name);
        tvEmail = view.findViewById(R.id.user_dialog_email);
        chatClickArea = view.findViewById(R.id.user_dialog_chat_clickArea);
        tvInviteProcess = view.findViewById(R.id.user_dialog_invite_process);
        ivAddFriend = view.findViewById(R.id.user_dialog_add_user);
        ivFriend = view.findViewById(R.id.user_dialog_friend);
        dialogProgressbar = view.findViewById(R.id.user_dialog_progressbar);
        dialogProgressbar.setVisibility(View.VISIBLE);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialog.show();
        presenter.onSearUserData();

    }

    @Override
    public void createNewChatDataToFirebase(String jsonStr) {
        Map<String,Object> map = new HashMap<>();
        map.put("json",jsonStr);
        firestore.collection(DISCUSSION)
                .document("登山即時討論區")
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("Michael","該更新資料了");
                            updateView();
                        }
                    }
                });
    }
    private void updateView() {
        Map<String,Object> map = new HashMap<>();
        map.put("is_discuss_update",true);
        firestore.collection("is_chat_update")
                .document("update")
                .set(map, SetOptions.merge());
    }


    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }


}
