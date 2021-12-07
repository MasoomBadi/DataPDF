package com.phoenix.datapdf.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.phoenix.datapdf.modals.Agri;
import com.phoenix.datapdf.modals.DisplayData;

import java.util.ArrayList;
import java.util.List;

public class FarmerDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Allfarmer.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public FarmerDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                FarmerContract.FarmerTable.TABLE_NAME + " ( " +
                FarmerContract.FarmerTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FarmerContract.FarmerTable.COL_NAME + " TEXT, " +
                FarmerContract.FarmerTable.COL_REGNO + " TEXT, " +
                FarmerContract.FarmerTable.COL_SONOF + " TEXT, " +
                FarmerContract.FarmerTable.COL_DOB + " TEXT, " +
                FarmerContract.FarmerTable.COL_PHONE + " TEXT, " +
                FarmerContract.FarmerTable.COL_AADHAR + " TEXT, " +
                FarmerContract.FarmerTable.COL_BLOCK + " TEXT, " +
                FarmerContract.FarmerTable.COL_VILLAGE + " TEXT, " +
                FarmerContract.FarmerTable.COL_PANCHAYAT + " TEXT, " +
                FarmerContract.FarmerTable.COL_DISTRICT + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + FarmerContract.FarmerTable.TABLE_NAME);
        onCreate(db);
    }

    void fillData() {
        Agri agri1 = new Agri("John Doe",
                "6554 1234 2951",
                "Jane Doe",
                "10-Jan-1980",
                "8401234567",
                "2341 46128 1234",
                "Bikram",
                "Mani",
                "Mani",
                "Rohtas, Bihar");

        addFarmer(agri1);

        Agri agri2 = new Agri("Paul Doglas",
                "6554 1234 2951",
                "Wade Doglas",
                "10-Aug-1984",
                "8401234567",
                "2341 46128 1234",
                "Bikram",
                "Mani",
                "Mani",
                "Rohtas, Bihar");

        addFarmer(agri2);

        Agri agri3 = new Agri("Vincent Carzola",
                "6554 1234 2951",
                "Santi Carzola",
                "10-Sep-1984",
                "8401234567",
                "2341 46128 1234",
                "Bikram",
                "Mani",
                "Mani",
                "Rohtas, Bihar");

        addFarmer(agri3);
    }

    private void addFarmer(Agri agri) {
        ContentValues cv = new ContentValues();
        cv.put(FarmerContract.FarmerTable.COL_NAME, agri.getName());
        cv.put(FarmerContract.FarmerTable.COL_REGNO, agri.getRegNo());
        cv.put(FarmerContract.FarmerTable.COL_SONOF, agri.getSonOf());
        cv.put(FarmerContract.FarmerTable.COL_DOB, agri.getDob());
        cv.put(FarmerContract.FarmerTable.COL_PHONE, agri.getPhone());
        cv.put(FarmerContract.FarmerTable.COL_AADHAR, agri.getAadharNo());
        cv.put(FarmerContract.FarmerTable.COL_BLOCK, agri.getBlock());
        cv.put(FarmerContract.FarmerTable.COL_VILLAGE, agri.getVillage());
        cv.put(FarmerContract.FarmerTable.COL_PANCHAYAT, agri.getPanchayat());
        cv.put(FarmerContract.FarmerTable.COL_DISTRICT, agri.getDistrict());
        db.insert(FarmerContract.FarmerTable.TABLE_NAME, null, cv);
    }

    public List<DisplayData> getDisplayData() {
        List<DisplayData> DataList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT "
                + FarmerContract.FarmerTable.COL_NAME
                + ", "
                + FarmerContract.FarmerTable._ID
                + ", "
                + FarmerContract.FarmerTable.COL_REGNO
                +" FROM "
                + FarmerContract.FarmerTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                DisplayData data = new DisplayData();
                data.setName(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_NAME)));
                data.setRegNo(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_REGNO)));
                data.setId(c.getString(c.getColumnIndex(FarmerContract.FarmerTable._ID)));
                DataList.add(data);
            } while (c.moveToNext());
        }
        c.close();
        return DataList;
    }

    public List<Agri> getSingleData(String id) {
        List<Agri> DataList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "
                + FarmerContract.FarmerTable.TABLE_NAME
                + " WHERE "
                + FarmerContract.FarmerTable._ID
                + " = "
                + id, null);
        if (c.moveToFirst()) {
            do {
                Agri data = new Agri();
                data.setName(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_NAME)));
                data.setRegNo(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_REGNO)));
                data.setSonOf(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_SONOF)));
                data.setDob(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_DOB)));
                data.setPhone(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_PHONE)));
                data.setAadharNo(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_AADHAR)));
                data.setBlock(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_BLOCK)));
                data.setVillage(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_VILLAGE)));
                data.setPanchayat(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_PANCHAYAT)));
                data.setDistrict(c.getString(c.getColumnIndex(FarmerContract.FarmerTable.COL_DISTRICT)));
                DataList.add(data);
            } while (c.moveToNext());
        }
        c.close();
        return DataList;
    }
}
