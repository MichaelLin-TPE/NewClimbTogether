package com.example.climbtogether.disscuss_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.climbtogether.R;


public class DiscussFragment extends Fragment {

    public static DiscussFragment newInstance() {
        DiscussFragment fragment = new DiscussFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discuss, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
