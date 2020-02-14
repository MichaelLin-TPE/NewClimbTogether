package com.example.climbtogether.chat_activity;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.climbtogether.R;
import com.example.climbtogether.chat_activity.chat_view_presenter.ViewPresenter;
import com.example.climbtogether.chat_activity.chat_view_presenter.ViewPresenterImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements ChatActivityVu{

    private String listName;

    private ChatActivityPresenter presenter;

    private EditText editMessage;

    private Button btnSend;

    private RecyclerView recyclerView;

    private ChatAdapter adapter;

    private FirebaseUser user;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private FirebaseFirestore firestore;

    private ViewPresenter viewPresenter;

    private static final String DISCUSSION = "discussion";

    private static final String CHAT_DATA = "chat_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initPresenter();
        initFirebase();
        initBundle();
        initView();
        adapter = new ChatAdapter(this,viewPresenter);
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        user = mAuth.getCurrentUser();
        if (user != null){
            if (user.getEmail() != null){
                presenter.onSearchChatData(user.getEmail());
            }
        }
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
        btnSend = findViewById(R.id.chat_btn_send);
        recyclerView = findViewById(R.id.chat_recycler_view);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString();
                long currentTime = System.currentTimeMillis();
                presenter.onBtnSendClickListener(message,currentTime);
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
        if (bundle != null){
            listName = bundle.getString("listName","");
            Log.i("Michael","進入 : "+listName);
        }
    }

    @Override
    public void searchChatDataFromFirestore(String email) {
        final ArrayList<ChatData> chatDataArrayList = new ArrayList<>();
        firestore.collection(DISCUSSION).document(listName).collection(CHAT_DATA).orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() != null){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Map<String , Object> map = document.getData();
                                    ChatData chatData = new ChatData();
                                    chatData.setEmail((String)map.get("email"));
                                    chatData.setTime((long)map.get("time"));
                                    chatData.setMessage((String)map.get("message"));
                                    chatData.setPhotoUrl((String)map.get("photo_url"));
                                    chatDataArrayList.add(chatData);
                                    Log.i("Michael","取得訊息 : "+chatDataArrayList.get(0).getMessage());
                                }
                                presenter.onCatchChatDataSuccessful(chatDataArrayList);

                            }else {
                                Log.i("Michael","找尋不到任何資料");
                            }
                        }
                    }
                });
    }

    @Override
    public void showProgressbar(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRecyclerView(ArrayList<ChatData> chatDataArrayList) {

        viewPresenter.setData(chatDataArrayList,user.getEmail());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(chatDataArrayList.size() -1);
    }

    @Override
    public void createChatDataToFirestore(String message, long currentTime) {
        Map<String,Object> map = new HashMap<>();
        map.put("time",currentTime);
        map.put("message",message);
        map.put("email",user.getEmail());
        map.put("photo_url","");
        firestore.collection(DISCUSSION).document(listName).collection(CHAT_DATA).document()
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            checkLoginStatus();
                        }
                    }
                });
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show();
    }
}
