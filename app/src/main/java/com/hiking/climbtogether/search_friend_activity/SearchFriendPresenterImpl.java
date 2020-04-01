package com.hiking.climbtogether.search_friend_activity;

import android.util.Log;

public class SearchFriendPresenterImpl implements SearchFriendPresenter {

    private SearchFriendVu mView;

    public SearchFriendPresenterImpl(SearchFriendVu mView) {
        this.mView = mView;
    }

    @Override
    public void onSearchEvent(String content) {
        mView.searchUserData(content);
        Log.i("Michael","搜尋 : "+content);
    }

    @Override
    public void onIsNotFriendEvent(String photoUrl, String name, String email) {
        mView.showUserInformation(photoUrl,name,email);
    }

    @Override
    public void onIsFriendEvent(String photoUrl, String name, String email) {
        mView.showIsFriendUserInformation(photoUrl,name,email);
    }

    @Override
    public void onSendButtonClickListener() {
        mView.sendInvitationToFriend();
    }

    @Override
    public void onSearchNoData() {
        mView.showSearChNoDataView();
    }
}
