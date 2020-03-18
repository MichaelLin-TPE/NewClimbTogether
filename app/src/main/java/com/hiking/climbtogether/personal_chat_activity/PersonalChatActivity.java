package com.hiking.climbtogether.personal_chat_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hiking.climbtogether.MyFirbaseMessagingService;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.ChatRoomDTO;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.hiking.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenter;
import com.hiking.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenterImpl;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalChatActivity extends AppCompatActivity implements PersonalChatVu {

    private String displayName, friendEmail, friendPhotoUrl;

    private PersonalChatPresenter presenter;

    private FirebaseUser user;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private RecyclerView recyclerView;

    private EditText edMessage;

    private Button btnSend;

    private PersonalPresenter personalPresenter;

    private static final String CHAT_DATA = "chat_data";

    private static final String CHAT_ROOM = "chat_room";

    private PersonalAdapter adapter;

    private String testPath;

    private boolean isStillPosting;

    private UserDataManager userDataManager;

    private int countSecond = 0;

    private MyFirbaseMessagingService service;

    private static final String IS_UPDATE = "is_chat_update";

    private static final String UPDATE = "update";

    private String lastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
        service = new MyFirbaseMessagingService();
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
        btnSend = findViewById(R.id.personal_chat_btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
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
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
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
        isStillPosting = false;
        countSecond = 0;
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


    private void catchDocument(String message, long time) {
        if (testPath != null){
            presenter.sendMessage(message,time,testPath,userDataManager.getPhotoUrl(),userDataManager.getDisplayName(),friendEmail,displayName,friendPhotoUrl);
        }
    }
}
