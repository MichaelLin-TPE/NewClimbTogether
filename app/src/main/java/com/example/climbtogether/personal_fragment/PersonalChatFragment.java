package com.example.climbtogether.personal_fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;
import com.example.climbtogether.login_activity.LoginActivity;
import com.example.climbtogether.personal_chat_activity.PersonalChatActivity;
import com.example.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.example.climbtogether.tool.FireStoreManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PersonalChatFragment extends Fragment implements PersonalFragmentVu {

    private PersonalFragmentPresenter presenter;

    private Context context;

    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private FireStoreManager manager;

    private FirebaseUser user;

    private ImageView ivLogo;

    private TextView tvNotice;

    private ProgressBar progressBar;

    private Button btnLogin;

    private static final String PERSONAL_CHAT = "personal_chat";

    private static final String CHAT_DATA = "chat_data";

    private static final String FRIENDSHIP = "friendship";

    private static final String FRIEND = "friend";

    private static final String IS_UPDATE = "is_chat_update";

    private static final String UPDATE = "update";

    private int searchCount = 0, searchCountOnResume = 0;

    private ArrayList<PersonalChatDTO> chatDataArrayList;

    private ArrayList<String> friendsArrayList;

    private String currentEmail;

    private int itemPosition;

    private PersonalFragmentAdapter adapter;

    private DataBaseApi dataBaseApi;

    private boolean isFirstSearchData, isConnecting;

    private ArrayList<FriendData> friendDataArray;

    private ArrayList<String> roomArray;

    private int friendCount;

    private ArrayList<String> jsonArray;

    public static PersonalChatFragment newInstance() {
        PersonalChatFragment fragment = new PersonalChatFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Log.i("Michael", "chat onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebase();
        initPresenter();
        dataBaseApi = new DataBaseImpl(context);
        Log.i("Michael", "chat onCreate");
    }

    private void initPresenter() {
        presenter = new PersonalFragmentPresenterImpl(this);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        manager = new FireStoreManager();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Michael", "chat onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_chat, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.personal_fragment_progressbar);
        recyclerView = view.findViewById(R.id.personal_fragment_recycler_view);
        btnLogin = view.findViewById(R.id.personal_fragment_btn_login);
        tvNotice = view.findViewById(R.id.personal_fragment_text_notice);
        ivLogo = view.findViewById(R.id.personal_fragment_iv_icon);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLoginClickListener();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Michael", "chat onActivityCreated");
    }

    private void searchData() {
        user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            if (isFirstSearchData) {
                presenter.onShowProgress(true);
            }
            roomArray = new ArrayList<>();
            friendDataArray = new ArrayList<>();
            friendsArrayList = new ArrayList<>();
            firestore.collection("chat_room")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    String user1 = (String) snapshot.get("user1");
                                    String user2 = (String) snapshot.get("user2");
                                    if (user1 != null && user2 != null) {
                                        if (user1.equals(user.getEmail())) {
                                            roomArray.add(snapshot.getId());
                                            friendsArrayList.add(user2);
                                            Log.i("Michael", "房間 : " + snapshot.getId());
                                        }
                                        if (user2.equals(user.getEmail())) {
                                            Log.i("Michael", "房間 : " + snapshot.getId());
                                            roomArray.add(snapshot.getId());
                                            friendsArrayList.add(user1);
                                        }
                                    }
                                }
                                friendCount = 0;
                                catchFriendData();
                            }
                        }
                    });
        } else {
            presenter.onNoUserEvent();
        }
    }

    private void catchFriendData() {
        if (friendCount < friendsArrayList.size() && user.getEmail() != null) {
            firestore.collection(FRIENDSHIP)
                    .document(user.getEmail())
                    .collection(FRIEND)
                    .document(friendsArrayList.get(friendCount))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot snapshot = task.getResult();
                                FriendData data = new FriendData();
                                data.setdisplayName((String) snapshot.get("displayName"));
                                data.setEmail(friendsArrayList.get(friendCount));
                                data.setPhotoUrl((String) snapshot.get("photoUrl"));
                                friendDataArray.add(data);
                                friendCount++;
                                catchFriendData();
                            } else {
                                Log.i("Michael", "沒房間");
                            }
                        }

                    });
        } else {
            friendCount = 0;
            jsonArray = new ArrayList<>();
            catchJson();

        }
    }

    private void catchJson() {
        if (friendCount < roomArray.size()) {
            Log.i("Michael", "有房間");
            firestore.collection("chat_data")
                    .document(roomArray.get(friendCount))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot snapshot = task.getResult();
                                jsonArray.add((String) snapshot.get("json"));
                                Log.i("Michael", "json : " + snapshot.get("json"));
                                friendCount++;
                                catchJson();
                            }
                        }
                    });

        } else {
            friendCount = 0;
            presenter.onCatchallData(jsonArray, friendDataArray);
        }
    }

    @Override
    public void showLoginInformation(boolean isShow) {
        ivLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setText(getString(R.string.login_notice_chat));
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(context, LoginActivity.class);
        context.startActivity(it);
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isFirstSearchData) {
            progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void searchForChatData(final String currentEmail,
                                  final ArrayList<String> friendsArrayList) {
        this.friendsArrayList = friendsArrayList;
        this.currentEmail = currentEmail;
        chatDataArrayList = new ArrayList<>();
        searchAllData();
    }

    @Override
    public void setRecyclerView(ArrayList<PersonalChatDTO> chatDataArrayList) {
        adapter = new PersonalFragmentAdapter(context);
        adapter.setData(chatDataArrayList);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnChatItemClickListener(new PersonalFragmentAdapter.OnChatItemClickListener() {
            @Override
            public void onClick(String displayName, String friendEmail, String photoUrl) {
                presenter.onItemClickListener(displayName, friendEmail, photoUrl);
            }
        });
        adapter.setOnChatItemLongClickListener(new PersonalFragmentAdapter.OnChatItemLongClickListener() {
            @Override
            public void onClick(String documentPath, int itemPosition1) {
                itemPosition = itemPosition1;
                presenter.onShowDeleteMessageConfirmDialog(documentPath);
            }
        });
    }

    @Override
    public void intentToPersonalChatActivity(String displayName, String friendEmail, String
            photoUrl) {
        Intent it = new Intent(context, PersonalChatActivity.class);
        it.putExtra("displayName", displayName);
        it.putExtra("mail", friendEmail);
        it.putExtra("photoUrl", photoUrl);
        context.startActivity(it);
    }

    @Override
    public void showNoChatDataView(boolean isShow) {
        ivLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setText(isShow ? getString(R.string.no_chat_data) : "");
    }

    @Override
    public void showDeleteConfirmDialog(String documentPath) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.confirm_to_delete_message))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chatDataArrayList.remove(itemPosition);
                        adapter.notifyDataSetChanged();
                        deleteMessage(documentPath);
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public Context getVuContext() {
        return context;
    }

    @Override
    public void continueSearchData() {
        if (user != null) {
            isConnecting = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    do {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isConnecting) {
                            firestore.collection(IS_UPDATE)
                                    .document(UPDATE)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                boolean isUpdate = (boolean) task.getResult().get("is_update");
                                                if (isUpdate) {
                                                    Log.i("Michael", "更新資料");
                                                    setNotUpdate();
                                                    searchData();

                                                } else {
                                                    Log.i("Michael", "不更新資料");
                                                }
                                            }
                                        }
                                    });
                        } else {
                            break;
                        }
                    } while (true);

                }
            }).start();
        }
    }

    private void setNotUpdate() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Map<String, Object> map = new HashMap<>();
                    map.put("is_update", false);
                    firestore.collection(IS_UPDATE)
                            .document(UPDATE)
                            .set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("Michael", "已更改不更新");
                                    }
                                }
                            });
                }
            });
        }
    }

    @Override
    public void updateView(ArrayList<PersonalChatDTO> allChatData) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setData(allChatData);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void deleteMessage(String documentPath) {
        ArrayList<String> documentArray = new ArrayList<>();
        firestore.collection(PERSONAL_CHAT)
                .document(documentPath)
                .collection(CHAT_DATA)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                documentArray.add(snapshot.getId());
                            }
                            if (documentArray.size() != 0) {
                                for (String id : documentArray) {
                                    firestore.collection(PERSONAL_CHAT)
                                            .document(documentPath)
                                            .collection(CHAT_DATA)
                                            .document(id)
                                            .delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.i("Michael", "刪除成功");
                                                    }
                                                }
                                            });
                                }
                                firestore.collection(PERSONAL_CHAT)
                                        .document(documentPath)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i("Michael", "路徑刪除成功");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }

    private void searchAllData() {
        if (searchCount < friendsArrayList.size()) {
            Log.i("Michael", "朋友有幾個 : " + friendsArrayList.size());
            searchFriendsData(friendsArrayList.get(searchCount), currentEmail);
        } else {
            searchCount = 0;
            if (chatDataArrayList.size() != 0) {
                Log.i("Michael", "第一筆聊天訊息 : " + chatDataArrayList.get(0).getMessage());
                if (chatDataArrayList.get(0).getMessage().isEmpty() || chatDataArrayList.get(0).getMessage() == null) {
                    Log.i("Michael", "沒有聊天訊息");
                    presenter.onShowProgress(false);
                    presenter.onNoMessageEvent();
                    return;
                }
            }
            Log.i("Michael", "所有資料讀取完畢");
            presenter.onCatchAllDataSucessful(chatDataArrayList);
        }
    }


    private void searchFriendsData(final String friendEmail, final String currentEmail) {

        manager.setFirstCollection("users");
        manager.setFirstDocument(friendEmail);
        manager.catchOneDocumentData(new FireStoreManager.OnFirebaseDocumentListener() {
            @Override
            public void onSuccess(Task<DocumentSnapshot> task) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot snapshot = task.getResult();
                                PersonalChatDTO data = new PersonalChatDTO();
                                data.setDisplayName((String) snapshot.get("displayName"));
                                data.setFriendEmail((String) snapshot.get("email"));
                                data.setPhotoUrl((String) snapshot.get("photoUrl"));
                                searchChatData(data, friendEmail, currentEmail);
                            }
                        }
                    });
                }
            }
        });
    }

    private void searchChatData(final PersonalChatDTO data, final String friendEmail,
                                final String currentEmail) {
        String path = currentEmail + "And" + friendEmail;
        firestore.collection(PERSONAL_CHAT).document(path)
                .collection(CHAT_DATA)
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<PersonalChatData> chatArrayList = new ArrayList<>();
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PersonalChatData chatData = new PersonalChatData();
                                chatData.setEmail((String) document.get("email"));
                                chatData.setMessage((String) document.get("message"));
                                Log.i("Michael", "Message : " + (String) document.get("message"));
                                chatData.setTime((long) document.get("time"));
                                chatArrayList.add(chatData);
                            }
                            if (chatArrayList.size() != 0) {

                                int size = chatArrayList.size() - 1;
                                Log.i("Michael", "有聊天訊息 : " + chatArrayList.get(size).getMessage());
                                data.setMessage(chatArrayList.get(size).getMessage());
                                data.setTime(chatArrayList.get(size).getTime());
                                data.setDocumentPath(path);
                                chatDataArrayList.add(data);
                                Log.i("Michael", "chatDataArrayList 第一筆 : " + chatDataArrayList.get(0).getMessage() + " , 長度 : " + chatDataArrayList.size());
                                searchCount++;
                                searchAllData();
                            } else {
                                Log.i("Michael", "有做到ReSearch");
                                String path = friendEmail + "And" + currentEmail;
                                reSearchChatData(path, data);
                            }

                        }
                    }
                });
    }

    private void reSearchChatData(String path, final PersonalChatDTO data) {
        firestore.collection(PERSONAL_CHAT).document(path)
                .collection(CHAT_DATA)
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<PersonalChatData> chatArrayList = new ArrayList<>();
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PersonalChatData chatData = new PersonalChatData();
                                chatData.setEmail((String) document.get("email"));
                                chatData.setMessage((String) document.get("message"));
                                chatData.setTime((long) document.get("time"));
                                chatArrayList.add(chatData);
                            }
                            if (chatArrayList.size() != 0) {
                                int size = chatArrayList.size() - 1;
                                data.setMessage(chatArrayList.get(size).getMessage());
                                data.setTime(chatArrayList.get(size).getTime());
                                data.setDocumentPath(path);
                                chatDataArrayList.add(data);
                                searchCount++;
                                searchAllData();
                            } else {
                                searchCount++;
                                searchAllData();
                            }

                        }
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("Michael", "chat OnResume");
        user = mAuth.getCurrentUser();
        if (user != null) {
            Log.i("Michael", "有用戶");
            if (dataBaseApi.getAllChatData() != null && dataBaseApi.getAllChatData().size() != 0) {
                Log.i("Michael", "Chat DB 有資料");
                isFirstSearchData = false;
                presenter.onCatchChatDataSuccessful(dataBaseApi.getAllChatData());
            } else {
                isFirstSearchData = true;
                searchData();
            }
        } else {
            chatDataArrayList = new ArrayList<>();
            if (adapter != null) {
                adapter = new PersonalFragmentAdapter(context);
                adapter.setData(chatDataArrayList);
                recyclerView.setAdapter(adapter);
            }
            presenter.onNoUserEvent();
            Log.i("Michael", "沒用戶");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("Michael", "chat onPause");
        isConnecting = false;
        if (adapter != null) {
            adapter = new PersonalFragmentAdapter(context);
            adapter.setData(new ArrayList<PersonalChatDTO>());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Michael", "chat onStop");
    }


}
