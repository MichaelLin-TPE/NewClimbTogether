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

    private InviteViewAdapter.OnCheckInviteIconClickListener listener;

    public void setOnCheckInviteListener(InviteViewAdapter.OnCheckInviteIconClickListener listener){
        this.listener = listener;
    }

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
                listener.onClickYes(isAdd,data,itemPosition);

            }

            @Override
            public void onClickNo(boolean isAdd, FriendInviteDTO data, int itemPosition) {
                listener.onClickNo(isAdd,data,itemPosition);

            }
        });
    }



}
