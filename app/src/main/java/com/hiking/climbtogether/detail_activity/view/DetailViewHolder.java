package com.hiking.climbtogether.detail_activity.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.DataDTO;

public class DetailViewHolder extends RecyclerView.ViewHolder {
    private TextView tvContent,tvMeter,tvTitle,tvDifficult,tvPath,tvLocation;

    public DetailViewHolder(@NonNull View itemView) {
        super(itemView);
        tvContent = itemView.findViewById(R.id.detail_content);
        tvMeter = itemView.findViewById(R.id.detail_meter);
        tvTitle = itemView.findViewById(R.id.detail_title);
        tvDifficult = itemView.findViewById(R.id.detail_difficult);
        tvPath = itemView.findViewById(R.id.detail_path);
        tvLocation = itemView.findViewById(R.id.detail_location);
    }

    public void setData(DataDTO data) {
        tvContent.setText(data.getContent());
        tvMeter.setText(String.format("海拔 : %s",data.getHeight()));
        tvTitle.setText(String.format("評選 : %s",data.getAllTitle()));
        tvDifficult.setText(String.format("難度 : %s",data.getDifficulty()));
        tvPath.setText(String.format("天數 : %s",data.getDay()));
        tvLocation.setText(String.format("地點 : %s",data.getLocation()));
    }
}
