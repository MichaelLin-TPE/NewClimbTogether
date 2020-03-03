package com.example.climbtogether.db_modle;

import java.util.ArrayList;

public interface DataBaseApi {

    ArrayList<DataDTO> getAllInformation();

    ArrayList<DataDTO> getLevelAInformation(String level);

    ArrayList<DataDTO> getInformationOrderByTimeNotFar();

    ArrayList<DataDTO> getInformationOrderByTimFar();

    ArrayList<EquipmentListDTO> getAllMyEquipment();

    ArrayList<EquipmentDTO> getStuffInformation(String stuffType);

    void update(DataDTO data);

    EquipmentDTO getEquipmentBySid(String table,int sid);

    void updateEquipmentData(EquipmentDTO data,String table);

    DataDTO getDataBySid(int sid);

    void delete(int sid);

    void insert(EquipmentListDTO data);
}
