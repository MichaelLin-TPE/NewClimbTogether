package com.hiking.climbtogether.personal_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.tool.ImageLoaderManager;
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

    private OnChatItemLongClickListener longClickListener;

    public void setOnChatItemClickListener(OnChatItemClickListener listener){
        this.listener = listener;
    }

    public void setOnChatItemLongClickListener(OnChatItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }

    public void setData(ArrayList<PersonalChatDTO> dataList){
        this.dataList = dataList;
    }

    public PersonalFragmentAdapter(Context context) {
        this.context = context;
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

        if (data.getPhotoUrl().isEmpty()){
            holder.ivUserPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.ivUserPhoto.setImageResource(R.drawable.empty_photo);
        }else {
            holder.ivUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageLoaderManager.setPhotoUrl(data.getPhotoUrl(),holder.ivUserPhoto);
        }



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
                listener.onClick(data.getDisplayName(),data.getFriendEmail(),data.getPhotoUrl(),position,data.getDocumentPath());
            }
        });
        holder.clickArea.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onClick(data.getDocumentPath(),position);
                return false;
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
        void onClick(String displayName, String friendEmail, String photoUrl, int position, String documentPath);
    }
    public interface OnChatItemLongClickListener{
        void onClick(String documentPath,int itemPosition);
    }
}
