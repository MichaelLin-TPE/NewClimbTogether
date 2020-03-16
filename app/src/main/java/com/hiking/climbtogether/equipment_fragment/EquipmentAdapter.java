package com.hiking.climbtogether.equipment_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.equipment_fragment.stuff_presenter.StuffPresenter;

public class EquipmentAdapter extends RecyclerView.Adapter {


    private StuffPresenter stuffPresenter;

    private Context context;

    private StuffItemAdapter.OnItemCheckBoxClickListener listener;

    public void setOnItemCheckBoxListener(StuffItemAdapter.OnItemCheckBoxClickListener listener){
        this.listener = listener;
    }

    public EquipmentAdapter(StuffPresenter stuffPresenter, Context context) {
        this.stuffPresenter = stuffPresenter;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new EquipmentViewHolder(LayoutInflater.from(context).inflate(R.layout.equipment_item,parent,false),context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EquipmentViewHolder){
            stuffPresenter.onBindViewHolder((EquipmentViewHolder)holder,position);
            stuffPresenter.setOnItemCheckBoxListener((EquipmentViewHolder)holder,listener);
        }
    }

    @Override
    public int getItemCount() {
        return stuffPresenter.getItemCount();
    }
}
