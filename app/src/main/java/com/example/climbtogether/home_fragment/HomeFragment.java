package com.example.climbtogether.home_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.climbtogether.R;
import com.example.climbtogether.home_fragment.view_presenter.ViewPresenter;
import com.example.climbtogether.home_fragment.view_presenter.ViewPresenterImpl;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements HomeFragmentVu {


    private HomeFragmentPresenter homePresenter;

    private RecyclerView homeRecyclerView;

    private ViewPresenter viewPresenter;

    private Context context;

    private HomeRecyclerViewAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    private void initPresenter() {
        homePresenter = new HomeFragmentPresenterImpl(this);
        viewPresenter = new ViewPresenterImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initView(View view) {
        homeRecyclerView = view.findViewById(R.id.home_fragment_recycler_view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homePresenter.onPrepareData();


    }

    @Override
    public void showAnimationPhoto(ArrayList<String> photoUrlArray) {

        viewPresenter.onSetAnimationPhotoData(photoUrlArray);

        homePresenter.onPrepareSpinnerData();

    }

    @Override
    public Context getVuContext() {
        return context;
    }

    @Override
    public void showWeatherSpinner(ArrayList<String> nationParkNameArray) {
        viewPresenter.onSetSpinnerData(nationParkNameArray);
        viewPresenter.onShowNewsData();
        adapter = new HomeRecyclerViewAdapter(context,viewPresenter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,1);
        homeRecyclerView.setLayoutManager(gridLayoutManager);
        homeRecyclerView.setAdapter(adapter);
    }
}
