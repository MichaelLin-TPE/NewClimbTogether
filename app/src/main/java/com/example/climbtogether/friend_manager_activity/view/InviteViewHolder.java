package com.example.climbtogether.friend_manager_activity.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.friend_manager_activity.FriendInviteDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InviteViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;

    private Context context;

    private FirebaseFirestore firestore;

    private static final String FRIENDSHIP = "friendship";

    private static final String FRIEND = "friend";

    private static final String INVITE_FRIEND = "invite_friend";

    private static final String INVITE = "invite";

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private ArrayList<FriendInviteDTO> inviteArrayList;

    private InviteViewAdapter adapter;

    public InviteViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.invite_recycler_view);
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void setData(ArrayList<FriendInviteDTO> inviteArrayList) {
        this.inviteArrayList = inviteArrayList;
        adapter = new InviteViewAdapter(context, inviteArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(adapter);
        adapter.setOnCheckInviteIconClickListener(new InviteViewAdapter.OnCheckInviteIconClickListener() {
            @Override
            public void onClickYes(boolean isAdd, FriendInviteDTO data, int itemPosition) {
                deleteInviteData(data, itemPosition);
                createFriendData(data);
            }

            @Override
            public void onClickNo(boolean isAdd, FriendInviteDTO data, int itemPosition) {
                deleteInviteData(data, itemPosition);
            }
        });
    }

    private void createFriendData(final FriendInviteDTO data) {
        firestore.collection("users").document(data.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> map = document.getData();
                                if (user != null) {
                                    if (user.getEmail() != null) {
                                        if (map != null) {
                                            becomeToFriend(user.getEmail(),data.getEmail(),map);
                                        }
                                    }
                                }

                            }
                        }
                    }
                });
    }

    private void becomeToFriend(final String userEmail, final String friendEmail, Map<String,Object> map) {
        firestore.collection(FRIENDSHIP).document(userEmail)
                .collection(FRIEND).document(friendEmail)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createUserData(userEmail,friendEmail);
                            Log.i("Michael", "新增成功");
                        }
                    }
                });
    }

    private void createUserData(final String userEmail, final String friendEmail) {
        firestore.collection("users").document(userEmail).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> map = document.getData();
                                if (user != null) {
                                    if (user.getEmail() != null) {
                                        if (map != null) {
                                            becomeToFriend(friendEmail,userEmail,map);
                                        }
                                    }
                                }

                            }
                        }
                    }
                });
    }

    private void deleteInviteData(FriendInviteDTO data, final int itemPosition) {
        if (user != null) {
            if (user.getEmail() != null) {
                firestore.collection(INVITE_FRIEND).document(user.getEmail()).collection(INVITE).document(data.getEmail())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("Michael", "已刪除");
                                    inviteArrayList.remove(itemPosition);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        } else {
            Log.i("Michael", "user null");
        }
    }

}
