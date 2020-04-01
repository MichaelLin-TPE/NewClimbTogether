package com.hiking.climbtogether.vote_list_activity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;

import java.util.ArrayList;

public class VoteAlreadyViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    private RecyclerView recyclerView;

    private VoteAlreadyAdapter.OnVoteAlreadyItemClickListener listener;

    public void setOnVoteAlreadyItemClickListener(VoteAlreadyAdapter.OnVoteAlreadyItemClickListener listener){
        this.listener = listener;
    }

    public VoteAlreadyViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        recyclerView = itemView.findViewById(R.id.vote_already_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setData(ArrayList<VoteData> voteDayLineArray) {
        VoteAlreadyAdapter alreadyAdapter = new VoteAlreadyAdapter(context,voteDayLineArray);
        recyclerView.setAdapter(alreadyAdapter);

        alreadyAdapter.setOnVoteAlreadyItemClickListener(new VoteAlreadyAdapter.OnVoteAlreadyItemClickListener() {
            @Override
            public void onClick(VoteData data) {
                listener.onClick(data);
            }
        });
    }
}
