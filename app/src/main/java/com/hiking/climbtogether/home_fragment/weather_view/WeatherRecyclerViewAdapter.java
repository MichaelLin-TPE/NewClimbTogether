package com.hiking.climbtogether.home_fragment.weather_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.home_fragment.json_object.WeatherElement;
import com.hiking.climbtogether.home_fragment.json_object.WeatherLocation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Locale;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {

    private ArrayList<WeatherElement> dataArray;

    private Context context;

    private DisplayImageOptions options;

    private ArrayList<String> rainArray,timeArray,tempArray,descriptionArray;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public WeatherRecyclerViewAdapter(Context context){

        this.context = context;
        initImageLoader();
    }
    public void setData( ArrayList<WeatherElement> dataArray){
        this.dataArray = dataArray;
        rainArray = new ArrayList<>();
        timeArray = new ArrayList<>();
        tempArray = new ArrayList<>();
        descriptionArray = new ArrayList<>();
        for (int i = 0 ; i < dataArray.size() ; i ++){
            for (int j = 0 ; j < dataArray.get(0).getTime().size() ; j ++){
                if (dataArray.get(i).getDescription().equals("12小時降雨機率")){
                    if (j % 2 == 0){
                        rainArray.add(dataArray.get(i).getTime().get(j).getElementValue().get(0).getValue()+"%");
                        timeArray.add(dataArray.get(i).getTime().get(j).getStartTime().substring(0,10));
                    }
                }
                if (dataArray.get(i).getDescription().equals("最高溫度")){
                    if (j % 2 == 0){
                        tempArray.add(dataArray.get(i).getTime().get(j).getElementValue().get(0).getValue());
                    }
                }
                if (dataArray.get(i).getDescription().equals("天氣預報綜合描述")){
                    if (j % 2 == 0){
                        descriptionArray.add(dataArray.get(i).getTime().get(j).getElementValue().get(0).getValue());
                    }
                }
            }
        }

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
//        imageLoader.displayImage(imgUrlArray.get(position),holder.ivIcon,options);
        if (descriptionArray.get(position).contains("雲")){
            holder.ivIcon.setImageResource(R.drawable.cloudy);
        }else if (descriptionArray.get(position).contains("晴")){
            holder.ivIcon.setImageResource(R.drawable.sun);
        }else if (descriptionArray.get(position).contains("雨")){
            holder.ivIcon.setImageResource(R.drawable.rain);
        }else {
            holder.ivIcon.setImageResource(R.drawable.cloudy);
        }
    }

    @Override
    public int getItemCount() {
        return dataArray == null ? 0 : dataArray.get(0).getTime().size()/2;
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
