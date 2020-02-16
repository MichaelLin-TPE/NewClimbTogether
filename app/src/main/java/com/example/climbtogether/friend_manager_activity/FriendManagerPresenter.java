package com.example.climbtogether.friend_manager_activity;

import java.util.ArrayList;

public interface FriendManagerPresenter {
    void onBackIconClickLiistener();

    void onCatchDataSuccessful(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList);
}
