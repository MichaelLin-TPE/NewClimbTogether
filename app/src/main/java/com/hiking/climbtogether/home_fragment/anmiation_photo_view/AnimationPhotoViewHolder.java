package com.hiking.climbtogether.home_fragment.anmiation_photo_view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnimationPhotoViewHolder extends RecyclerView.ViewHolder {

    private AnimationPhotoView animationPhotoView;

    public AnimationPhotoViewHolder(@NonNull View itemView) {
        super(itemView);

        animationPhotoView = (AnimationPhotoView)itemView;
    }

    public void setPhotoUrlData(ArrayList<String> photoUrlArray) {
        animationPhotoView.setPhotoData(photoUrlArray);
    }
}
