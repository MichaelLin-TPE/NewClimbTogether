package com.example.climbtogether.friend_manager_activity;

import java.util.ArrayList;

public interface FriendManagerPresenter {
    void onBackIconClickLiistener();

    void onCatchDataSuccessful(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList);

    void onFriendItemClickListener(FriendDTO data, int itemPosition);

    void onChatButtonClickListener(String email, String displayName, String friendPhotoUrl);

    void onDeleteButtonClickListener(String email,int itemPosition);

    void onConfirmToDelectFriendClick(String friendEmail,int itemPosition);
}
