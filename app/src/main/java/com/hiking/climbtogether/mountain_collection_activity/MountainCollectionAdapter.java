package com.hiking.climbtogether.mountain_collection_activity;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.mountain_collection_activity.view.LandView;
import com.hiking.climbtogether.mountain_collection_activity.view.LandViewHolder;
import com.hiking.climbtogether.mountain_collection_activity.view.PortView;
import com.hiking.climbtogether.mountain_collection_activity.view.PortViewHolder;
import com.hiking.climbtogether.mountain_collection_activity.view_presenter.MtPresenter;

import static com.hiking.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl.LAND_VIEW;
import static com.hiking.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl.PORT_VIEW;

public class MountainCollectionAdapter extends RecyclerView.Adapter {

    private MtPresenter mtPresenter;
    private Context context;
    private PortView.OnPortViewItemClickListener listener;

    public void setOnPortViewItemClickListener (PortView.OnPortViewItemClickListener listener){
        this.listener = listener;
    }

    public MountainCollectionAdapter(Context context, MtPresenter mtPresenter) {
        this.mtPresenter = mtPresenter;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case PORT_VIEW:
                PortView portView = new PortView(context);
                return new PortViewHolder(portView);
            case LAND_VIEW:
                LandView landView = new LandView(context);
                return new LandViewHolder(landView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PortViewHolder){
            mtPresenter.onBindPortViewHolder((PortViewHolder)holder,position);
            mtPresenter.setOnPortViewClickListener((PortViewHolder)holder,listener);
        }
        if (holder instanceof LandViewHolder){
            mtPresenter.onBindLandViewHolder((LandViewHolder)holder,position);
            mtPresenter.setOnLanViewClickListener((LandViewHolder)holder,listener);
        }
    }

    @Override
    public int getItemCount() {
        return mtPresenter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mtPresenter.getItemViewType(position);
    }

}
