package com.example.soomin.contestproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "scheduledb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String medicalRecordSql = "create table medical_record (" +
                "_id integer primary key autoincrement," +
                "title," +
                "memo," +
                "name," +
                "date," +
                "dong_name," +
                "dong_tel," +
                "dong_address," +
                "type)";
        db.execSQL(medicalRecordSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == DATABASE_VERSION) {
            db.execSQL("drop table medical_record");
            onCreate(db);
        }
    }
}


