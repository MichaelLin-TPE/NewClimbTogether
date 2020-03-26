package com.hiking.climbtogether.personal_chat_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.my_equipment_activity.FriendData;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class BottomShareAdapter extends RecyclerView.Adapter<BottomShareAdapter.ViewHolder> {

    private ArrayList<FriendData> friendDataArray;

    private Context context;

    private OnShareItemClickListener listener;

    public void setOnShareItemClickListener(OnShareItemClickListener listener){
        this.listener = listener;
    }

    public BottomShareAdapter(ArrayList<FriendData> friendDataArray, Context context) {
        this.friendDataArray = friendDataArray;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.bottom_share_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendData data = friendDataArray.get(position);

        NewImageLoaderManager.getInstance(context).setPhotoUrl(data.getPhoto(),holder.ivPhoto);
        holder.tvName.setText(data.getName());

        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(data);
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendDataArray == null ? 0 : friendDataArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivPhoto;

        private TextView tvName;

        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clickArea = itemView.findViewById(R.id.bottom_share_item_click_area);
            ivPhoto = itemView.findViewById(R.id.bottom_share_item_photo);
            tvName = itemView.findViewById(R.id.bottom_share_item_name);
        }
    }

    public interface OnShareItemClickListener{
        void onClick(FriendData data);
    }
}
