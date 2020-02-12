package com.example.climbtogether.home_fragment.weather_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Locale;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> timeArray;

    private ArrayList<String> rainArray;

    private ArrayList<String> tempArray;

    private ArrayList<String> imgUrlArray;

    private Context context;

    private DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public WeatherRecyclerViewAdapter(Context context){

        this.context = context;
        initImageLoader();
    }
    public void setData( ArrayList<String> timeArray , ArrayList<String> rainArray,ArrayList<String> tempArray,ArrayList<String> imgUrlArray){
        this.timeArray = timeArray;
        this.rainArray = rainArray;
        this.tempArray = tempArray;
        this.imgUrlArray = imgUrlArray;
    }

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options).build();
        imageLoader.init(config);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.national_park_weather_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTime.setText(timeArray.get(position));
        holder.tvRain.setText(rainArray.get(position));
        holder.tvTemp.setText(String.format(Locale.getDefault(),"%s℃",tempArray.get(position)));
        imageLoader.displayImage(imgUrlArray.get(position),holder.ivIcon,options);
    }

    @Override
    public int getItemCount() {
        return timeArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTime;

        private TextView tvTemp;

        private TextView tvRain;

        private ImageView ivIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.weather_text_time);
            tvTemp = itemView.findViewById(R.id.weather_text_temp);
            tvRain = itemView.findViewById(R.id.weather_text_rain);
            ivIcon = itemView.findViewById(R.id.weather_big_icon);
        }
    }
}
