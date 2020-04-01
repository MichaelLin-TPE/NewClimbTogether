package com.hiking.climbtogether.vote_activity.vote_presenter;

import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteViewHolder;

public interface VotePresenter {
    int getItemCount();

    int getItemViewType(int position);

    void onBindViewHolder(VoteViewHolder holder, int position);
}
