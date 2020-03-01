package com.example.climbtogether.equipment_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.equipment_fragment.stuff_presenter.StuffPresenter;

import static com.example.climbtogether.equipment_fragment.stuff_presenter.StuffPresenterImpl.BODY;

public class EquipmentAdapter extends RecyclerView.Adapter {


    private StuffPresenter stuffPresenter;

    private Context context;

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
        }
    }

    @Override
    public int getItemCount() {
        return stuffPresenter.getItemCount();
    }
}
