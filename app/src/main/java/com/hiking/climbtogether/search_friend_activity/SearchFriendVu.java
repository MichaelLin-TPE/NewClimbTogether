package com.hiking.climbtogether.search_friend_activity;

public interface SearchFriendVu {
    void searchUserData(String content);

    void showUserInformation(String photoUrl, String name, String email);

    void showIsFriendUserInformation(String photoUrl, String name, String email);

    void showSearChNoDataView();

    void sendInvitationToFriend();
}
