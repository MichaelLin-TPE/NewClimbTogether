package com.example.climbtogether.chat_activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;

public class ChatRightViewHolder extends RecyclerView.ViewHolder {

    private TextView tvMessage;

    private TextView tvTime;

    public ChatRightViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessage = itemView.findViewById(R.id.chat_left_item_message);
        tvTime = itemView.findViewById(R.id.chat_left_item_time);
    }
}
