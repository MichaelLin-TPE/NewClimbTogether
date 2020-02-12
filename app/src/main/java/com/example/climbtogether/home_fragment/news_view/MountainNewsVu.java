package com.example.climbtogether.home_fragment.news_view;

import java.util.ArrayList;

public interface MountainNewsVu {
    void showRecyclerView(ArrayList<String> titleArrayList, ArrayList<String> locationArrayList, ArrayList<String> timeArrayList, ArrayList<String> newsUrlArrayList);

    void intentToBrowser(String url);

    void showProgressBar(boolean isShow);
}
