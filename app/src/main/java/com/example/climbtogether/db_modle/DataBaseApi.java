package com.example.climbtogether.db_modle;

import java.util.ArrayList;

public interface DataBaseApi {

    ArrayList<DataDTO> getAllInformation();

    ArrayList<DataDTO> getLevelAInformation(String level);

    ArrayList<DataDTO> getInformationOrderByTimeNotFar();

    ArrayList<DataDTO> getInformationOrderByTimFar();

    ArrayList<EquipmentDTO> getStuffInformation(String stuffType);

    void update(DataDTO data);

    DataDTO getDataBySid(int sid);
}
