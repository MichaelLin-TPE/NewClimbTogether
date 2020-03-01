package com.example.climbtogether.db_modle;

import android.database.Cursor;

public class EquipmentDTO {

    private int sid;

    private String name;

    private String description;

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
    }

}
