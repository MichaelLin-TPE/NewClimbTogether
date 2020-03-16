package com.hiking.climbtogether.friend_manager_activity.view;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.friend_manager_activity.FriendDTO;

import java.util.ArrayList;

public class FriendViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    private FriendViewAdapter adapter;

    private RecyclerView recyclerView;

    private FriendViewAdapter.OnfriendItemClickListener listener;

    public void setOnfriendItemClickListener(FriendViewAdapter.OnfriendItemClickListener listener){
        this.listener = listener;
    }

    public FriendViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        recyclerView = itemView.findViewById(R.id.friend_recycler_view);
    }

    public void setData(ArrayList<FriendDTO> inviteArrayList) {
        adapter = new FriendViewAdapter(context,inviteArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.setOnfriendItemClickListener(new FriendViewAdapter.OnfriendItemClickListener() {
            @Override
            public void onClick(FriendDTO data, int itemPosition) {
                listener.onClick(data,itemPosition);
            }
        });
    }
}
