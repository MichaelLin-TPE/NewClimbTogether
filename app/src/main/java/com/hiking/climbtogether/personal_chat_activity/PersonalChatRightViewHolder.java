package com.hiking.climbtogether.personal_chat_activity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PersonalChatRightViewHolder extends RecyclerView.ViewHolder {

    private TextView tvMessage;

    private TextView tvTime,tvOneTime,tvTwoTime;

    private RoundedImageView ivOnePhoto,ivTwoPhoto;
    private Context context;
    
    private PersonalChatLeftViewHolder.OnPhotoClickListenr listener;
    
    public void setOnPhotoClicklistener (PersonalChatLeftViewHolder.OnPhotoClickListenr listener){
        this.listener = listener;
    }

    public PersonalChatRightViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvOneTime = itemView.findViewById(R.id.chat_right_item_time_one);
        tvTwoTime = itemView.findViewById(R.id.chat_right_item_time_two);
        ivOnePhoto = itemView.findViewById(R.id.chat_right_image_one);
        ivTwoPhoto = itemView.findViewById(R.id.chat_right_image_two);
        tvMessage = itemView.findViewById(R.id.chat_item_message);
        tvTime = itemView.findViewById(R.id.chat_item_time);
    }

    public void setData(PersonalChatData personalChatData) {
        String hour = new SimpleDateFormat("HH", Locale.TAIWAN).format(new Date(personalChatData.getTime()));
        int hours = Integer.parseInt(hour);
        if (hours < 12) {
            hour = "上午";
        } else {
            hour = "下午";
        }
        String time = String.format(Locale.getDefault(), "%s%s", new SimpleDateFormat("HH:mm", Locale.TAIWAN).format(new Date(personalChatData.getTime())), hour);

        if (!personalChatData.getMessage().isEmpty()){
            tvMessage.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);
            ivOnePhoto.setVisibility(View.GONE);
            ivTwoPhoto.setVisibility(View.GONE);
            tvOneTime.setVisibility(View.GONE);
            tvTwoTime.setVisibility(View.GONE);
            tvMessage.setText(personalChatData.getMessage());
            tvTime.setText(time);
        }else if (personalChatData.getImageUrl().size() == 1){
            tvMessage.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            ivOnePhoto.setVisibility(View.VISIBLE);
            tvOneTime.setVisibility(View.VISIBLE);
            ivTwoPhoto.setVisibility(View.GONE);
            tvTwoTime.setVisibility(View.GONE);
            NewImageLoaderManager.getInstance(context).setPhotoUrl(personalChatData.getImageUrl().get(0),ivOnePhoto);
            tvOneTime.setText(time);
        }else if (personalChatData.getImageUrl().size() == 2){
            tvMessage.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            ivOnePhoto.setVisibility(View.VISIBLE);
            tvOneTime.setVisibility(View.GONE);
            ivTwoPhoto.setVisibility(View.VISIBLE);
            tvTwoTime.setVisibility(View.VISIBLE);
            NewImageLoaderManager.getInstance(context).setPhotoUrl(personalChatData.getImageUrl().get(0),ivOnePhoto);
            NewImageLoaderManager.getInstance(context).setPhotoUrl(personalChatData.getImageUrl().get(1),ivTwoPhoto);
            tvTwoTime.setText(time);
        }
        ivTwoPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(personalChatData.getImageUrl().get(1));
            }
        });
        ivOnePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(personalChatData.getImageUrl().get(0));
            }
        });

    }
}
