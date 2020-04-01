package com.hiking.climbtogether.vote_list_activity.list_view_presenter;

import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;
import com.hiking.climbtogether.vote_list_activity.VoteAlreadyAdapter;
import com.hiking.climbtogether.vote_list_activity.VoteAlreadyViewHolder;
import com.hiking.climbtogether.vote_list_activity.VotingAdapter;
import com.hiking.climbtogether.vote_list_activity.VotingViewHolder;

import java.util.ArrayList;

public interface ListViewPresenter {
    void setData(ArrayList<VoteData> dataArrayList);

    int getItemViewType(int position);

    int getItemCount();

    void onBindVotingViewHolder(VotingViewHolder holder, int position);

    void setOnVotingItemClickListener(VotingViewHolder holder, VotingAdapter.OnVotingItemClickListener listener);

    void setVoteDayLineData(ArrayList<VoteData> voteDayLineArray);

    void onBindVoteAlreadyViewHolder(VoteAlreadyViewHolder holder, int position);

    void setOnVoteAlreadyItemClickListener(VoteAlreadyViewHolder holder, VoteAlreadyAdapter.OnVoteAlreadyItemClickListener alreadyListener);
}
