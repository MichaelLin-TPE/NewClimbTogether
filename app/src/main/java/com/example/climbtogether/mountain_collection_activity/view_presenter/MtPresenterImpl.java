package com.example.climbtogether.mountain_collection_activity.view_presenter;

import com.example.climbtogether.mountain_collection_activity.view.LandView;
import com.example.climbtogether.mountain_collection_activity.view.LandViewHolder;
import com.example.climbtogether.mountain_collection_activity.view.PortView;
import com.example.climbtogether.mountain_collection_activity.view.PortViewHolder;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

public class MtPresenterImpl implements MtPresenter {

    public static final int PORT_VIEW = 0;

    public static final int LAND_VIEW = 1;

    private boolean isPortShow;

    private boolean isLandShow;

    private boolean isColorChange;

    private ArrayList<DataDTO> dataArrayList;


    @Override
    public int getItemViewType(int position) {
        if (isPortShow){
            return PORT_VIEW;
        }
        if (isLandShow){
            return LAND_VIEW;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    @Override
    public void setData(ArrayList<DataDTO> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public void setViewType(int viewType) {
        if (viewType == PORT_VIEW){
            isPortShow = true;
            isLandShow = false;
        }
        if (viewType == LAND_VIEW){
            isLandShow = true;
            isPortShow = false;
        }
    }

    @Override
    public int getSpanCount() {
        if (isPortShow){
            return 1;
        }
        if (isLandShow){
            return 2;
        }
        return 0;
    }

    @Override
    public void onBindPortViewHolder(PortViewHolder holder, int position) {
        if (isColorChange){
            holder.setData(dataArrayList.get(position),isColorChange,position);
            isColorChange = false;
        }else {
            holder.setData(dataArrayList.get(position),isColorChange,position);
            isColorChange = true;
        }


    }

    @Override
    public void onBindLandViewHolder(LandViewHolder holder, int position) {
        if (isColorChange){
            holder.setData(dataArrayList.get(position),isColorChange,position);
            isColorChange = false;
        }else {
            holder.setData(dataArrayList.get(position),isColorChange,position);
            isColorChange = true;
        }
    }

    @Override
    public void setOnPortViewClickListener(PortViewHolder holder,PortView.OnPortViewItemClickListener listener) {
        holder.setOnPortViewItemClickListener(listener);
    }

    @Override
    public void setOnLanViewClickListener(LandViewHolder holder, PortView.OnPortViewItemClickListener listener) {
        holder.setOnLandViewItemClickListener(listener);
    }
}
