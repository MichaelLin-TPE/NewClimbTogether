package com.example.climbtogether.mountain_fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.DataDTO;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MountainRecyclerViewAdapter extends RecyclerView.Adapter<MountainRecyclerViewAdapter.ViewHolder> {

    private Context context;

    private ArrayList<DataDTO> dataArrayList;

    private OnMountainItemClickListener listener;

    public MountainRecyclerViewAdapter(Context context){
        this.context = context;
    }
    public void setData(ArrayList<DataDTO> dataArrayList){
        this.dataArrayList = dataArrayList;
    }

    public void setOnMountainItemClickListener(OnMountainItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.mountain_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final DataDTO data = dataArrayList.get(position);

        holder.tvTitle.setText(data.getName());

        holder.tvHeight.setText(data.getHeight());

        holder.tvLocation.setText(data.getLocation());

        holder.tvDifficulty.setText(String.format(Locale.getDefault(),"難易度 : %s",data.getDifficulty()));

        byte[] ab = data.getPhoto();

        if (ab != null){
            holder.ivIcon.setImageBitmap(BitmapFactory.decodeByteArray(ab,0,ab.length));
        }

        holder.ivTopIcon.setImageResource(data.getCheck().equals("false") ? R.drawable.flag_no_top : R.drawable.flag_top);

        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(dataArrayList.get(position));
            }
        });

        holder.ivTopIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sid = dataArrayList.get(position).getSid();
                listener.onIconClick(sid);
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN);
        String time;
        if (data.getTime() != 0){
            time = sdf.format(new Date(data.getTime()));
        }else {
            time = "";
        }
        holder.tvTime.setText(time);

    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle,tvHeight,tvDifficulty,tvLocation,tvTime;
        private ImageView ivTopIcon;
        private ConstraintLayout clickArea;
        private RoundedImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.mountain_rather_title);
            tvHeight = itemView.findViewById(R.id.mountain_rather_height);
            tvDifficulty = itemView.findViewById(R.id.mountain_rather_difficulty);
            tvLocation = itemView.findViewById(R.id.mountain_rather_location);
            ivIcon = itemView.findViewById(R.id.mountain_rather_icon);
            ivTopIcon = itemView.findViewById(R.id.mountain_rather_top_icon);
            clickArea = itemView.findViewById(R.id.mountain_rather_click_area);
            tvTime = itemView.findViewById(R.id.mountain_rather_time);
        }
    }

    public interface OnMountainItemClickListener{
        void onClick(DataDTO data);

        void onIconClick(int sid);
    }
}
