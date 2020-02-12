package com.example.climbtogether.equipment_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.climbtogether.R;

public class EquipmentFragment extends Fragment {

    public static EquipmentFragment newInstance() {
        EquipmentFragment fragment = new EquipmentFragment();

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
        return inflater.inflate(R.layout.fragment_equipment, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
