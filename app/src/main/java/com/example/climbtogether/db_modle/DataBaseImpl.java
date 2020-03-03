package com.example.climbtogether.db_modle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.SplittableRandom;

public class DataBaseImpl implements DataBaseApi {
    private static final String DB_NAME = "mountain_db.db";
    private Context ctx;

    public DataBaseImpl(Context ctx) {
        this.ctx = ctx;
        //複製DB
        File dbFile = ctx.getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            File parentDir = new File(dbFile.getParent());
            if (!parentDir.exists()) {
                parentDir.mkdir();
            }
            InputStream is = null;
            OutputStream os = null;
            try {
                is = ctx.getAssets().open(DB_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int read = is.read(buffer);
                while (read > 0) {
                    os.write(buffer, 0, read);
                    read = is.read(buffer);
                }
            } catch (Exception e) {
                Log.e("Michael", "複製DB失敗 : " + e.toString());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private SQLiteDatabase getReadableDatatbase() {
        File dbFile = ctx.getDatabasePath(DB_NAME);
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    private SQLiteDatabase getWriteableDatabase() {
        File dbFile = ctx.getDatabasePath(DB_NAME);
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public ArrayList<DataDTO> getAllInformation() {
        ArrayList<DataDTO> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatatbase();
        Cursor cursor = db.rawQuery("SELECT * FROM mountain_table", null);
        if (cursor.moveToFirst()) {
            do {
                DataDTO dataDTO = new DataDTO();
                dataDTO.fromCursor(cursor);
                data.add(dataDTO);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    @Override
    public ArrayList<DataDTO> getLevelAInformation(String levelType) {
        ArrayList<DataDTO> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatatbase();
        Cursor cursor = db.rawQuery("SELECT * FROM mountain_table WHERE difficulty = '" + levelType + "'", null);
        if (cursor.moveToFirst()) {
            do {
                DataDTO dataDTO = new DataDTO();
                dataDTO.fromCursor(cursor);
                data.add(dataDTO);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    @Override
    public ArrayList<DataDTO> getInformationOrderByTimeNotFar() {
        ArrayList<DataDTO> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatatbase();
        Cursor cursor = db.rawQuery("SELECT * FROM mountain_table ORDER BY time", null);
        if (cursor.moveToFirst()) {
            do {
                DataDTO dataDTO = new DataDTO();
                dataDTO.fromCursor(cursor);
                data.add(dataDTO);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    @Override
    public ArrayList<DataDTO> getInformationOrderByTimFar() {
        ArrayList<DataDTO> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatatbase();
        Cursor cursor = db.rawQuery("SELECT * FROM mountain_table", null);
        if (cursor.moveToFirst()) {
            do {
                DataDTO dataDTO = new DataDTO();
                dataDTO.fromCursor(cursor);
                data.add(dataDTO);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    @Override
    public ArrayList<EquipmentListDTO> getAllMyEquipment() {
        ArrayList<EquipmentListDTO> data  = new ArrayList<>();
        SQLiteDatabase db = getReadableDatatbase();
        Cursor cursor = db.rawQuery("SELECT * FROM equipment_list",null);
        if (cursor.moveToFirst()){
            do {
                EquipmentListDTO dataDto = new EquipmentListDTO();
                dataDto.fromCursor(cursor);
                data.add(dataDto);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return data;
    }

    @Override
    public ArrayList<EquipmentDTO> getStuffInformation(String stuffType) {
        ArrayList<EquipmentDTO> equipmentArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatatbase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + stuffType, null);
        if (cursor.moveToFirst()) {
            do {
                EquipmentDTO data = new EquipmentDTO();
                data.fromCursor(cursor);
                equipmentArrayList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return equipmentArrayList;
    }

    @Override
    public void update(DataDTO data) {
        SQLiteDatabase db = getWriteableDatabase();
        try {
            db.update("mountain_table", data.toContentValues(), "sid=?", new String[]{"" + data.getSid()});
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    @Override
    public void updateEquipmentData(EquipmentDTO data, String table) {
        SQLiteDatabase db = getWriteableDatabase();
        try {
            db.update(table, data.toContentValues(), "sid=?", new String[]{"" + data.getSid()});
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    @Override
    public EquipmentDTO getEquipmentBySid(String table, int sid) {
        EquipmentDTO data = null;
        SQLiteDatabase db = getReadableDatatbase();
        String sql = String.format(Locale.getDefault(), "SELECT * FROM %s WHERE sid=%d", table, sid);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            data = new EquipmentDTO();
            data.fromCursor(cursor);
        }
        cursor.close();
        db.close();
        return data;
    }


    @Override
    public DataDTO getDataBySid(int sid) {
        DataDTO data = null;
        SQLiteDatabase db = getReadableDatatbase();
        String sql = String.format(Locale.getDefault(), "SELECT * FROM mountain_table WHERE sid=%d", sid);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            data = new DataDTO();
            data.fromCursor(cursor);
        }
        cursor.close();
        db.close();
        return data;
    }
    @Override
    public void delete(int sid) {
        SQLiteDatabase db = getWriteableDatabase();
        try {
            db.delete("equipment_list", "sid=?", new String[]{"" + sid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    //新增資料
    @Override
    public void insert(EquipmentListDTO data) {
        SQLiteDatabase db = getWriteableDatabase();
        try {
            db.insert("equipment_list", null, data.toContentValues());
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
}
