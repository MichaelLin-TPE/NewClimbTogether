package com.hiking.climbtogether.search_friend_activity;

public interface SearchFriendPresenter {
    void onSearchEvent(String toString);

    void onIsNotFriendEvent(String photoUrl, String name, String email);

    void onIsFriendEvent(String photoUrl, String name, String email);

    void onSendButtonClickListener();

    void onSearchNoData();
}
