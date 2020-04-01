package com.hiking.climbtogether.vote_list_activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.vote_list_activity.list_view_presenter.ListViewPresenter;
import com.hiking.climbtogether.vote_list_activity.list_view_presenter.ListViewPresenterImpl;

public class VoteListAdapter extends RecyclerView.Adapter {

    private Context context;

    private ListViewPresenter presenter;

    private VotingAdapter.OnVotingItemClickListener listener;

    private VoteAlreadyAdapter.OnVoteAlreadyItemClickListener alreadyListener;

    public void setOnVoteAlreadyItemClickListener(VoteAlreadyAdapter.OnVoteAlreadyItemClickListener alreadyListener){
        this.alreadyListener = alreadyListener;
    }


    public void setOnVotingItemClickListener(VotingAdapter.OnVotingItemClickListener listener) {
        this.listener = listener;
    }

    public VoteListAdapter(Context context, ListViewPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case ListViewPresenterImpl.VOTE:
                Log.i("Michael", "跑投票中");
                return new VotingViewHolder(LayoutInflater.from(context).inflate(R.layout.voting_view, parent, false), context);
            case ListViewPresenterImpl.VOTE_ALREADY:

                return new VoteAlreadyViewHolder(LayoutInflater.from(context).inflate(R.layout.vote_already_view,parent,false),context);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VotingViewHolder) {
            presenter.onBindVotingViewHolder((VotingViewHolder) holder, position);
            presenter.setOnVotingItemClickListener((VotingViewHolder) holder, listener);
        }

        if (holder instanceof VoteAlreadyViewHolder){
            presenter.onBindVoteAlreadyViewHolder((VoteAlreadyViewHolder) holder, position);
            presenter.setOnVoteAlreadyItemClickListener((VoteAlreadyViewHolder)holder,alreadyListener);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }
}
