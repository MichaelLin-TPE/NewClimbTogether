package com.example.climbtogether.mountain_fragment.db_modle;

import java.util.ArrayList;

public interface DataBaseApi {

    ArrayList<DataDTO> getAllInformation();

    ArrayList<DataDTO> getLevelAInformation(String level);

    void update(DataDTO data);

    DataDTO getDataBySid(int sid);
}
