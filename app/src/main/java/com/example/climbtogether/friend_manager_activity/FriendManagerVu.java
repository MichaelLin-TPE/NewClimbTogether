package com.example.climbtogether.friend_manager_activity;

import java.util.ArrayList;

public interface FriendManagerVu {
    void closePage();

    void setRecyclerView(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList);
}
