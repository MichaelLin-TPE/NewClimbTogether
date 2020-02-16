package com.example.climbtogether.friend_manager_activity.view;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.friend_manager_activity.FriendDTO;
import com.example.climbtogether.friend_manager_activity.FriendInviteDTO;
import com.example.climbtogether.friend_manager_activity.FriendManagerAdapter;

import java.util.ArrayList;

public class FriendViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    private FriendViewAdapter adapter;

    private RecyclerView recyclerView;


    public FriendViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        recyclerView = itemView.findViewById(R.id.friend_recycler_view);
    }

    public void setData(ArrayList<FriendDTO> inviteArrayList) {
        adapter = new FriendViewAdapter(context,inviteArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

    }
}
