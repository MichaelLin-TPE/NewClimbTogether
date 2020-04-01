package com.hiking.climbtogether.vote_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.vote_activity.vote_presenter.VotePresenter;
import com.hiking.climbtogether.vote_activity.vote_presenter.VotePresenterImpl;
import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteViewHolder;

public class VoteAdapter extends RecyclerView.Adapter {

    private VotePresenter presenter;

    private Context context;

    public VoteAdapter(VotePresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VotePresenterImpl.VIEW){
            return new VoteViewHolder(LayoutInflater.from(context).inflate(R.layout.create_vote_view,parent,false),context);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoteViewHolder){
            presenter.onBindViewHolder((VoteViewHolder)holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getItemViewType(position);
    }
}
