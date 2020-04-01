package com.hiking.climbtogether.vote_list_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;

import java.util.ArrayList;
import java.util.Locale;

public class VoteResultAdapter extends RecyclerView.Adapter<VoteResultAdapter.ViewHolder> {

    private Context context;

    private ArrayList<VoteResultData> dataArrayList;


    public VoteResultAdapter(Context context, ArrayList<VoteResultData> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.vote_result_item,parent,false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int maxProgress = 0;

        for (VoteResultData data : dataArrayList){
            maxProgress += data.getNumber();
        }
        holder.seekBar.setMax(maxProgress);
        holder.seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        VoteResultData data = dataArrayList.get(position);
        holder.seekBar.setProgress(data.getNumber());
        holder.tvContent.setText(data.getContent());
        holder.tvNumber.setText(String.format(Locale.getDefault(),"%d",data.getNumber()));
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContent,tvNumber;

        private SeekBar seekBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.vote_result_item_content);
            tvNumber = itemView.findViewById(R.id.vote_result_item_number);
            seekBar = itemView.findViewById(R.id.vote_result_item_seekbar);
        }
    }
}
