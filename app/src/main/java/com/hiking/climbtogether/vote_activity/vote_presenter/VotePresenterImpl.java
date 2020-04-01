package com.hiking.climbtogether.vote_activity.vote_presenter;

import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteViewHolder;

public class VotePresenterImpl implements VotePresenter {

    public static final int VIEW = 0;


    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW;
    }

    @Override
    public void onBindViewHolder(VoteViewHolder holder, int position) {
        holder.showView();
    }
}
