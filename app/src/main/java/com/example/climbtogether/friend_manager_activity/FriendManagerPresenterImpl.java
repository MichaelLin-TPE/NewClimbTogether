package com.example.climbtogether.friend_manager_activity;

import java.util.ArrayList;

public class FriendManagerPresenterImpl implements FriendManagerPresenter {

    private FriendManagerVu mView;

    public FriendManagerPresenterImpl(FriendManagerVu mView){
        this.mView = mView;
    }

    @Override
    public void onBackIconClickLiistener() {
        mView.closePage();
    }

    @Override
    public void onCatchDataSuccessful(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList) {
        mView.setRecyclerView(inviteArrayList,friendArrayList);
    }

    @Override
    public void onFriendItemClickListener(FriendDTO data, int itemPosition) {
        mView.showUserDialog(data,itemPosition);
    }

    @Override
    public void onChatButtonClickListener(String email, String displayName, String friendPhotoUrl) {
        mView.intentToPersonalChatActivity(email,displayName,friendPhotoUrl);
    }

    @Override
    public void onDeleteButtonClickListener(String friendEmail,int itemPosition) {
        mView.showConfirmDialog(friendEmail,itemPosition);
    }

    @Override
    public void onConfirmToDelectFriendClick(String friendEmail,int itemPosition) {
        mView.changeRecyclerView(itemPosition);
        mView.deleteFriendData(friendEmail);
    }
}
