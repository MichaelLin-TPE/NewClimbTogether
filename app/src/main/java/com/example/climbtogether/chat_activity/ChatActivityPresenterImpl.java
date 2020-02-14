package com.example.climbtogether.chat_activity;

import java.util.ArrayList;

public class ChatActivityPresenterImpl implements ChatActivityPresenter {

    private ChatActivityVu mView;

    public ChatActivityPresenterImpl(ChatActivityVu mView){
        this.mView = mView;
    }

    @Override
    public void onBtnSendClickListener(String message, long currentTime) {
        if (message == null || message.isEmpty()){
            String errorMessage = "請輸入對話";
            mView.showErrorMessage(errorMessage);
            return;
        }
        mView.createChatDataToFirestore(message,currentTime);
    }

    @Override
    public void onSearchChatData(String email) {
        mView.searchChatDataFromFirestore(email);
    }

    @Override
    public void onCatchChatDataSuccessful(ArrayList<ChatData> chatDataArrayList) {
        mView.setRecyclerView(chatDataArrayList);
    }
}
