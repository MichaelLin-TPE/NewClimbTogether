package com.example.climbtogether.mountain_collection_activity.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

public class LandViewHolder extends RecyclerView.ViewHolder {

    private LandView landView;

    public LandViewHolder(@NonNull View itemView) {
        super(itemView);

        landView = (LandView) itemView;
    }

    public void setData(DataDTO dataDTO, boolean isColorChange) {
        landView.setData(dataDTO,isColorChange);
    }
}
