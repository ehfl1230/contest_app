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
                "name integer," +
                "date," +
                "dong_name," +
                "dong_tel," +
                "dong_address," +
                "type," +
                "dong_old_address," +
                "dong_lat," +
                "dong_lng)";
        String bookmarkSql = "create table bookmark (" +
                "_id integer primary key autoincrement," +
                "dong_name," +
                "dong_address," +
                "dong_tel," +
                "type, " +
                "dong_old_address," +
                "dong_lat," +
                "dong_lng)";
        String animalSql = "create table animal (" +
                "_id integer primary key autoincrement," +
                "animal_name)";
        db.execSQL(medicalRecordSql);
        db.execSQL(bookmarkSql);
        db.execSQL(animalSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == DATABASE_VERSION) {
            db.execSQL("drop table medical_record");
            db.execSQL("drop table bookmark");
            db.execSQL("drop table animal");
            onCreate(db);
        }
    }
}


