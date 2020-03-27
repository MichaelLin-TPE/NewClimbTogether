package com.hiking.climbtogether.personal_chat_activity;

import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;

import java.util.ArrayList;

public interface PersonalChatVu {
    void closePage();

    void showToast(String message);
    void setRecyclerView(ArrayList<PersonalChatData> chatArrayList);

    void changeRecyclerView(ArrayList<PersonalChatData> chatArrayList);

    void searchFriendData(String friendEmail,String message,String displayName);

    String getDisplayName();

    void setDataToFireStore(String message, long time);

    String getEmail();

    void setChatDataToFireStore(String jsonStr);

    void showPhotoPage();

    void uploadPhoto(ArrayList<byte[]> photoBytesArray);

    void showErrorCode(String message);

    void intentToPhotoActivity(String downLoadUrl);

    void showCamera();

    void showBottomShareView(ArrayList<String> downloadUrl);

    void closeBottomView(boolean isShow);

    void intentToPersonalChatActivity(String email, String name, String photo, String path);

    void addPhoto(String email, String name, String photo, ArrayList<String> downloadUrl, String path);

    String getPhotoUrl();

    void updateFriendChatData(String jsonStr, String path);

    void showToolsListView();

    String getSearchStr();

    void closeToolsList(boolean isShow);

    void showSearchDataView(boolean isShow);

    void closeAllToolsView();

    void showSearchNoChatDataDialog();

    void showSearchResult(ArrayList<Integer> searchContentIndexArray);

    void scrollToPosition(Integer contentIndex);

    void hideKeyBoard();

    String getPictureStr();

    void intentToPersonalChatImageActivity(ArrayList<String> photoUrlArray);
}
