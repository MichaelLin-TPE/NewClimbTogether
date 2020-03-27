package com.hiking.climbtogether.favorite_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.detail_activity.MountainFavoriteData;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context context;

    private ArrayList<MountainFavoriteData> dataArrayList;

    public FavoriteAdapter(Context context, ArrayList<MountainFavoriteData> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.favorite_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MountainFavoriteData data = dataArrayList.get(position);
        NewImageLoaderManager.getInstance(context).setPhotoUrl(data.getPhoto(),holder.ivPhoto);
        holder.tvName.setText(data.getName());
        holder.tvMeter.setText(data.getHeight());
        holder.tvLocation.setText(data.getLocation());
        holder.tvDifficult.setText(data.getDifficulty());
        holder.tvTitle.setText(data.getAllTitle());
        holder.tvDays.setText(data.getDay());
        if (data.getTime() != 0){
            holder.tvTop.setText(context.getString(R.string.already_top));
        }else {
            holder.tvTop.setText(context.getString(R.string.not_top));
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivPhoto;

        private TextView tvName,tvMeter,tvLocation,tvDifficult,tvTitle,tvDays,tvTop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.favorite_photo);
            tvName = itemView.findViewById(R.id.favorite_name);
            tvMeter = itemView.findViewById(R.id.favorite_meter);
            tvLocation = itemView.findViewById(R.id.favorite_location);
            tvDifficult = itemView.findViewById(R.id.favorite_difficult);
            tvTitle = itemView.findViewById(R.id.favorite_title);
            tvDays = itemView.findViewById(R.id.favorite_day);
            tvTop = itemView.findViewById(R.id.favorite_top);
        }
    }
}
