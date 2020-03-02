package com.example.climbtogether.db_modle;

import android.content.ContentValues;
import android.database.Cursor;

public class EquipmentDTO {

    private int sid;

    private String name;

    private String description;

    private String check;

    private String sort;

    public String getCheck() {
        return check;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void fromCursor(Cursor cursor){
        sid = cursor.getInt(cursor.getColumnIndex("sid"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        description = cursor.getString(cursor.getColumnIndex("description"));
        check = cursor.getString(cursor.getColumnIndex("check_box"));
        sort = cursor.getString(cursor.getColumnIndex("sort"));
    }

    public ContentValues toContentValues(){
        ContentValues data = new ContentValues();
        data.put("check_box",this.check);
        return data;
    }

}
