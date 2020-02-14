package com.example.climbtogether.mountain_collection_activity.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

import java.util.ArrayList;

public class PortViewHolder extends RecyclerView.ViewHolder {

    private PortView portView;

    private PortView.OnPortViewItemClickListener listener;

    public void setOnPortViewItemClickListener(PortView.OnPortViewItemClickListener listener){
        portView.setOnPortViewItemClickListener(listener);
    }

    public PortViewHolder(@NonNull View itemView) {
        super(itemView);

        portView = (PortView) itemView;
    }

    public void setData(DataDTO data,boolean isColorChange) {
        portView.setData(data,isColorChange);
    }
}
