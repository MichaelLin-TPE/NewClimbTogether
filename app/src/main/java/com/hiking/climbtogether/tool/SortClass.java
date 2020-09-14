package com.hiking.climbtogether.tool;

import com.hiking.climbtogether.db_modle.DataDTO;

import java.util.Comparator;

public class SortClass implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        DataDTO firstTime = (DataDTO)o1;
        DataDTO secondTime = (DataDTO)o2;

        if (firstTime.getTime() > secondTime.getTime()){
            return -1;
        }else {
            return 1;
        }

    }
}
