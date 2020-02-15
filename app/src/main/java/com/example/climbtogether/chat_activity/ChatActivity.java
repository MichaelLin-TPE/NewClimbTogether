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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.climbtogether.R;
import com.example.climbtogether.chat_activity.chat_view_presenter.ViewPresenter;
import com.example.climbtogether.chat_activity.chat_view_presenter.ViewPresenterImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements ChatActivityVu{

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

    private static final String CHAT_DATA = "chat_data";

    private boolean isStillPosting;

    private int secondSize;

    private long countSecond = 0;

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

    /**
     * 以下的CODE 是點選空白處可以收起鍵盤 但是 這樣會導致 我按下送出鈕後,鍵盤也會縮小 不符合使用者體驗
     * 以後再想想要怎麼改
     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        if (ev.getAction() == MotionEvent.ACTION_DOWN){
//            View view = getCurrentFocus();
//            if (isShouldHideInput(view,ev)){
//                hideSoftInput(view.getWindowToken());
//            }
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }
//
//    private void hideSoftInput(IBinder windowToken) {
//        if (windowToken != null){
//            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (im != null){
//                im.hideSoftInputFromWindow(windowToken,InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//    }
//
//    private boolean isShouldHideInput(View view, MotionEvent event) {
//
//        if ( view != null && (view instanceof EditText)){
//            int[] l = {0,0};
//            view.getLocationInWindow(l);
//            int left = l[0],top = l[1],bottom = top + view.getHeight(),right = left+view.getWidth();
//
//            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom){
//                return false;
//            }else {
//                return true;
//            }
//        }
//
//        return false;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        checkChatDataChange();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStillPosting = false;
    }

    /**
     * countSecond 是拿來如果使用者惡意一直停留在這個畫面的話,就不會20秒後就不會繼續讀取 Firebase
     * 但是當有人在講話資料新增秒數將重新計算,減少流量
     */

    private void checkChatDataChange() {
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
                    Log.i("Michael","資料沒新增 : "+countSecond);
                    if (countSecond != 22500){
                        if (isStillPosting){
                            firestore.collection(DISCUSSION).document(listName).collection(CHAT_DATA)
                                    .orderBy("time" , Query.Direction.ASCENDING)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult() != null){
                                            ArrayList<String> chatArrayList = new ArrayList<>();
                                            for (QueryDocumentSnapshot document : task.getResult()){
                                                chatArrayList.add(document.getId());
                                            }
                                            int firstSize = chatArrayList.size();
                                            if (firstSize == secondSize || secondSize == 0){
                                                secondSize = firstSize;
                                                Log.i("Michael","長度相同");
                                            }else {
                                                countSecond = 0;
                                                presenter.onChangeData(user.getEmail());
                                                secondSize = firstSize;
                                            }
                                        }
                                    }
                                }
                            });
                        }else {
                            Log.i("Michael","結束迴圈");
                            break;
                        }
                    }else {
                        Log.i("Michael","結束迴圈");
                        break;
                    }

                }while (true);
            }
        }).start();
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
        Button btnSend = findViewById(R.id.chat_btn_send);
        recyclerView = findViewById(R.id.chat_recycler_view);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString();
                long currentTime = System.currentTimeMillis();
                presenter.onBtnSendClickListener(message,currentTime);
                editMessage.setText("");

                if (countSecond >= 22500){
                    checkChatDataChange();
                    countSecond = 0;
                }
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
    public void catchDataFormFirestore(String email) {
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
                                presenter.onShowRecyclerViewChangeData(chatDataArrayList);

                            }else {
                                Log.i("Michael","找尋不到任何資料");
                            }
                        }
                    }
                });
    }

    @Override
    public void reShowRecyclerView(ArrayList<ChatData> chatDataArrayList) {
        viewPresenter.setData(chatDataArrayList,user.getEmail());
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatDataArrayList.size()-1);
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
