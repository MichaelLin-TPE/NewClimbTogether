package com.hiking.climbtogether.chat_activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatLeftViewHolder extends RecyclerView.ViewHolder {
    private TextView tvMessage;

    private TextView tvTime,tvName;

    private RoundedImageView ivUserPhoto;

    private Context context;

    private OnUserPhotoClickListener listener;

    public void setOnUserPhotoClickListener(OnUserPhotoClickListener listener){
        this.listener = listener;

    }

//    private TextView tvEmail;

    public ChatLeftViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvName = itemView.findViewById(R.id.chat_left_item_name);
        tvMessage = itemView.findViewById(R.id.chat_left_item_message);
        tvTime = itemView.findViewById(R.id.chat_left_item_time);
        ivUserPhoto = itemView.findViewById(R.id.chat_left_item_user_photo);
//        tvEmail = itemView.findViewById(R.id.chat_left_item_email);
    }



    public void setData(final ChatData chatData) {
        tvMessage.setText(chatData.getMessage());
        tvName.setText(chatData.getDisPlayName());
//        tvEmail.setText(String.format(Locale.getDefault(),"%s 說 : ",chatData.getEmail()));
        String hour = new SimpleDateFormat("HH",Locale.TAIWAN).format(new Date(chatData.getTime()));
        int houtInt = Integer.parseInt(hour);
        if (houtInt < 12){
            hour = "AM";
        }else {
            hour = "PM";
        }
        tvTime.setText(String.format(Locale.getDefault(),"%s %s",new SimpleDateFormat("HH:mm", Locale.TAIWAN).format(new Date(chatData.getTime())),hour));

        NewImageLoaderManager.getInstance(context).setPhotoUrl(chatData.getPhotoUrl(),ivUserPhoto);

        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(chatData.getEmail());
            }
        });
    }

    public interface OnUserPhotoClickListener{
        void onClick(String mail);
    }
}
