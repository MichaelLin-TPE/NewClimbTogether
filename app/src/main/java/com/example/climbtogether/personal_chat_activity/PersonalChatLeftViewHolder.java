package com.example.climbtogether.personal_chat_activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.chat_activity.ChatData;
import com.example.climbtogether.chat_activity.ChatLeftViewHolder;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PersonalChatLeftViewHolder extends RecyclerView.ViewHolder {
    private TextView tvMessage;

    private TextView tvTime,tvName;

    private RoundedImageView ivUserPhoto;

    private ImageLoaderManager imageLoaderManager;

    private ChatLeftViewHolder.OnUserPhotoClickListener listener;

    public void setOnUserPhotoClickListener(ChatLeftViewHolder.OnUserPhotoClickListener listener){
        this.listener = listener;
    }



    public PersonalChatLeftViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        tvName = itemView.findViewById(R.id.chat_left_item_name);
        tvMessage = itemView.findViewById(R.id.chat_left_item_message);
        tvTime = itemView.findViewById(R.id.chat_left_item_time);
        ivUserPhoto = itemView.findViewById(R.id.chat_left_item_user_photo);
        imageLoaderManager = new ImageLoaderManager(context);
    }



    public void setData(PersonalChatData personalChatData,String displayName, String friendPhotoUrl) {
        imageLoaderManager.setPhotoUrl(friendPhotoUrl,ivUserPhoto);
        tvName.setText(displayName);
        tvMessage.setText(personalChatData.getMessage());
        String hour = new SimpleDateFormat("HH",Locale.TAIWAN).format(new Date(personalChatData.getTime()));
        int hours = Integer.parseInt(hour);
        if (hours < 12){
            hour = "上午";
        }else {
            hour = "下午";
        }
        tvTime.setText(String.format(Locale.getDefault(),"%s%s",new SimpleDateFormat("HH:mm",Locale.TAIWAN).format(new Date(personalChatData.getTime())),hour));
    }
}
