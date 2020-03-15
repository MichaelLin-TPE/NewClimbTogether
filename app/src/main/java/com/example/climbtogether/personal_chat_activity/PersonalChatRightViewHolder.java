package com.example.climbtogether.personal_chat_activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PersonalChatRightViewHolder extends RecyclerView.ViewHolder {

    private TextView tvMessage;

    private TextView tvTime;

    public PersonalChatRightViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessage = itemView.findViewById(R.id.chat_item_message);
        tvTime = itemView.findViewById(R.id.chat_item_time);
    }

    public void setData(PersonalChatData personalChatData) {

        tvMessage.setText(personalChatData.getMessage());

        String hour = new SimpleDateFormat("HH", Locale.TAIWAN).format(new Date(personalChatData.getTime()));
        int hours = Integer.parseInt(hour);
        if (hours < 12) {
            hour = "上午";
        } else {
            hour = "下午";
        }
        tvTime.setText(String.format(Locale.getDefault(), "%s%s", new SimpleDateFormat("HH:mm", Locale.TAIWAN).format(new Date(personalChatData.getTime())), hour));
    }
}
