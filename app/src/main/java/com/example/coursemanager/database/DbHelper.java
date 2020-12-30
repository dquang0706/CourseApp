package com.example.coursemanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String dbName = "CourseManager.db";
    private static final int dbVersion = 1;

    public DbHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableSql = "create table User(id text primary key not null," +
                "passWord text not null)";
        sqLiteDatabase.execSQL(createTableSql);
        createTableSql = "create table Course(" +
                "courseCode integer primary key autoincrement," +
                "name text not null," +
                "startDay date not null," +
                "tutorName text not null)";
        sqLiteDatabase.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTableSql = "drop table if exists User";
        sqLiteDatabase.execSQL(dropTableSql);
        dropTableSql = "drop table if exists Course";
        sqLiteDatabase.execSQL(dropTableSql);
        onCreate(sqLiteDatabase);
    }
}
