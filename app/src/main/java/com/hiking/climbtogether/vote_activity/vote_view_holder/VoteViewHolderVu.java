package com.hiking.climbtogether.vote_activity.vote_view_holder;

public interface VoteViewHolderVu {
    void showNumberSelectDialog();

    void setTvSpinnerText(String numberType);

    void addEditTextView(String s);

    void showDatePicker();

    void showErrorCode(String message);

    void saveVoteDataToFirebase(String jsonStr,String title);

    String getUserEmail();
}
