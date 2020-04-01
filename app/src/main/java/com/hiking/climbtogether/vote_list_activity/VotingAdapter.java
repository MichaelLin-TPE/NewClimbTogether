package com.hiking.climbtogether.vote_list_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;

import java.util.ArrayList;
import java.util.Locale;

public class VotingAdapter extends RecyclerView.Adapter<VotingAdapter.ViewHolder> {

    private Context context;

    private ArrayList<VoteData> dataArrayList;

    private OnVotingItemClickListener listener;

    public void setOnVotingItemClickListener(OnVotingItemClickListener listener){
        this.listener = listener;
    }

    public VotingAdapter(Context context, ArrayList<VoteData> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.voting_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoteData data = dataArrayList.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvCreator.setText(String.format(Locale.getDefault(),"發起人 : %s \n投票截止日 : %s",data.getCreator(),data.getTime()));

        holder.clickArae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(data,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle,tvCreator;

        private ConstraintLayout clickArae;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.voting_item_title);
            tvCreator = itemView.findViewById(R.id.voting_item_creator);
            clickArae = itemView.findViewById(R.id.voting_item_click_area);
        }
    }

    public interface OnVotingItemClickListener{
        void onClick(VoteData data,int itemPosition);
    }
}
