package com.hiking.climbtogether.my_equipment_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private ArrayList<FriendData> dataArrayList;

    private OnFriendListClickListener listClickListener;

    private Context context;


    public void setOnFriendListClickListener(OnFriendListClickListener listClickListener){
        this.listClickListener = listClickListener;
    }


    public FriendAdapter(Context context) {
        this.context = context;
    }

    public void setDataArrayList(ArrayList<FriendData> dataArrayList){
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.friend_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendData data = dataArrayList.get(position);
        NewImageLoaderManager.getInstance(context).setPhotoUrl(data.getPhoto(),holder.ivPhoto);
        holder.tvName.setText(data.getName());
        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClickListener.onClick(data.getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView ivPhoto;

        private TextView tvName;

        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clickArea = itemView.findViewById(R.id.friend_click_area);
            ivPhoto = itemView.findViewById(R.id.friend_item_photo);
            tvName = itemView.findViewById(R.id.friend_item_name);
        }
    }

    public interface OnFriendListClickListener{
        void onClick(String email);
    }
}
