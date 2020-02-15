package com.example.climbtogether.chat_activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatRightViewHolder extends RecyclerView.ViewHolder {

    private TextView tvMessage;

    private TextView tvTime;

    public ChatRightViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessage = itemView.findViewById(R.id.chat_item_message);
        tvTime = itemView.findViewById(R.id.chat_item_time);
    }

    public void setData(ChatData chatData) {
        tvMessage.setText(chatData.getMessage());
        String hour = new SimpleDateFormat("HH",Locale.TAIWAN).format(new Date(chatData.getTime()));
        int houtInt = Integer.parseInt(hour);
        if (houtInt < 12){
            hour = "AM";
        }else {
            hour = "PM";
        }
        tvTime.setText(String.format(Locale.getDefault(),"%s %s",new SimpleDateFormat("HH:mm", Locale.TAIWAN).format(new Date(chatData.getTime())),hour));

    }
}
