package com.example.climbtogether.home_fragment.news_view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MountNewsViewHolder extends RecyclerView.ViewHolder {
    private MountainNewsView mountainNewsView;

    public MountNewsViewHolder(@NonNull View itemView) {
        super(itemView);
        mountainNewsView = (MountainNewsView)itemView;
    }

    public void showNews() {
        mountainNewsView.showNews();
    }
}
