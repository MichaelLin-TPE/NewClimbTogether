package com.hiking.climbtogether.home_fragment.weather_view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.tool.WeatherHttpConnection;
import com.hiking.climbtogether.weather_parser.WeatherObject;


import java.io.File;
import java.util.ArrayList;

public class WeatherSpinnerView extends ConstraintLayout implements WeatherSpinnerVu {

    private TextView tvSpinner;

    private RecyclerView recyclerView;

    private Context context;

    private Handler handler;

    private ProgressBar progressBar;

    private WeatherSpinnerPresenter spinnerPresenter;

    private WeatherRecyclerViewAdapter adapter;


    public WeatherSpinnerView(Context context) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.weather_spinner_layout, this);



        //在這邊要使用到 runOnUiThread所以才用這個
        handler = new Handler(context.getMainLooper());
        initPresenter();
        initView(view);

    }

    private void initPresenter() {
        spinnerPresenter = new WeatherSpinnerPresenterImpl(this);
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.weather_progressbar);
        tvSpinner = view.findViewById(R.id.weather_spinner);
        recyclerView = view.findViewById(R.id.weather_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setData(ArrayList<String> nationParkNameArray) {

        tvSpinner.setText(nationParkNameArray.get(0));

        spinnerPresenter.onShowRecyclerView();


//        String url = "https://opendata.cwb.gov.tw/fileapi/v1/opendataapi/F-B0053-039?Authorization=CWB-CF93991C-7A79-4387-8A8B-D5F583B50AEC&downloadType=WEB&format=JSON";
//
//        WeatherHttpConnection connection = new WeatherHttpConnection();
//        connection.execute(url);
//        connection.setOnConnectionListener(new WeatherHttpConnection.OnConnectionListener() {
//            @Override
//            public void onSuccessful(String result) {
//                Log.i("Michael","Post successful : "+result);
//                Gson gson = new Gson();
//                WeatherObject object = gson.fromJson(result,WeatherObject.class);
//                Log.i("Michael","取得element : "+object.getCwbOpenData().getDataSet().getLocation().getLocation().get(0).getWeatherElement().get(0).getTime().get(0).getElementValue());
//            }
//
//            @Override
//            public void onFailure(String errorCode) {
//                Log.i("Michael","Post failure : "+errorCode);
//            }
//        });





        //改成客製化Dialog
        tvSpinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPresenter.onShowCustomDialog(nationParkNameArray);
            }
        });


    }

    //為了在mainThread做
    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }



    @Override
    public Context getVuContext() {
        return context;
    }

    @Override
    public void showRecyclerView(ArrayList<String> weatherUrl, final ArrayList<String> timeArray, final ArrayList<String> tempArray, final ArrayList<String> rainArray, final ArrayList<String> imgUrlArray) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new WeatherRecyclerViewAdapter(context);
                adapter.setData(timeArray,rainArray,tempArray,imgUrlArray);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showProgressBar(final boolean isShow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(isShow ? VISIBLE : GONE);
            }
        });

    }

    @Override
    public void showDialog(ArrayList<String> nationParkNameArray) {
        ArrayList<Integer> imageArray = new ArrayList<>();
        imageArray.add(R.drawable.yu_icon);
        imageArray.add(R.drawable.snow_icon);
        imageArray.add(R.drawable.ru_icon);
        imageArray.add(R.drawable.sun_icon);

        View view = View.inflate(getContext(),R.layout.weather_spinner_dialog,null);
        RecyclerView recyclerView = view.findViewById(R.id.weather_dialog_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        WeatherDialogAdapter adapter = new WeatherDialogAdapter(getContext(),nationParkNameArray,imageArray);

        recyclerView.setAdapter(adapter);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view).create();
        dialog.show();
        adapter.setOnWeatherDialogItemClickListener(new WeatherDialogAdapter.OnWeatherDialogItemClickListener() {
            @Override
            public void onClick(String name, int position) {
                tvSpinner.setText(name);
                spinnerPresenter.onSpinnerItemSelectListener(name,position);
                dialog.dismiss();
            }
        });
    }
}
