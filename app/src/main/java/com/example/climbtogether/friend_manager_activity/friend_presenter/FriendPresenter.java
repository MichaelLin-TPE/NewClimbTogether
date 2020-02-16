package com.example.climbtogether.friend_manager_activity.friend_presenter;

import com.example.climbtogether.friend_manager_activity.FriendDTO;
import com.example.climbtogether.friend_manager_activity.FriendInviteDTO;
import com.example.climbtogether.friend_manager_activity.view.FriendViewHolder;
import com.example.climbtogether.friend_manager_activity.view.InviteViewHolder;

import java.util.ArrayList;

public interface FriendPresenter {
    int getItemViewType(int position);

    int getItemCount();

    void setData(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList);

    void onBindInviteViewHolder(InviteViewHolder holder, int position);

    void onBindFriendViewHolder(FriendViewHolder holder, int position);
}
