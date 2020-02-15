package com.example.climbtogether.chat_activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatLeftViewHolder extends RecyclerView.ViewHolder {
    private TextView tvMessage;

    private TextView tvTime;

    private TextView tvEmail;

    public ChatLeftViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessage = itemView.findViewById(R.id.chat_left_item_message);
        tvTime = itemView.findViewById(R.id.chat_left_item_time);
        tvEmail = itemView.findViewById(R.id.chat_left_item_email);
    }

    public void setData(ChatData chatData) {
        tvMessage.setText(chatData.getMessage());
        tvEmail.setText(String.format(Locale.getDefault(),"%s 說 : ",chatData.getEmail()));
        tvTime.setText(new SimpleDateFormat("HH:mm", Locale.TAIWAN).format(new Date(chatData.getTime())));
    }
}
