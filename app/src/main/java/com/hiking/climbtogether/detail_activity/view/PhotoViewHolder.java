package com.hiking.climbtogether.detail_activity.view;

import android.graphics.BitmapFactory;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.tool.ImageLoaderProvider;
import com.makeramen.roundedimageview.RoundedImageView;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private RoundedImageView ivPhoto;

    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        ivPhoto = itemView.findViewById(R.id.detail_photo);
    }

    public void setData(String photo) {
        ImageLoaderProvider.getInstance().setImage(photo,ivPhoto);
    }
}
