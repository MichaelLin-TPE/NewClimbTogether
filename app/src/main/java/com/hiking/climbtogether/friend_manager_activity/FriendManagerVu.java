package com.hiking.climbtogether.friend_manager_activity;

import java.util.ArrayList;

public interface FriendManagerVu {
    void closePage();

    void setRecyclerView(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList);

    void showUserDialog(FriendDTO data, int itemPosition);

    void intentToPersonalChatActivity(String email, String displayName, String friendPhotoUrl);

    void showConfirmDialog(String friendEmail,int itemPosition);

    void deleteFriendData(String friendEmail);

    void changeRecyclerView(int itemPosition);

    void showNoFriendInfo(boolean isShow);
}
