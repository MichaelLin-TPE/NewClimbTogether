package com.hiking.climbtogether.vote_list_activity;

import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;

import java.util.ArrayList;

public interface VoteListActivityVu {
    void closePage();

    void setRecyclerView(ArrayList<VoteData> dataArrayList, ArrayList<Boolean> isVote, ArrayList<VoteData> voteDayLineArray);

    void showVotingDialog(VoteData data, int itemPosition);

    void showErrorCode(String message);

    void saveVoteDataToFirebase(String itemContent, VoteData data);

    String getUserEmail();

    void updateVoteDataToFirebase(String title, String jsonStr);

    void updateVoteDayLineData(ArrayList<VoteData> voteDayLineArray);

    void showResultDialog(VoteData data);

    void showNoDataView(boolean isShow);
}
