package com.example.climbtogether.mountain_collection_activity.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.db_modle.DataDTO;

public class LandViewHolder extends RecyclerView.ViewHolder {

    private LandView landView;

    public LandViewHolder(@NonNull View itemView) {
        super(itemView);

        landView = (LandView) itemView;
    }

    public void setData(DataDTO dataDTO, boolean isColorChange,int position) {
        landView.setData(dataDTO,isColorChange,position);
    }

    public void setOnLandViewItemClickListener(PortView.OnPortViewItemClickListener listener) {
        landView.setOnLandViewItemClickListener(listener);
    }
}
