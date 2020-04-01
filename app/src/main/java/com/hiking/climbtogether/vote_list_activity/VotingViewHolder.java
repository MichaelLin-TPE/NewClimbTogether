package com.hiking.climbtogether.vote_list_activity;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;

import java.util.ArrayList;

public class VotingViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;

    private Context context;

    private VotingAdapter.OnVotingItemClickListener listener;

    public void setOnVotingItemClickListener(VotingAdapter.OnVotingItemClickListener listener){
        this.listener = listener;
    }

    public VotingViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        recyclerView = itemView.findViewById(R.id.voting_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
    }

    public void setData(ArrayList<VoteData> dataArrayList) {
        VotingAdapter adapter = new VotingAdapter(context,dataArrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnVotingItemClickListener(new VotingAdapter.OnVotingItemClickListener() {
            @Override
            public void onClick(VoteData data,int itemPosition) {
                listener.onClick(data,itemPosition);
            }
        });
    }
}
