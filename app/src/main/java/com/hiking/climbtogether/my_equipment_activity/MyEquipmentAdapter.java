package com.hiking.climbtogether.my_equipment_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.my_equipment_activity.sort_presenter.SortPresenter;
import com.hiking.climbtogether.my_equipment_activity.view.NotPrepareViewHolder;
import com.hiking.climbtogether.my_equipment_activity.view.PreparedViewHolder;
import com.hiking.climbtogether.my_equipment_activity.view.SortAdapter;
import com.hiking.climbtogether.my_equipment_activity.view.SortPreparedAdapter;

import static com.hiking.climbtogether.my_equipment_activity.sort_presenter.SortPresenterImpl.NOT_PREPARE;
import static com.hiking.climbtogether.my_equipment_activity.sort_presenter.SortPresenterImpl.PREPARED;

public class MyEquipmentAdapter extends RecyclerView.Adapter {

    private Context context;

    private SortPresenter sortPresenter;

    private SortAdapter.OnSortItemClickListener listener;

    private SortPreparedAdapter.OnSortPreparedItemClickListener preparedListener;

    public void setOnSortPreparedItemClickListener(SortPreparedAdapter.OnSortPreparedItemClickListener listener){
        this.preparedListener = listener;
    }

    public void setOnSortItemClickListener(SortAdapter.OnSortItemClickListener listener){
        this.listener = listener;
    }

    public MyEquipmentAdapter(Context context, SortPresenter sortPresenter) {
        this.context = context;
        this.sortPresenter = sortPresenter;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case PREPARED:
                return new PreparedViewHolder(LayoutInflater.from(context).inflate(R.layout.sort_equipment_view, parent, false),context);
            case NOT_PREPARE:
                return new NotPrepareViewHolder(LayoutInflater.from(context).inflate(R.layout.sort_equipment_view, parent, false),context);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PreparedViewHolder){
            sortPresenter.onBindPrepareViewHolder((PreparedViewHolder)holder,position);
            sortPresenter.setOnSortItemClickListener((PreparedViewHolder)holder, preparedListener);
        }
        if (holder instanceof NotPrepareViewHolder){
            sortPresenter.onBindViewHolder((NotPrepareViewHolder)holder,position);
            sortPresenter.setOnSortItemClickListener((NotPrepareViewHolder)holder,listener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return sortPresenter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return sortPresenter.getItemCount();
    }
}
