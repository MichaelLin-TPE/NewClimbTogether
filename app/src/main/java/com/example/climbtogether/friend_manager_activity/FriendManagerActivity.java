package com.example.climbtogether.friend_manager_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.climbtogether.R;
import com.example.climbtogether.friend_manager_activity.friend_presenter.FriendPresenter;
import com.example.climbtogether.friend_manager_activity.friend_presenter.FriendPresenterImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendManagerActivity extends AppCompatActivity implements FriendManagerVu {

    private FriendManagerPresenter presenter;

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private FriendPresenter friendPresenter;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private StorageReference storage;

    private FirebaseFirestore firestore;

    private static final String FRIENDSHIP = "friendship";

    private static final String FRIEND = "friend";

    private static final String INVITE_FRIEND = "invite_friend";

    private static final String INVITE = "invite";

    private ArrayList<FriendInviteDTO> inviteArrayList;

    private ArrayList<FriendDTO> friendArrayList;

    private FriendManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);
        initPresenter();
        initFirebase();
        initView();
        checkAllData();
    }

    /**
     * 先檢查邀清
     * 在檢查好友,如果有好友的話,列出列表並下載好友頭貼
     */

    private void checkAllData() {
        if (user != null){
            if (user.getEmail() != null){
                firestore.collection(INVITE_FRIEND).document(user.getEmail()).collection(INVITE)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    inviteArrayList = new ArrayList<>();
                                    if (task.getResult() != null){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            FriendInviteDTO friendInviteDTO = new FriendInviteDTO();
                                            friendInviteDTO.setAnswer((String)document.getData().get("answer"));
                                            friendInviteDTO.setEmail(document.getId());
                                            inviteArrayList.add(friendInviteDTO);
                                        }
                                        if (inviteArrayList.size() != 0){
                                            Log.i("Michael","有邀情");
                                        }else {
                                            Log.i("Michael","沒邀請");
                                        }

                                        searchForFriend(user.getEmail());
                                    }
                                }
                            }
                        });
            }
        }
    }

    private void searchForFriend(String email) {
        firestore.collection(FRIENDSHIP).document(email).collection(FRIEND)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() != null){
                                friendArrayList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    FriendDTO friendDTO = new FriendDTO();
                                    friendDTO.setDisplayName((String)document.getData().get("displayName"));
                                    friendDTO.setEmail((String)document.getData().get("email"));
                                    friendArrayList.add(friendDTO);
                                }
                                if (friendArrayList.size() != 0){
                                    Log.i("Michael","有朋友");
                                }else {
                                    Log.i("Michael","沒朋友");
                                }

                                presenter.onCatchDataSuccessful(inviteArrayList,friendArrayList);

                            }
                        }
                    }
                });
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
    }

    private void initView() {
        toolbar = findViewById(R.id.friend_manager_toolbar);
        recyclerView = findViewById(R.id.friend_manager_recycler_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackIconClickLiistener();
            }
        });
    }

    private void initPresenter() {
        presenter = new FriendManagerPresenterImpl(this);
        friendPresenter = new FriendPresenterImpl();
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void setRecyclerView(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList) {
        friendPresenter.setData(inviteArrayList,friendArrayList);

        adapter = new FriendManagerAdapter(friendPresenter,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);

    }
}
