package com.example.climbtogether.personal_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PersonalFragmentAdapter extends RecyclerView.Adapter<PersonalFragmentAdapter.ViewHolder> {


    private Context context;

    private ArrayList<PersonalChatDTO> dataList;

    private ImageLoaderManager imageLoaderManager;

    private OnChatItemClickListener listener;

    public void setOnChatItemClickListener(OnChatItemClickListener listener){
        this.listener = listener;
    }

    public PersonalFragmentAdapter(Context context, ArrayList<PersonalChatDTO> dataList) {
        this.context = context;
        this.dataList = dataList;
        imageLoaderManager = new ImageLoaderManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.personal_recycler_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PersonalChatDTO data = dataList.get(position);
        imageLoaderManager.setPhotoUrl(data.getPhotoUrl(),holder.ivUserPhoto);

        int hour = Integer.parseInt(new SimpleDateFormat("HH", Locale.TAIWAN).format(new Date(data.getTime())));
        String hours;
        if (hour < 12){
            hours = "上午";
        }else {
            hours = "下午";
        }
        holder.tvTime.setText(String.format(Locale.getDefault(),"%s %s",hours,new SimpleDateFormat("HH:mm",Locale.TAIWAN).format(new Date(data.getTime()))));

        holder.tvTitle.setText(data.getDisplayName());

        holder.tvMessage.setText(data.getMessage());

        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data.getDisplayName(),data.getFriendEmail(),data.getPhotoUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivUserPhoto;

        private TextView tvMessage,tvTime,tvTitle;

        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clickArea = itemView.findViewById(R.id.personal_item_click_area);
            ivUserPhoto = itemView.findViewById(R.id.personal_item_user_photo);
            tvMessage = itemView.findViewById(R.id.personal_item_message);
            tvTitle = itemView.findViewById(R.id.personal_item_display_name);
            tvTime = itemView.findViewById(R.id.personal_item_time);
        }
    }
    public interface OnChatItemClickListener{
        void onClick(String displayName,String friendEmail,String photoUrl);
    }
}
