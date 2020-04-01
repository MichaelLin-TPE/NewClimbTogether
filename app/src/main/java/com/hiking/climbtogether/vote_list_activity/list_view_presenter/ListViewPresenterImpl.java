package com.hiking.climbtogether.vote_list_activity.list_view_presenter;

import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;
import com.hiking.climbtogether.vote_list_activity.VoteAlreadyAdapter;
import com.hiking.climbtogether.vote_list_activity.VoteAlreadyViewHolder;
import com.hiking.climbtogether.vote_list_activity.VotingAdapter;
import com.hiking.climbtogether.vote_list_activity.VotingViewHolder;

import java.util.ArrayList;

public class ListViewPresenterImpl implements ListViewPresenter {

    public static final int VOTE = 0;

    public static final int VOTE_ALREADY = 1;

    private boolean isShowVote,isShowVoteAlready;

    private ArrayList<VoteData> voteDayLineArray;

    private ArrayList<VoteData> dataArrayList;

    @Override
    public void setData(ArrayList<VoteData> dataArrayList) {
        this.dataArrayList = dataArrayList;

        if (dataArrayList.size() != 0){
            isShowVote = true;
        }else {
            isShowVote = false;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isShowVote && position == 0){
            return VOTE;
        }else if (isShowVoteAlready && position == 0){
            return VOTE_ALREADY;
        }

        if (isShowVoteAlready && position == 1){
            return VOTE_ALREADY;
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (isShowVoteAlready){
            count ++;
        }
        if (isShowVote){
            count ++;
        }

        return count;
    }

    @Override
    public void onBindVotingViewHolder(VotingViewHolder holder, int position) {
        holder.setData(dataArrayList);
    }

    @Override
    public void setOnVotingItemClickListener(VotingViewHolder holder, VotingAdapter.OnVotingItemClickListener listener) {
        holder.setOnVotingItemClickListener(listener);
    }

    @Override
    public void setVoteDayLineData(ArrayList<VoteData> voteDayLineArray) {
        this.voteDayLineArray = voteDayLineArray;
        if (voteDayLineArray.size() != 0){
            isShowVoteAlready = true;
        }else {
            isShowVoteAlready = false;
        }
    }

    @Override
    public void onBindVoteAlreadyViewHolder(VoteAlreadyViewHolder holder, int position) {
        holder.setData(voteDayLineArray);
    }

    @Override
    public void setOnVoteAlreadyItemClickListener(VoteAlreadyViewHolder holder, VoteAlreadyAdapter.OnVoteAlreadyItemClickListener alreadyListener) {
        holder.setOnVoteAlreadyItemClickListener(alreadyListener);
    }
}
