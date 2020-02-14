package com.example.climbtogether.chat_activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;

public class ChatLeftViewHolder extends RecyclerView.ViewHolder {
    private TextView tvMessage;

    private TextView tvTime;

    public ChatLeftViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessage = itemView.findViewById(R.id.chat_item_message);
        tvTime = itemView.findViewById(R.id.chat_item_time);
    }
}
