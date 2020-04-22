package com.hiking.climbtogether.home_fragment.weather_view;

import android.util.Log;

import com.google.gson.Gson;
import com.hiking.climbtogether.home_fragment.json_object.WeatherElement;
import com.hiking.climbtogether.home_fragment.json_object.WeatherLocation;
import com.hiking.climbtogether.home_fragment.json_object.WeatherObject;
import com.hiking.climbtogether.tool.WeatherHttpConnection;

import java.util.ArrayList;

public class WeatherSpinnerPresenterImpl implements WeatherSpinnerPresenter {

    private WeatherSpinnerVu mView;

    private ArrayList<String> locationArray;

    private Gson gson;
    private WeatherObject object;

    public WeatherSpinnerPresenterImpl(WeatherSpinnerVu mView) {
        this.mView = mView;
        gson = new Gson();
    }

    @Override
    public void onGetWeatherData() {
        //目前的JSON 網址
        String url = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-091?Authorization=CWB-CF93991C-7A79-4387-8A8B-D5F583B50AEC&locationName=%E5%AE%9C%E8%98%AD%E7%B8%A3,%E8%8A%B1%E8%93%AE%E7%B8%A3,%E8%87%BA%E6%9D%B1%E7%B8%A3,%E6%BE%8E%E6%B9%96%E7%B8%A3,%E9%87%91%E9%96%80%E7%B8%A3,%E9%80%A3%E6%B1%9F%E7%B8%A3,%E8%87%BA%E5%8C%97%E5%B8%82,%E6%96%B0%E5%8C%97%E5%B8%82,%E6%A1%83%E5%9C%92%E5%B8%82,%E8%87%BA%E4%B8%AD%E5%B8%82,%E8%87%BA%E5%8D%97%E5%B8%82,%E9%AB%98%E9%9B%84%E5%B8%82,%E5%9F%BA%E9%9A%86%E5%B8%82,%E6%96%B0%E7%AB%B9%E7%B8%A3,%E6%96%B0%E7%AB%B9%E5%B8%82,%E8%8B%97%E6%A0%97%E7%B8%A3,%E5%BD%B0%E5%8C%96%E7%B8%A3,%E5%8D%97%E6%8A%95%E7%B8%A3,%E9%9B%B2%E6%9E%97%E7%B8%A3,%E5%98%89%E7%BE%A9%E7%B8%A3,%E5%98%89%E7%BE%A9%E5%B8%82,%E5%B1%8F%E6%9D%B1%E7%B8%A3&elementName=MinT,MaxT,PoP12h,WeatherDescription";

        mView.showProgress(true);

        WeatherHttpConnection connection = new WeatherHttpConnection();
        connection.execute(url);
        connection.setOnConnectionListener(new WeatherHttpConnection.OnConnectionListener() {
            @Override
            public void onSuccessful(String result) {

                Log.i("Michael", "Post successful : " + result);

                object = gson.fromJson(result, WeatherObject.class);
                Log.i("Michael", "取城市名稱 : " + object.getRecords().getLocations().get(0).getLocation().get(0).getLocationName());

                locationArray = new ArrayList<>();
                for (WeatherLocation data : object.getRecords().getLocations().get(0).getLocation()) {
                    locationArray.add(data.getLocationName());
                }
                if (locationArray.size() != 0){
                    mView.setSpinnerText(locationArray.get(0));
                    ArrayList<WeatherElement> dataArray = object.getRecords().getLocations().get(0).getLocation().get(0).getWeatherElement();
                    mView.showProgress(false);
                    mView.setRecyclerView(dataArray);
                }


            }

            @Override
            public void onFailure(String errorCode) {
                Log.i("Michael", "Post failure : " + errorCode);
            }
        });
    }

    @Override
    public void onTvSpinnerClickListener() {
        mView.showLocationListDialog(locationArray);
    }

    @Override
    public void onLocationItemClickListener(String name, int itemPosition) {

        mView.setSpinnerText(name);
        mView.changeView(itemPosition);

        ArrayList<WeatherElement> dataArray = object.getRecords().getLocations().get(0).getLocation().get(itemPosition).getWeatherElement();

        mView.changeRecyclerView(dataArray);
    }
}
