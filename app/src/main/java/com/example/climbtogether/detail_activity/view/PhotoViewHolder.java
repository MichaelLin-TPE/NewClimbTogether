package com.example.climbtogether.detail_activity.view;

import android.graphics.BitmapFactory;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private RoundedImageView ivPhoto;

    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setData(byte[] photo) {
        ivPhoto.setImageBitmap(BitmapFactory.decodeByteArray(photo,0,photo.length));
    }
}
