package com.hiking.climbtogether.db_modle;

import com.hiking.climbtogether.personal_fragment.PersonalChatDTO;

import java.util.ArrayList;

public interface DataBaseApi {

    ArrayList<DataDTO> getAllInformation();

    ArrayList<DataDTO> getLevelAInformation(String level);

    ArrayList<PersonalChatDTO> getAllChatData();

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

    void insertChatData(PersonalChatDTO data);

    void updateChatData(PersonalChatDTO data);

    void deleteChatData(int sid);
}
