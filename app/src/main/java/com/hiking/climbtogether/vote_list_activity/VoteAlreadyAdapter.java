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

public class VoteAlreadyAdapter extends RecyclerView.Adapter<VoteAlreadyAdapter.ViewHolder> {

    private Context context;

    private ArrayList<VoteData> dataArrayList;

    private OnVoteAlreadyItemClickListener listener;

    public void setOnVoteAlreadyItemClickListener(OnVoteAlreadyItemClickListener listener){
        this.listener = listener;
    }

    public VoteAlreadyAdapter(Context context, ArrayList<VoteData> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.vote_already_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoteData data = dataArrayList.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvContent.setText(String.format(Locale.getDefault(),"發起人 : %s\n截止日期 : %s",data.getCreator(),data.getTime()));

        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout clickArea;

        private TextView tvTitle,tvContent;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clickArea = itemView.findViewById(R.id.vote_already_item_click_area);
            tvTitle = itemView.findViewById(R.id.vote_already_item_title);
            tvContent = itemView.findViewById(R.id.vote_already_item_creator);
        }
    }

    public interface OnVoteAlreadyItemClickListener{
        void onClick(VoteData data);
    }
}
