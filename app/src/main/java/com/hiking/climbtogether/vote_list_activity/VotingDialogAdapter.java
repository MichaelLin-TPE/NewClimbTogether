package com.hiking.climbtogether.vote_list_activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class VotingDialogAdapter extends RecyclerView.Adapter<VotingDialogAdapter.ViewHolder> {

    private ArrayList<String> dataArray;

    private Context context;

    private int userClickPosition;

    private OnVotingDialogItemClickListener listener;

    public void setOnVotingDialogItemClickListener(OnVotingDialogItemClickListener listener){
        this.listener = listener;
    }

    public VotingDialogAdapter(ArrayList<String> dataArray, Context context) {
        this.dataArray = dataArray;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.voting_dialog_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvContent.setText(dataArray.get(position));


        if (userClickPosition == position){
            holder.radioButton.setChecked(true);
        }else {
            holder.radioButton.setChecked(false);
        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClickPosition = position;
                listener.onClick(dataArray.get(position));
            }
        });

        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClickPosition = position;
                listener.onClick(dataArray.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArray == null ? 0 : dataArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RadioButton radioButton;

        private TextView tvContent;

        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.voting_dialog_item_rad);
            tvContent = itemView.findViewById(R.id.voting_dialog_item_content);
            clickArea = itemView.findViewById(R.id.voting_dialog_item_click_area);
        }
    }

    public interface OnVotingDialogItemClickListener{
        void onClick(String content);
    }
}
