package com.example.climbtogether.mountain_collection_activity.view_presenter;

import com.example.climbtogether.mountain_collection_activity.view.LandViewHolder;
import com.example.climbtogether.mountain_collection_activity.view.PortView;
import com.example.climbtogether.mountain_collection_activity.view.PortViewHolder;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

import java.util.ArrayList;

public interface MtPresenter {
    int getItemViewType(int position);

    int getItemCount();

    void setData(ArrayList<DataDTO> dataArrayList);

    void setViewType(int portView);

    int getSpanCount();

    void onBindPortViewHolder(PortViewHolder holder, int position);

    void onBindLandViewHolder(LandViewHolder holder, int position);

    void setOnPortViewClickListener(PortViewHolder holder,PortView.OnPortViewItemClickListener listener);

    void setOnLanViewClickListener(LandViewHolder holder, PortView.OnPortViewItemClickListener listener);
}
