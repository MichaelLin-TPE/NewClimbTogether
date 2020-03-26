package com.hiking.climbtogether.personal_chat_activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.chat_activity.ChatLeftViewHolder;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PersonalChatLeftViewHolder extends RecyclerView.ViewHolder {
    private TextView tvMessage;

    private TextView tvTime,tvName,tvOneTime,tvTwoTime;

    private RoundedImageView ivUserPhoto,ivOnePhoto,ivTwoPhoto;

    private ImageView ivShareOne,ivShareTwo;

    private Context context;

    private OnPhotoClickListenr listenr;

    public void setOnPhotoClickListenr(OnPhotoClickListenr listenr){
        this.listenr = listenr;
    }



    public PersonalChatLeftViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvName = itemView.findViewById(R.id.chat_left_item_name);
        tvMessage = itemView.findViewById(R.id.chat_left_item_message);
        tvTime = itemView.findViewById(R.id.chat_left_item_time);
        ivUserPhoto = itemView.findViewById(R.id.chat_left_item_user_photo);
        tvOneTime = itemView.findViewById(R.id.chat_left_item_time_one);
        tvTwoTime = itemView.findViewById(R.id.chat_left_item_time_two);
        ivOnePhoto = itemView.findViewById(R.id.chat_left_image_one);
        ivTwoPhoto = itemView.findViewById(R.id.chat_left_image_two);
        ivShareOne = itemView.findViewById(R.id.chat_left_share_image_one);
        ivShareTwo = itemView.findViewById(R.id.chat_left_share_image_two);
    }



    public void setData(PersonalChatData personalChatData, String displayName, String friendPhotoUrl) {
        if (friendPhotoUrl.isEmpty()){
            ivUserPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else {
            ivUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        tvName.setText(displayName);
        NewImageLoaderManager.getInstance(context).setPhotoUrl(friendPhotoUrl,ivUserPhoto);
        String hour = new SimpleDateFormat("HH",Locale.TAIWAN).format(new Date(personalChatData.getTime()));
        int hours = Integer.parseInt(hour);
        if (hours < 12){
            hour = "上午";
        }else {
            hour = "下午";
        }
        String time = String.format(Locale.getDefault(),"%s%s",new SimpleDateFormat("HH:mm",Locale.TAIWAN).format(new Date(personalChatData.getTime())),hour);

        if (!personalChatData.getMessage().isEmpty()){
            tvMessage.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);
            ivTwoPhoto.setVisibility(View.GONE);
            tvTwoTime.setVisibility(View.GONE);
            ivOnePhoto.setVisibility(View.GONE);
            tvOneTime.setVisibility(View.GONE);
            ivShareTwo.setVisibility(View.GONE);
            ivShareOne.setVisibility(View.GONE);
            tvMessage.setText(personalChatData.getMessage());
            tvTime.setText(time);
        }else if (personalChatData.getImageUrl().size() == 1){
            tvMessage.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            ivTwoPhoto.setVisibility(View.GONE);
            tvTwoTime.setVisibility(View.GONE);
            ivOnePhoto.setVisibility(View.VISIBLE);
            tvOneTime.setVisibility(View.VISIBLE);
            ivShareTwo.setVisibility(View.GONE);
            ivShareOne.setVisibility(View.VISIBLE);
            NewImageLoaderManager.getInstance(context).setPhotoUrl(personalChatData.getImageUrl().get(0),ivOnePhoto);
            tvOneTime.setText(time);
        }else if (personalChatData.getImageUrl().size() == 2){
            tvMessage.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            ivTwoPhoto.setVisibility(View.VISIBLE);
            tvTwoTime.setVisibility(View.VISIBLE);
            ivOnePhoto.setVisibility(View.VISIBLE);
            tvOneTime.setVisibility(View.GONE);
            ivShareTwo.setVisibility(View.VISIBLE);
            ivShareOne.setVisibility(View.GONE);
            NewImageLoaderManager.getInstance(context).setPhotoUrl(personalChatData.getImageUrl().get(0),ivOnePhoto);
            NewImageLoaderManager.getInstance(context).setPhotoUrl(personalChatData.getImageUrl().get(1),ivTwoPhoto);
            tvTwoTime.setText(time);
        }

        ivOnePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenr.onClick(personalChatData.getImageUrl().get(0));
            }
        });
        ivTwoPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenr.onClick(personalChatData.getImageUrl().get(1));
            }
        });
        ivShareOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenr.onShareClick(personalChatData.getImageUrl());
            }
        });
        ivShareTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenr.onShareClick(personalChatData.getImageUrl());
            }
        });
    }

    public interface OnPhotoClickListenr{
        void onClick(String downLoadUrl);
        void onShareClick(ArrayList<String> downloadUrl);
    }
}
