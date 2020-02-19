package com.example.climbtogether.personal_chat_activity;

import androidx.annotation.NonNull;
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

import com.example.climbtogether.R;
import com.example.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenter;
import com.example.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenterImpl;
import com.example.climbtogether.tool.UserDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

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

    private static final String PERSONAL_CHAT = "personal_chat";

    private static final String CHAT_DATA = "chat_data";

    private ArrayList<PersonalChatData> chatArrayList;

    private PersonalAdapter adapter;

    private String documentPath;

    private boolean isStillPosting;

    private int countSecond = 0, secondSize =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
        initPresenter();
        initFirebase();
        initBundle();
        initView();
        searchChatData();

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
            }
        });
    }

    private void searchChatData() {

        chatArrayList = new ArrayList<>();
        if (user != null) {
            if (user.getEmail() != null) {

                documentPath = user.getEmail() + "And" + friendEmail;

                firestore.collection(PERSONAL_CHAT).document(documentPath).collection(CHAT_DATA)
                        .orderBy("time", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult() != null) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            PersonalChatData data = new PersonalChatData();
                                            data.setEmail((String)document.getData().get("email"));
                                            data.setMessage((String) document.getData().get("message"));
                                            data.setTime((long) document.getData().get("time"));
                                            chatArrayList.add(data);
                                        }
                                        if (chatArrayList.size() != 0) {
                                            presenter.onCatchChatData(chatArrayList);
                                        } else {
                                            searchAgain();
                                            Log.i("Michael", "沒資料做這");
                                        }
                                    }
                                }
                            }
                        });
            }
        }


    }

    private void searchAgain() {
        chatArrayList = new ArrayList<>();
        if (user != null) {
            if (user.getEmail() != null) {

                documentPath = friendEmail + "And" + user.getEmail();

                firestore.collection(PERSONAL_CHAT).document(documentPath).collection(CHAT_DATA)
                        .orderBy("time", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult() != null) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            PersonalChatData data = new PersonalChatData();
                                            data.setEmail((String)document.getData().get("email"));
                                            data.setMessage((String) document.getData().get("message"));
                                            data.setTime((long) document.getData().get("time"));
                                            chatArrayList.add(data);
                                        }
                                        if (chatArrayList.size() != 0) {
                                            presenter.onCatchChatData(chatArrayList);
                                        } else {
                                            Log.i("Michael", "沒資料做這");
                                        }
                                    }
                                }
                            }
                        });
            }
        }
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
        checkDataChange();

    }

    private void checkDataChange() {
        //這裡監控 回家做

        isStillPosting = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countSecond += 1500;
                    Log.i("Michael", "資料沒新增 : " + countSecond);
                    if (countSecond != 22500) {
                        if (isStillPosting) {
                            chatArrayList = new ArrayList<>();
                            firestore.collection(PERSONAL_CHAT).document(documentPath)
                                    .collection(CHAT_DATA)
                                    .orderBy("time", Query.Direction.ASCENDING)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    PersonalChatData data = new PersonalChatData();
                                                    data.setMessage((String) document.getData().get("message"));
                                                    data.setTime((long) document.getData().get("time"));
                                                    data.setEmail((String)document.getData().get("email"));
                                                    chatArrayList.add(data);
                                                }
                                                int firstSize = chatArrayList.size();
                                                if (firstSize == secondSize || secondSize == 0) {
                                                    Log.i("Michael", "資料數量相同");
                                                    secondSize = firstSize;
                                                } else {
                                                    countSecond = 0;
                                                    presenter.onDataChangeEvent(chatArrayList);
                                                    secondSize = firstSize;
                                                }
                                            }
                                        }
                                    });
                        } else {
                            Log.i("Michael", "結束迴圈");
                            break;
                        }
                    } else {
                        Log.i("Michael", "結束迴圈");
                        break;
                    }

                } while (true);
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        isStillPosting = false;
        countSecond = 0;
    }

    @Override
    public void createDataToFirestroe(String message, long time) {
        if (user.getEmail() != null || user != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("email",user.getEmail());
            map.put("message", message);
            map.put("time", time);
            firestore.collection(PERSONAL_CHAT).document(documentPath).collection(CHAT_DATA)
                    .document()
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("Michael", "資料新增成功");
                                if (countSecond >= 22500){
                                    countSecond = 0;
                                    checkDataChange();
                                }else{
                                    countSecond = 0;
                                }


                            }
                        }
                    });
        }

    }

    @Override
    public void setRecyclerView(ArrayList<PersonalChatData> chatArrayList) {
        if (user != null && user.getEmail() != null) {
            personalPresenter.setCurrentUserEmail(user.getEmail());
            personalPresenter.setFriendData(displayName, friendPhotoUrl);
        }

        personalPresenter.setData(chatArrayList);

        adapter = new PersonalAdapter(this, personalPresenter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(chatArrayList.size()-1);

    }

    @Override
    public void changeRecyclerView(ArrayList<PersonalChatData> chatArrayList) {
        personalPresenter.setData(chatArrayList);
        if (adapter == null){
            presenter.onCatchChatData(chatArrayList);
        }
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatArrayList.size() -1);
    }
}
