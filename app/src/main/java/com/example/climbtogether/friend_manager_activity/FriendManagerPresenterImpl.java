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
}
