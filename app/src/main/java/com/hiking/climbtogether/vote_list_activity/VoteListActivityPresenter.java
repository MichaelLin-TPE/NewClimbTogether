package com.hiking.climbtogether.vote_list_activity;

import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;

import java.util.ArrayList;

public interface VoteListActivityPresenter {
    void onBackButtonClickListener();

    void onCatchJsonStr(ArrayList<String> jsonArray, ArrayList<Boolean> isDayLineArray);

    void onVotingItemClickListener(VoteData data, int itemPosition);

    void onBtnDecideClickListener(String itemContent, VoteData data, int itemPosition);

    void onVoteAlreadyItemClickListener(VoteData data);
}
